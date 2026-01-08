package graphic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import library.*;

public class FenetrePrincipale extends JFrame {

    private Bibliotheque maBiblio;
    private GestionDonnees gestion;
    private JTable tableauDocuments;
    private DefaultTableModel modeleTableau;

    public FenetrePrincipale() {
        // Initialisation
        maBiblio = new Bibliotheque();
        gestion = new GestionDonnees();
        gestion.charger(maBiblio);

        // Configuration Fenêtre
        setTitle("Gestion de Bibliothèque Universitaire");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tableau
        String[] colonnes = {"Type", "Titre", "Référence", "Prix", "Stock", "Détails"};
        modeleTableau = new DefaultTableModel(colonnes, 0);
        tableauDocuments = new JTable(modeleTableau);
        add(new JScrollPane(tableauDocuments), BorderLayout.CENTER);

        // Boutons
        JPanel panneauBoutons = new JPanel();
        JButton btnAjoutLivre = new JButton("Ajouter Livre");
        JButton btnAjoutEtudiant = new JButton("Ajouter Étudiant");
        JButton btnActualiser = new JButton("Actualiser");
        JButton btnSauvegarder = new JButton("Sauvegarder");

        panneauBoutons.add(btnAjoutLivre);
        panneauBoutons.add(btnAjoutEtudiant);
        panneauBoutons.add(Box.createHorizontalStrut(20));
        panneauBoutons.add(btnActualiser);
        panneauBoutons.add(btnSauvegarder);
        add(panneauBoutons, BorderLayout.SOUTH);

        // --- ACTIONS DES BOUTONS (C'est là que ça change) ---

        // Appel à la classe externe FormulaireLivre
        // On passe "this::rafraichirTableau" pour qu'il puisse mettre à jour le tableau après l'ajout
        btnAjoutLivre.addActionListener(e -> {
            FormulaireLivre.afficher(this, maBiblio, this::rafraichirTableau);
        });

        // Appel à la classe externe FormulaireEtudiant
        btnAjoutEtudiant.addActionListener(e -> {
            FormulaireEtudiant.afficher(this, maBiblio);
        });

        btnActualiser.addActionListener(e -> {
            maBiblio.toutEffacer();
            gestion.charger(maBiblio);
            rafraichirTableau();
            JOptionPane.showMessageDialog(this, "Données rechargées !");
        });

        btnSauvegarder.addActionListener(e -> {
            gestion.sauvegarder(maBiblio);
            JOptionPane.showMessageDialog(this, "Sauvegarde effectuée !");
            System.exit(0);
        });

        rafraichirTableau();
    }

    // Cette méthode reste ici car elle touche directement au tableau de la fenêtre
    private void rafraichirTableau() {
        modeleTableau.setRowCount(0);
        List<Document> docs = maBiblio.obtenirToutLesDocuments();

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

            Object[] ligne = {
                type, d.getTitre(), d.getReference(), 
                d.getPrix() + " €", d.getNbExemplaire(), details
            };
            modeleTableau.addRow(ligne);
        }
    }
    
}
