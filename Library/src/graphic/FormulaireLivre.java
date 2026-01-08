package graphic;

import library.*;
import javax.swing.*;
import java.awt.*;


public class FormulaireLivre {

    // Méthode statique : on peut l'appeler sans faire "new FormulaireLivre()"
    public static void afficher(Component parent, Bibliotheque biblio, Runnable actionMiseAJour) {
        
        JTextField champRef = new JTextField();
        JTextField champTitre = new JTextField();
        JTextField champAuteur = new JTextField();
        JTextField champPrix = new JTextField();
        JTextField champStock = new JTextField();
        JTextField champTaux = new JTextField("0.1");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Référence (ex: LIV_001) :"));
        panel.add(champRef);
        panel.add(new JLabel("Titre :"));
        panel.add(champTitre);
        panel.add(new JLabel("Auteur :"));
        panel.add(champAuteur);
        panel.add(new JLabel("Prix (entier) :"));
        panel.add(champPrix);
        panel.add(new JLabel("Nombre d'exemplaires :"));
        panel.add(champStock);
        panel.add(new JLabel("Taux remboursement (ex: 0.2) :"));
        panel.add(champTaux);

        int resultat = JOptionPane.showConfirmDialog(parent, panel, 
                "Nouveau Livre", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                String ref = champRef.getText();
                String titre = champTitre.getText();
                String auteur = champAuteur.getText();
                int prix = Integer.parseInt(champPrix.getText());
                int stock = Integer.parseInt(champStock.getText());
                float taux = Float.parseFloat(champTaux.getText());

                Livre nouveauLivre = new Livre(ref, titre, prix, stock, auteur, taux);
                biblio.AjouterDocument(nouveauLivre);

                // C'est ici qu'on dit à la fenêtre principale de se mettre à jour
                if (actionMiseAJour != null) {
                    actionMiseAJour.run();
                }
                
                JOptionPane.showMessageDialog(parent, "Livre ajouté avec succès !");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}