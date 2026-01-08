package graphic;

import library.*;
import javax.swing.*;
import java.awt.*;


public class FormulaireEtudiant {

    public static void afficher(Component parent, Bibliotheque biblio) {
        
        JTextField champNom = new JTextField();
        JTextField champEmail = new JTextField();
        JTextField champInstitut = new JTextField();
        JTextField champAdresse = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nom complet :"));
        panel.add(champNom);
        panel.add(new JLabel("Email :"));
        panel.add(champEmail);
        panel.add(new JLabel("Institut / Faculté :"));
        panel.add(champInstitut);
        panel.add(new JLabel("Adresse Postale :"));
        panel.add(champAdresse);

        int resultat = JOptionPane.showConfirmDialog(parent, panel, 
                "Nouvel Étudiant", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                String nom = champNom.getText();
                String email = champEmail.getText();
                String institut = champInstitut.getText();
                String adresse = champAdresse.getText();

                Etudiant nouvelEtudiant = new Etudiant(nom, email, institut, adresse);
                biblio.AjouterLecteur(nouvelEtudiant);

                JOptionPane.showMessageDialog(parent, "Étudiant ajouté avec succès !");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Erreur : " + ex.getMessage());
            }
        }
    }
}