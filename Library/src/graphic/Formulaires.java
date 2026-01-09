package graphic;

import javax.swing.*;

import gestion.Bibliotheque;

import java.awt.*;
import modele.Document;
import modele.Enseignant;
import modele.Etudiant;
import modele.Lecteur;
import modele.Livre;
import modele.Periodique;

public class Formulaires {

    // Ajout Doc
    public static void ajouterDocument(Component parent, Bibliotheque biblio, Runnable updateCallback) {
        // Choix du type
        String[] options = {"Livre", "Périodique"};
        int choix = JOptionPane.showOptionDialog(parent, "Quel type de document ?", "Nouveau Document",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choix == -1) return; // Annulé

        JTextField champRef = new JTextField();
        JTextField champTitre = new JTextField();
        JTextField champPrix = new JTextField();
        JTextField champStock = new JTextField();
        // Champs spécifiques
        JTextField champVariable1 = new JTextField(); // Auteur OU Numéro
        JTextField champVariable2 = new JTextField(); // Taux OU Année

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Référence :")); panel.add(champRef);
        panel.add(new JLabel("Titre :")); panel.add(champTitre);
        panel.add(new JLabel("Prix :")); panel.add(champPrix);
        panel.add(new JLabel("Stock :")); panel.add(champStock);

        if (choix == 0) { // LIVRE
            panel.add(new JLabel("Auteur :")); panel.add(champVariable1);
            panel.add(new JLabel("Taux remb. (0.x) :")); 
            champVariable2.setText("0.1"); // Valeur défaut
            panel.add(champVariable2);
        } else { // PERIODIQUE
            panel.add(new JLabel("Numéro :")); panel.add(champVariable1);
            panel.add(new JLabel("Année :")); panel.add(champVariable2);
        }

        int res = JOptionPane.showConfirmDialog(parent, panel, "Ajout " + options[choix], JOptionPane.OK_CANCEL_OPTION);
        
        if (res == JOptionPane.OK_OPTION) {
            try {
                String ref = champRef.getText();
                String titre = champTitre.getText();
                int prix = Integer.parseInt(champPrix.getText());
                int stock = Integer.parseInt(champStock.getText());

                if (choix == 0) { // Création Livre
                    String auteur = champVariable1.getText();
                    float taux = Float.parseFloat(champVariable2.getText());
                    biblio.AjouterDocument(new Livre(ref, titre, prix, stock, auteur, taux));
                } else { // Création Périodique
                    int num = Integer.parseInt(champVariable1.getText());
                    int annee = Integer.parseInt(champVariable2.getText());
                    Periodique p = new Periodique(ref, titre, prix, num, annee);
                    p.defNbExemplaire(stock);
                    biblio.AjouterDocument(p);
                }
                
                updateCallback.run(); // Mise à jour du tableau
                JOptionPane.showMessageDialog(parent, "Document ajouté !");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Erreur de saisie : " + e.getMessage());
            }
        }
    }

    // --- 2. AJOUTER LECTEUR (Choix Etudiant ou Enseignant) ---
    public static void ajouterLecteur(Component parent, Bibliotheque biblio, Runnable updateCallback) {
        String[] options = {"Étudiant", "Enseignant"};
        int choix = JOptionPane.showOptionDialog(parent, "Quel type de lecteur ?", "Nouveau Lecteur",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choix == -1) return;

        JTextField champNom = new JTextField();
        JTextField champEmail = new JTextField();
        JTextField champInstitut = new JTextField();
        JTextField champVariable = new JTextField(); // Adresse OU Téléphone

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nom :")); panel.add(champNom);
        panel.add(new JLabel("Email :")); panel.add(champEmail);
        panel.add(new JLabel("Institut :")); panel.add(champInstitut);

        if (choix == 0) { // ETUDIANT
            panel.add(new JLabel("Adresse :")); panel.add(champVariable);
        } else { // ENSEIGNANT
            panel.add(new JLabel("Téléphone :")); panel.add(champVariable);
        }

        int res = JOptionPane.showConfirmDialog(parent, panel, "Ajout " + options[choix], JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String nom = champNom.getText();
                String email = champEmail.getText();
                String inst = champInstitut.getText();
                String var = champVariable.getText();

                if (choix == 0) {
                    biblio.AjouterLecteur(new Etudiant(nom, email, inst, var));
                } else {
                    biblio.AjouterLecteur(new Enseignant(nom, email, inst, var));
                }
                
                updateCallback.run();
                JOptionPane.showMessageDialog(parent, "Lecteur ajouté !");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Erreur : " + e.getMessage());
            }
        }
    }

    // --- 3. REQUETE DE PRET ---
    public static void nouveauPret(Component parent, Bibliotheque biblio, Runnable updateCallback) {
        JTextField champEmail = new JTextField();
        JTextField champRef = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Email du Lecteur :")); panel.add(champEmail);
        panel.add(new JLabel("Référence du Document :")); panel.add(champRef);

        int res = JOptionPane.showConfirmDialog(parent, panel, "Nouveau Prêt", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            // Recherche des objets
            Lecteur l = trouverLecteur(biblio, champEmail.getText().trim());
            Document d = trouverDocument(biblio, champRef.getText().trim());

            if (l == null) {
                JOptionPane.showMessageDialog(parent, "Lecteur introuvable !");
                return;
            }
            if (d == null) {
                JOptionPane.showMessageDialog(parent, "Document introuvable !");
                return;
            }

            // Tentative de prêt
            boolean succes = biblio.requetePret(l, d);
            if (succes) {
                updateCallback.run();
                JOptionPane.showMessageDialog(parent, "Prêt validé pour " + l.getNom());
            } else {
                JOptionPane.showMessageDialog(parent, "Prêt REFUSÉ (Quota ou Stock)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helpers privés pour la recherche
    private static Lecteur trouverLecteur(Bibliotheque b, String email) {
        for (Lecteur l : b.obtenirToutLesLecteurs()) {
            if (l.getEmail().equalsIgnoreCase(email)) return l;
        }
        return null;
    }

    private static Document trouverDocument(Bibliotheque b, String ref) {
        for (Document d : b.obtenirToutLesDocuments()) {
            if (d.getReference().equalsIgnoreCase(ref)) return d;
        }
        return null;
    }
}