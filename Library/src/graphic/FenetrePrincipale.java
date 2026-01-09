package graphic;

import gestion.*;
import modele.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FenetrePrincipale extends JFrame {
    
    private Bibliotheque maBiblio;
    private GestionDonnees gestion;
    
    // Tableaux
    private DefaultTableModel modelDocs, modelLecteurs, modelPrets;
    private JTable tablePrets; 
    
    public FenetrePrincipale() {
        
        maBiblio = new Bibliotheque();
        gestion = new GestionDonnees();
        gestion.charger(maBiblio);
        
        setTitle("La tour de Babel !");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // --- ONGLETS ---
        JTabbedPane onglets = new JTabbedPane();
        
        // Onglet 1 : Documents
        modelDocs = new DefaultTableModel(new String[]{"Type", "Ref", "Titre", "Prix", "Stock", "Info", "Taux/Année"}, 0);
        onglets.addTab("Documents", new JScrollPane(new JTable(modelDocs)));
        
        // Onglet 2 : Lecteurs
        modelLecteurs = new DefaultTableModel(new String[]{"Type", "Nom", "Email", "Institut", "Durée Max","Emprunts","Détail"}, 0);
        onglets.addTab("Lecteurs", new JScrollPane(new JTable(modelLecteurs)));
        
        // Onglet 3 : Prêts
        modelPrets = new DefaultTableModel(new String[]{"Document", "Lecteur", "Email", "Institut", "Date Prêt", "Retour Prévu", "Prolongé?"}, 0);
        tablePrets = new JTable(modelPrets);
        onglets.addTab("Prêts en cours", new JScrollPane(tablePrets));
        
        
        add(onglets, BorderLayout.CENTER);
        
        // --- BARRE D'OUTILS (Boutons) ---
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        JButton btnAddDoc = new JButton("Ajouter Document");
        JButton btnModifDoc = new JButton("Modifier Document");
        
        JButton btnAddLecteur = new JButton("Ajouter Lecteur");
        JButton btnSuppr = new JButton("Supprimer");
        btnSuppr.setForeground(Color.RED); 
        JButton btnModifLecteur = new JButton("Modifier Lecteur");
        
        toolbar.add(btnAddDoc);
        toolbar.add(btnModifDoc);
        toolbar.addSeparator();
        
        toolbar.add(btnAddLecteur);
        toolbar.add(btnModifLecteur);
        toolbar.add(btnSuppr);        
        toolbar.addSeparator();
        
        JButton btnNouveauPret = new JButton("Nouveau prêt");
        btnNouveauPret.setBackground(new Color(200, 255, 200));
        toolbar.add(btnNouveauPret);
        JButton btnProlonger = new JButton("Prolonger prêt");
        btnProlonger.setBackground(new Color(200, 255, 200));
        toolbar.add(btnProlonger);
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
        
        // --- BOUTONS DU BAS ---
        JPanel bas = new JPanel();
        JButton btnRefresh = new JButton("Par défaut");
        JButton btnSave = new JButton("Sauvegarder");
        bas.add(btnRefresh);
        bas.add(btnSave);
        add(bas, BorderLayout.SOUTH);
        
        // --- ACTIONS ---
        
        // 1. Ajouts
        btnAddDoc.addActionListener(e -> Formulaires.ajouterDocument(this, maBiblio, this::rafraichirTout));
        btnAddLecteur.addActionListener(e -> Formulaires.ajouterLecteur(this, maBiblio, this::rafraichirTout));
        btnNouveauPret.addActionListener(e -> Formulaires.nouveauPret(this, maBiblio, this::rafraichirTout));
        
        // 2. Suppression (Nouveau)
        btnSuppr.addActionListener(e -> actionSupprimer());
        
        // 3. Modification (Nouveau)
        btnModifLecteur.addActionListener(e -> actionModifierLecteur());
        btnModifDoc.addActionListener(e -> actionModifierDocument());
        
        // 4. Gestion Prêts
        btnRetour.addActionListener(e -> actionRetourPret(false));
        btnPerte.addActionListener(e -> actionRetourPret(true));
        btnProlonger.addActionListener(e -> actionProlonger());
        
        // 5. Retards & Système
        btnRetards.addActionListener(e -> afficherRetards());
        
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
        
        rafraichirTout();
    }
    

    
    // Supprimer un document ou un lecteur
    private void actionSupprimer() {
        JTabbedPane onglets = (JTabbedPane) ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        int indexOnglet = onglets.getSelectedIndex();
        
        if (indexOnglet == 0) { // Onglet DOCUMENTS
            int ligne = ((JTable)((JScrollPane)onglets.getComponentAt(0)).getViewport().getView()).getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez une ligne."); return; }
            
            String ref = (String) modelDocs.getValueAt(ligne, 1);
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le document " + ref + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Document d = trouverDocument(ref);
                if(d != null) {
                    maBiblio.obtenirToutLesDocuments().remove(d);
                    rafraichirTout();
                    JOptionPane.showMessageDialog(this, "Document supprimé.");
                }
            }
        } 
        else if (indexOnglet == 1) { // Onglet LECTEURS
            int ligne = ((JTable)((JScrollPane)onglets.getComponentAt(1)).getViewport().getView()).getSelectedRow();
            if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionnez un lecteur."); return; }
            
            String email = (String) modelLecteurs.getValueAt(ligne, 2);
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le lecteur " + email + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Lecteur l = trouverLecteur(email);
                if(l != null) {
                    if (maBiblio.getNbEmpruntsEnCours(l) > 0) {
                        JOptionPane.showMessageDialog(this, "Impossible : Ce lecteur a des livres non rendus.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        maBiblio.obtenirToutLesLecteurs().remove(l);
                        rafraichirTout();
                        JOptionPane.showMessageDialog(this, "Lecteur supprimé.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Placez-vous sur l'onglet Documents ou Lecteurs pour supprimer.");
        }
    }
    
    // Modifier un lecteur
    private void actionModifierLecteur() {
        JTabbedPane onglets = (JTabbedPane) ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (onglets.getSelectedIndex() != 1) {
            JOptionPane.showMessageDialog(this, "Veuillez aller sur l'onglet 'Lecteurs' et sélectionner une ligne.");
            return;
        }
        
        JTable table = (JTable)((JScrollPane)onglets.getComponentAt(1)).getViewport().getView();
        int ligne = table.getSelectedRow();
        if (ligne == -1) return;
        
        String email = (String) modelLecteurs.getValueAt(ligne, 2);
        Lecteur l = trouverLecteur(email);
        
        if (l != null) {
            JTextField champMax = new JTextField(String.valueOf(l.getMaxEmprunt()));
            JTextField champDuree = new JTextField(String.valueOf(l.getDureePret()));
            
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nouveau Quota Max (Livres) :"));
            panel.add(champMax);
            panel.add(new JLabel("Nouvelle Durée (Jours) :"));
            panel.add(champDuree);
            
            int res = JOptionPane.showConfirmDialog(this, panel, "Modifier " + l.getNom(), JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int newMax = Integer.parseInt(champMax.getText());
                    int newDuree = Integer.parseInt(champDuree.getText());
                    
                    l.setMaxEmprunt(newMax);
                    
                    // Condition Etudiant / Enseignant
                    if (l instanceof Etudiant) ((Etudiant)l).setDureePret(newDuree);
                    else if (l instanceof Enseignant) ((Enseignant)l).setDureePret(newDuree);
                    
                    rafraichirTout();
                    JOptionPane.showMessageDialog(this, "Paramètres modifiés avec succès !");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
                }
            }
        }
    }
    
    // Méthode pour modifier les propriétés des documents (sauf REF !!!!)
    private void actionModifierDocument() {
        JTabbedPane onglets = (JTabbedPane) ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        // Vérif qu'on est sur le bon onglet
        if (onglets.getSelectedIndex() != 0) {
            JOptionPane.showMessageDialog(this, "Veuillez aller sur l'onglet 'Documents' et sélectionner une ligne.");
            return;
        }
        
        JTable table = (JTable)((JScrollPane)onglets.getComponentAt(0)).getViewport().getView();
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans le tableau des documents !");
            return; // On arrête tout ici
        }
        
        // On récupère la référence (Colonne 1)
        String ref = (String) modelDocs.getValueAt(ligne, 1);
        
        Document d = null;
        for(Document doc : maBiblio.obtenirToutLesDocuments()) {
            if(doc.getReference().equals(ref)) { d = doc; break; }
        }
        
        if (d != null) {
            // --- 1. Création des champs ---
            JTextField champTitre = new JTextField(d.getTitre());
            JTextField champPrix = new JTextField(String.valueOf(d.getPrix()));
            JTextField champStock = new JTextField(String.valueOf(d.getNbExemplaire()));
            
            // Champs Spécifiques
            JTextField champSpecifique1 = new JTextField(); // Auteur ou Numéro
            JTextField champSpecifique2 = new JTextField(); // Taux ou Année
            
            String label1 = "", label2 = "";
            
            if (d instanceof Livre) {
                label1 = "Auteur :";
                label2 = "Taux Remboursement (0.x) :";
                champSpecifique1.setText(((Livre)d).getNomAuteur());
                champSpecifique2.setText(String.valueOf(((Livre)d).getTauxRemboursement()));
            } else if (d instanceof Periodique) {
                label1 = "Numéro :";
                label2 = "Année :";
                champSpecifique1.setText(String.valueOf(((Periodique)d).getNumero()));
                champSpecifique2.setText(String.valueOf(((Periodique)d).getAnneeParution()));
            }
            
            // --- 2. Construction du Formulaire ---
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Titre :"));      panel.add(champTitre);
            panel.add(new JLabel("Prix :"));       panel.add(champPrix);
            panel.add(new JLabel("Stock :"));      panel.add(champStock);
            
            panel.add(new JSeparator());
            panel.add(new JLabel(label1));         panel.add(champSpecifique1);
            panel.add(new JLabel(label2));         panel.add(champSpecifique2);
            
            panel.add(new JLabel("⚠️ La référence ne peut pas être modifiée."));
            
            int res = JOptionPane.showConfirmDialog(this, panel, "Modifier " + d.getReference(), JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String textePrix = champPrix.getText().replace(",", ".");
                    // 1. On sauvegarde les infos communes
                    d.setTitre(champTitre.getText());
                    d.setPrix((int) Float.parseFloat(textePrix));
                    d.defNbExemplaire(Integer.parseInt(champStock.getText()));
                    
                    // 2. On sauvegarde les infos spécifiques (C'est ICI que ça se joue)
                    if (d instanceof Livre) {
                        // On cast en Livre pour accéder aux méthodes setNomAuteur et setTauxRemboursement
                        Livre leLivre = (Livre) d; 
                        leLivre.setNomAuteur(champSpecifique1.getText());
                        
                        // Attention au Float ici !
                        leLivre.setTauxRemboursement(Float.parseFloat(champSpecifique2.getText()));
                    } 
                    else if (d instanceof Periodique) {
                        Periodique lePeriodique = (Periodique) d;
                        lePeriodique.setNumero(Integer.parseInt(champSpecifique1.getText()));
                        lePeriodique.setAnneeParution(Integer.parseInt(champSpecifique2.getText()));
                    }
                    
                    rafraichirTout();
                    JOptionPane.showMessageDialog(this, "Document mis à jour !");
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
                }
            }
        }
    }
    
    private void actionRetourPret(boolean estPerdu) {
        int ligneSelect = tablePrets.getSelectedRow();
        if (ligneSelect == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne dans l'onglet 'Prêts' d'abord.");
            return;
        }
        
        String email = (String) modelPrets.getValueAt(ligneSelect, 2);
        String refDoc = extractRefFromTitle((String) modelPrets.getValueAt(ligneSelect, 0));
        
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
                maBiblio.declarationPerte(lecteur, doc);
                double prix = doc.getPrix();
                double taux = 0.0;
                if (doc instanceof Livre) {
                    Livre l = (Livre) doc; 
                    taux = l.getTauxRemboursement();
                }
                double aPayer = prix + (prix * taux);
                JOptionPane.showMessageDialog(this, "PERTE DÉCLARÉE.\n" + lecteur.getNom() + 
                " doit payer : " + aPayer + " €");
            } else {
                maBiblio.retourPret(lecteur, doc);
                JOptionPane.showMessageDialog(this, "Document retourné avec succès.");
            }
            rafraichirTout();
        }
    }
    
    private void actionProlonger() {
        int ligneSelect = tablePrets.getSelectedRow();
        if (ligneSelect == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un prêt.");
            return;
        }
        
        String email = (String) modelPrets.getValueAt(ligneSelect, 2);
        String refDoc = extractRefFromTitle((String) modelPrets.getValueAt(ligneSelect, 0));
        
        Lecteur l = trouverLecteur(email);
        Document d = trouverDocument(refDoc);
        
        if (l != null && d != null) {
            // DEMANDE UTILISATEUR
            String input = JOptionPane.showInputDialog(this, 
                "Combien de jours ajouter ? (Max " + Bibliotheque.MAX_JOURS_PAR_RALLONGE + ")", 
                "Prolongation", JOptionPane.QUESTION_MESSAGE);
                
                if (input == null || input.isEmpty()) return; // Annulation
                
                try {
                    int jours = Integer.parseInt(input);
                    
                    // APPEL BACKEND
                    String resultat = maBiblio.prolongationPret(l, d, jours);
                    
                    if (resultat.equals("SUCCÈS")) {
                        JOptionPane.showMessageDialog(this, "Prêt rallongé de " + jours + " jours !");
                        rafraichirTout();
                    } else {
                        JOptionPane.showMessageDialog(this, resultat, "Attention", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Entrez un nombre valide.");
                }
            }
        }
        
        private void afficherRetards() {
            StringBuilder msg = new StringBuilder("LISTE DES RETARDS :\n\n");
            boolean retardTrouve = false;
            LocalDate aujourdhui = LocalDate.now();
            
            for(Pret p : maBiblio.getToutLesPrets()) {
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
        
        private void rafraichirTout() {
            // 1. Docs
            modelDocs.setRowCount(0);
            for (Document d : maBiblio.obtenirToutLesDocuments()) {
                String type = (d instanceof Livre) ? "Livre" : "Périodique";
                String info1 = (d instanceof Livre) ? ((Livre)d).getNomAuteur() : String.valueOf(((Periodique)d).getNumero());
                String info2 = (d instanceof Livre) ? (((Livre)d).getTauxRemboursement() * 100) + "%" : String.valueOf(((Periodique)d).getAnneeParution());
                modelDocs.addRow(new Object[]{type, d.getReference(), d.getTitre(), d.getPrix() + "€", d.getNbExemplaire(), info1, info2});
            }
            // 2. Lecteurs
            modelLecteurs.setRowCount(0);
            for (Lecteur l : maBiblio.obtenirToutLesLecteurs()) {
                String type = (l instanceof Etudiant) ? "Étudiant" : "Enseignant";
                String info = (l instanceof Etudiant) ? ((Etudiant)l).getAdressePostale() : ((Enseignant)l).getTelephone();
                int encours = maBiblio.getNbEmpruntsEnCours(l);
                
                modelLecteurs.addRow(new Object[]{
                    type, 
                    l.getNom(), 
                    l.getEmail(), 
                    l.getInstitut(), 
                    l.getDureePret() + " jours", 
                    encours + "/" + l.getMaxEmprunt(), 
                    info
                });
            }
            // 3. Prêts
            modelPrets.setRowCount(0);
            for (Pret p : maBiblio.getToutLesPrets()) {
                long dureeBase = p.getLecteur().getDureePret();
                long bonus = p.getJoursSupplementaires(); // On récupère le total accumulé
                
                LocalDate dateRetour = p.getDatePret().plusDays(dureeBase + bonus);
                
                //  "Oui (1/2)" ou "Non"
                String infoProlong = (p.getNbProlongations() > 0) 
                ? "Oui (" + p.getNbProlongations() + "/" + Bibliotheque.MAX_PROLONGATIONS_AUTORISEES + ")" 
                : "Non";
                
                String titreComplet = "[" + p.getDocument().getReference() + "] " + p.getDocument().getTitre();
                
                modelPrets.addRow(new Object[]{
                    titreComplet,
                    p.getLecteur().getNom(),
                    p.getLecteur().getEmail(),
                    p.getLecteur().getInstitut(),
                    p.getDatePret(),
                    dateRetour,    
                    infoProlong    
                });
            }
        }
        
        // --- UTILITAIRES ---
        
        private Document trouverDocument(String ref) {
            for(Document d : maBiblio.obtenirToutLesDocuments()) {
                if(d.getReference().equals(ref)) return d;
            }
            return null;
        }
        
        private Lecteur trouverLecteur(String email) {
            for(Lecteur l : maBiblio.obtenirToutLesLecteurs()) {
                if(l.getEmail().equals(email)) return l;
            }
            return null;
        }
        
        private String extractRefFromTitle(String fullTitle) {
            if(fullTitle.startsWith("[")) {
                return fullTitle.substring(1, fullTitle.indexOf("]"));
            }
            return fullTitle;
        }
        
    }