package graphic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import library.*;

public class FenetrePrincipale extends JFrame {
    
    private Bibliotheque maBiblio;
    private GestionDonnees gestion;
    
    // Tableaux
    private DefaultTableModel modelDocs, modelLecteurs, modelPrets;
    private JTable tablePrets; // On le garde en attribut pour savoir quelle ligne est sélectionnée
    
    public FenetrePrincipale() {
        // --- INIT ---
        maBiblio = new Bibliotheque();
        gestion = new GestionDonnees();
        gestion.charger(maBiblio);
        
        setTitle("Système de Gestion Bibliothèque");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // --- ONGLETS ---
        JTabbedPane onglets = new JTabbedPane();
        
        // Onglet 1 : Documents
        modelDocs = new DefaultTableModel(new String[]{"Type", "Ref", "Titre", "Prix", "Stock", "Info"}, 0);
        onglets.addTab("Documents", new JScrollPane(new JTable(modelDocs)));
        
        // Onglet 2 : Lecteurs
        modelLecteurs = new DefaultTableModel(new String[]{"Type", "Nom", "Email", "Institut", "Emprunts", "Détail"}, 0);
        onglets.addTab("Lecteurs", new JScrollPane(new JTable(modelLecteurs)));
        
        // Onglet 3 : Prêts (Nouveau !)
        modelPrets = new DefaultTableModel(new String[]{"Document", "Lecteur", "Email", "Institut", "Date Prêt", "Retour Prévu"}, 0);
        tablePrets = new JTable(modelPrets);
        onglets.addTab("Prêts en cours", new JScrollPane(tablePrets));
        
        add(onglets, BorderLayout.CENTER);
        
        // --- BARRE D'OUTILS (Boutons) ---
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // Fixer la barre
        
        JButton btnAddDoc = new JButton("Ajouter Document");
        JButton btnAddLecteur = new JButton("Ajouter Lecteur");
        toolbar.add(btnAddDoc);
        toolbar.add(btnAddLecteur);
        toolbar.addSeparator();
        
        JButton btnNouveauPret = new JButton("NOUVEAU PRÊT");
        btnNouveauPret.setBackground(new Color(200, 255, 200)); // Vert clair
        toolbar.add(btnNouveauPret);
        toolbar.addSeparator();
        
        JButton btnRetour = new JButton("Retourner Document");
        JButton btnPerte = new JButton("Déclarer Perte");
        toolbar.add(btnRetour);
        toolbar.add(btnPerte);
        toolbar.addSeparator();
        
        JButton btnRetards = new JButton("⚠️ Voir Retards");
        btnRetards.setForeground(Color.RED);
        toolbar.add(btnRetards);
        
        add(toolbar, BorderLayout.NORTH);
        
        // --- NOUVEAU BOUTON ---
        JButton btnProlonger = new JButton("Prolonger (+14j)");
        btnProlonger.setBackground(new Color(255, 255, 200)); // Jaune clair
        // ----------------------
        
        toolbar.add(btnRetour);
        toolbar.add(btnPerte);
        toolbar.add(btnProlonger); // On l'ajoute à la barre
        toolbar.addSeparator();
        
        // --- BOUTONS DU BAS ---
        JPanel bas = new JPanel();
        JButton btnRefresh = new JButton("Actualiser");
        JButton btnSave = new JButton("Sauvegarder");
        bas.add(btnRefresh);
        bas.add(btnSave);
        add(bas, BorderLayout.SOUTH);
        
        // --- ACTIONS ---
        
        // 1. Ajouts (Via la classe Formulaires)
        btnAddDoc.addActionListener(e -> Formulaires.ajouterDocument(this, maBiblio, this::rafraichirTout));
        btnAddLecteur.addActionListener(e -> Formulaires.ajouterLecteur(this, maBiblio, this::rafraichirTout));
        btnNouveauPret.addActionListener(e -> Formulaires.nouveauPret(this, maBiblio, this::rafraichirTout));
        
        // 2. Gestion Prêts (Retour)
        btnRetour.addActionListener(e -> actionRetourPret(false)); // False = pas perdu
        
        // 3. Gestion Prêts (Perte)
        btnPerte.addActionListener(e -> actionRetourPret(true));  // True = déclaré perdu
        
        // 4. Notification Retards
        btnRetards.addActionListener(e -> afficherRetards());
        
        // 5. Système
        btnRefresh.addActionListener(e -> {
            maBiblio.toutEffacer();
            gestion.charger(maBiblio);
            rafraichirTout();
        });
        
        btnSave.addActionListener(e -> {
            gestion.sauvegarder(maBiblio);
            JOptionPane.showMessageDialog(this, "Sauvegarde terminée !");
            System.exit(0);
        });
        
        // ... autres actions ...
        
        btnProlonger.addActionListener(e -> actionProlonger());
        
        rafraichirTout();
    }
    
    // --- LOGIQUE METIER ---
    
    private void actionRetourPret(boolean estPerdu) {
        int ligneSelect = tablePrets.getSelectedRow();
        if (ligneSelect == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans l'onglet 'Prêts' d'abord.");
            return;
        }
        
        // 1. Récupération des infos visuelles
        String email = (String) modelPrets.getValueAt(ligneSelect, 2);
        String refDoc = extractRefFromTitle((String) modelPrets.getValueAt(ligneSelect, 0));
        
        // 2. Recherche du vrai objet Prêt
        Pret pretConcerne = null;
        for(Pret p : maBiblio.getToutLesPrets()) {
            if(p.getLecteur().getEmail().equals(email) && p.getDocument().getReference().equals(refDoc)) {
                pretConcerne = p;
                break;
            }
        }
        
        if(pretConcerne != null) {
            Document doc = pretConcerne.getDocument();
            Lecteur lecteur = pretConcerne.getLecteur();
            
            if(estPerdu) {
                // Action backend
                maBiblio.declarationPerte(lecteur, doc);
                
                // --- CALCUL DU PRIX (CORRECTION ICI) ---
                double prix = doc.getPrix();
                double taux = 0.0; // Par défaut (Périodique) c'est 0
                
                // Si c'est un Livre, on récupère son taux spécifique
                if (doc instanceof Livre) {
                    Livre l = (Livre) doc; 
                    taux = l.getTauxRemboursement();
                }
                
                double aPayer = prix + (prix * taux);
                // ----------------------------------------
                
                JOptionPane.showMessageDialog(this, "PERTE DÉCLARÉE.\n" + lecteur.getNom() + 
                " doit payer : " + aPayer + " €");
            } else {
                // Retour normal
                maBiblio.retourPret(lecteur, doc);
                JOptionPane.showMessageDialog(this, "Document retourné avec succès.");
            }
            
            // Mise à jour de l'affichage
            rafraichirTout();
        }
    }
    
    private void afficherRetards() {
        StringBuilder msg = new StringBuilder("LISTE DES RETARDS :\n\n");
        boolean retardTrouve = false;
        LocalDate aujourdhui = LocalDate.now();
        
        for(Pret p : maBiblio.getToutLesPrets()) {
            // On recalcule la date de retour prévue
            LocalDate retourPrevu = p.getDatePret().plusDays(p.getLecteur().getDureePret());
            
            if(aujourdhui.isAfter(retourPrevu)) {
                long joursRetard = ChronoUnit.DAYS.between(retourPrevu, aujourdhui);
                msg.append("- ").append(p.getLecteur().getNom())
                .append(" (").append(p.getDocument().getTitre()).append(")")
                .append(" : ").append(joursRetard).append(" jours de retard.\n");
                retardTrouve = true;
            }
        }
        
        if(!retardTrouve) msg.append("Aucun retard à signaler ! Bravo.");
        
        JOptionPane.showMessageDialog(this, msg.toString(), "Notifications Retards", JOptionPane.WARNING_MESSAGE);
    }
    
    // --- METHODES D'AFFICHAGE ---
    
    private void rafraichirTout() {
        // 1. Docs
        modelDocs.setRowCount(0);
        for (Document d : maBiblio.obtenirToutLesDocuments()) {
            String info = (d instanceof Livre) ? ((Livre)d).getNomAuteur() : "N°"+((Periodique)d).getNumero();
            modelDocs.addRow(new Object[]{(d instanceof Livre ? "Livre" : "Périodique"), d.getReference(), d.getTitre(), d.getPrix(), d.getNbExemplaire(), info});
        }
        
        // 2. Lecteurs
        modelLecteurs.setRowCount(0);
        for (Lecteur l : maBiblio.obtenirToutLesLecteurs()) {
            String type = (l instanceof Etudiant) ? "Étudiant" : "Enseignant";
            String info = (l instanceof Etudiant) ? ((Etudiant)l).getAdressePostale() : ((Enseignant)l).getTelephone();
            int encours = maBiblio.getNbEmpruntsEnCours(l);
            modelLecteurs.addRow(new Object[]{type, l.getNom(), l.getEmail(), l.getInstitut(), encours + "/" + l.getMaxEmprunt(), info});
        }
        
        // 3. Prêts
        modelPrets.setRowCount(0);
        for (Pret p : maBiblio.getToutLesPrets()) {
            LocalDate dateRetour = p.getDatePret().plusDays(p.getLecteur().getDureePret());
            // Astuce : Je mets la Ref dans le titre pour pouvoir la retrouver lors du clic "Retour"
            String titreComplet = "[" + p.getDocument().getReference() + "] " + p.getDocument().getTitre();
            
            modelPrets.addRow(new Object[]{
                titreComplet,
                p.getLecteur().getNom(),
                p.getLecteur().getEmail(),
                p.getLecteur().getInstitut(),
                p.getDatePret(),
                dateRetour
            });
        }
    }
    
    private void actionProlonger() {
        int ligneSelect = tablePrets.getSelectedRow();
        if (ligneSelect == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un prêt à prolonger.");
            return;
        }
        
        // 1. Récupérer les infos
        String email = (String) modelPrets.getValueAt(ligneSelect, 2); // Colonne Email
        String refDoc = extractRefFromTitle((String) modelPrets.getValueAt(ligneSelect, 0)); // Colonne Titre
        
        // 2. Trouver les objets
        Lecteur l = null;
        Document d = null;
        
        // On cherche dans la liste des prêts actuels pour retrouver les objets liés
        for(Pret p : maBiblio.getToutLesPrets()) {
            if(p.getLecteur().getEmail().equals(email) && p.getDocument().getReference().equals(refDoc)) {
                l = p.getLecteur();
                d = p.getDocument();
                break;
            }
        }
        
        if (l != null && d != null) {
            // 3. Appel au backend
            boolean succes = maBiblio.prolongationPret(l, d);
            
            if (succes) {
                JOptionPane.showMessageDialog(this, "Prêt prolongé de 14 jours !");
                rafraichirTout(); // Mise à jour du tableau
            } else {
                JOptionPane.showMessageDialog(this, "Impossible : Ce prêt est déjà prolongé ou n'existe pas.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    // Petit utilitaire pour extraire "REF1" de "[REF1] Titre du livre"
    private String extractRefFromTitle(String fullTitle) {
        if(fullTitle.startsWith("[")) {
            return fullTitle.substring(1, fullTitle.indexOf("]"));
        }
        return fullTitle;
    }
}