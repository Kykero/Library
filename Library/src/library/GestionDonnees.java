package library;

import java.io.*;
import java.time.*;
import java.util.*;

public class GestionDonnees {
    
    // Fichiers à gérer
    private static final String FICHIER_DOCS = "Documents.txt";
    private static final String FICHIER_LECTEURS = "Lecteurs.txt";
    private static final String FICHIER_PRETS = "Prets.txt";
    
    // =================================================================
    // Chargement des fichiers
    // =================================================================

    public void charger(Bibliotheque biblio) {
        System.out.println("Chargement des données...");
        chargerDocuments(biblio);
        chargerLecteurs(biblio);
        //chargerPrets(biblio);
        System.out.println("Chargement terminé.");
    }

    /* 
    * La méthode repose principalement sur du Regex et Parsing
    * Ordre du document :
    * type, ref, titre, prix, nb, auteur/numero, taux/annee
    */
    private void chargerDocuments(Bibliotheque biblio) {
        File fichier = new File(FICHIER_DOCS);
        if (!fichier.exists()) return; 

        // Lecture du fichier Documents (la doc recommende un try exception)
        try {
            Scanner scanner = new Scanner(fichier);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] infos = ligne.split(";"); // Regex : on sépare les lignes avec les ;

                String type = infos[0];
                String ref = infos[1];
                String titre = infos[2];
                int prix = Integer.parseInt(infos[3]); //parseInt permet juste de convertir le string en int
                int nb = Integer.parseInt(infos[4]);

                if (type.equals("LIVRE")) {
                    String auteur = infos[5];
                    float taux = Float.parseFloat(infos[6]);
                    Livre l = new Livre(ref, titre, prix, nb, auteur, taux);
                    biblio.AjouterDocument(l);
                } 
                else if (type.equals("PERIODIQUE")) {
                    int numero = Integer.parseInt(infos[5]);
                    int annee = Integer.parseInt(infos[6]);
                    Periodique p = new Periodique(ref, titre, prix, numero, annee);
                    // On force le nombre d'exemplaires car le constructeur met 1 par défaut
                    p.defNbExemplaire(nb); 
                    biblio.AjouterDocument(p);
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erreur lecture documents : " + e.getMessage());
        }
    }

    /* 
    * La méthode repose également sur du Regex et Parsing (même méthode que doc)
    * Ordre du document :
    * type, nom, email, institut, adresse/tel, durée
    */
    private void chargerLecteurs(Bibliotheque biblio) {
        File fichier = new File(FICHIER_LECTEURS);
        if (!fichier.exists()) return;

        try {
            Scanner scanner = new Scanner(fichier);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] infos = ligne.split(";");

                String type = infos[0];
                String nom = infos[1];
                String email = infos[2];
                String institut = infos[3];
                int max = Integer.parseInt(infos[4]);

                if (type.equals("ETUDIANT")) {
                    String adresse = infos[5];
                    int duree = Integer.parseInt(infos[6]);
                    Etudiant e = new Etudiant(nom, email, institut, max, adresse, duree);
                    biblio.AjouterLecteur(e);
                } 
                else if (type.equals("ENSEIGNANT")) {
                    String tel = infos[5];
                    int duree = Integer.parseInt(infos[6]);
                    Enseignant ens = new Enseignant(nom, email, institut, max, tel, duree);
                    biblio.AjouterLecteur(ens);
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erreur lecture lecteurs : " + e.getMessage());
        }
    }



    
    
}