package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class InterfaceGraphique extends JFrame {

    private Bibliotheque maBiblio;
    private GestionDonnees gestion;
    private JTable tableauDocuments;
    private DefaultTableModel modeleTableau;

    public InterfaceGraphique() {
        // 1. Initialisation des données
        maBiblio = new Bibliotheque();
        gestion = new GestionDonnees();
        gestion.charger(maBiblio); // On charge les fichiers txt dès le lancement

        // 2. Configuration de la fenêtre
        setTitle("Gestion de Bibliothèque Universitaire");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer à l'écran
        setLayout(new BorderLayout());

        // 3. Création du Tableau (JTable)
        // Les colonnes du tableau
        String[] colonnes = {"Type", "Titre", "Référence", "Prix", "Stock", "Détails (Auteur/Num)"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableauDocuments = new JTable(modeleTableau);
        
        // Ajouter le tableau dans une zone avec barre de défilement (Scroll)
        JScrollPane scrollPane = new JScrollPane(tableauDocuments);
        add(scrollPane, BorderLayout.CENTER);

        // 4. Création des Boutons (En bas)
        JPanel panneauBoutons = new JPanel();
        
        JButton btnSauvegarder = new JButton("Sauvegarder & Quitter");
        JButton btnActualiser = new JButton("Actualiser la liste");
        
        panneauBoutons.add(btnActualiser);
        panneauBoutons.add(btnSauvegarder);
        add(panneauBoutons, BorderLayout.SOUTH);

        // 5. Actions des Boutons
        btnActualiser.addActionListener(e -> rafraichirTableau());
        
        btnSauvegarder.addActionListener(e -> {
            gestion.sauvegarder(maBiblio);
            JOptionPane.showMessageDialog(this, "Données sauvegardées !");
            System.exit(0);
        });

        // Remplir le tableau au démarrage
        rafraichirTableau();
    }

    // --- Méthode pour transformer ta List<Document> en lignes de Tableau ---
    private void rafraichirTableau() {
        // 1. Vider le tableau actuel
        modeleTableau.setRowCount(0);

        // 2. Récupérer la liste depuis ta classe Bibliotheque
        List<Document> docs = maBiblio.obtenirToutLesDocuments();

        // 3. Parcourir et ajouter les lignes
        for (Document d : docs) {
            String type = "";
            String details = "";

            if (d instanceof Livre) {
                type = "Livre";
                details = ((Livre) d).getNomAuteur();
            } else if (d instanceof Periodique) {
                type = "Périodique";
                details = "N°" + ((Periodique) d).getNumero();
            }

            // On crée un tableau d'objets pour la ligne
            Object[] ligne = {
                type,
                d.getTitre(),
                d.getReference(),
                d.getPrix() + " €",
                d.getNbExemplaire(),
                details
            };
            
            // Hop, on ajoute la ligne visuellement
            modeleTableau.addRow(ligne);
        }
    }

    // --- MAIN pour lancer la fenêtre ---
    public static void main(String[] args) {
        // Pour avoir le look & feel moderne de ton système d'exploitation
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        // Lancer la fenêtre
        SwingUtilities.invokeLater(() -> {
            InterfaceGraphique fenetre = new InterfaceGraphique();
            fenetre.setVisible(true);
        });
    }
}