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
        //chargerLecteurs(biblio);
        //chargerPrets(biblio);
        System.out.println("Chargement terminé.");
    }

    /* 
    * La méthode repose principalement sur du Regex et Parsing
    */
    private void chargerDocuments(Bibliotheque biblio) {
        File fichier = new File(FICHIER_DOCS);
        if (!fichier.exists()) return; 

        // Lecture du fichier Documents 
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
    
    
}