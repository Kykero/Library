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
    // Sauvegarde des fichiers
    // =================================================================
    
    // Sauvegarde générale
    public void sauvegarder(Bibliotheque biblio) {
        System.out.println("Début de la sauvegarde...");
        
        sauvegarderDocuments(biblio);
        sauvegarderLecteurs(biblio);
        sauvegarderPrets(biblio);
        
        System.out.println("Sauvegarde terminée !");
    }
    
    // Writer permet d'écrire dans les fichier textes
    private void sauvegarderDocuments(Bibliotheque biblio) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_DOCS));
            for (Document doc : biblio.obtenirToutLesDocuments()) {
                String ligne = "";
                
                if (doc instanceof Livre) {
                    Livre l = (Livre) doc;
                    // Format : TYPE;REF;TITRE;PRIX;NB;AUTEUR;TAUX
                    ligne = "LIVRE;" + l.getReference() + ";" + l.getTitre() + ";" + l.getPrix() + ";" 
                    + l.getNbExemplaire() + ";" + l.getNomAuteur() + ";" + l.getTauxRemboursement();
                } 
                else if (doc instanceof Periodique) {
                    Periodique p = (Periodique) doc;
                    // Format : TYPE;REF;TITRE;PRIX;NB;NUMERO;ANNEE
                    ligne = "PERIODIQUE;" + p.getReference() + ";" + p.getTitre() + ";" + p.getPrix() + ";" 
                    + p.getNbExemplaire() + ";" + p.getNumero() + ";" + p.getAnneeParution();
                }
                writer.println(ligne);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde documents : " + e.getMessage());
        }
    }
    
    private void sauvegarderLecteurs(Bibliotheque biblio) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FICHIER_LECTEURS));
            for (Lecteur lect : biblio.obtenirToutLesLecteurs()) {
                String ligne = "";
                
                if (lect instanceof Etudiant) {
                    Etudiant e = (Etudiant) lect;
                    // Format : TYPE;NOM;EMAIL;INSTITUT;MAX;ADRESSE;DUREE
                    ligne = "ETUDIANT;" + e.getNom() + ";" + e.getEmail() + ";" + e.getInstitut() + ";" 
                    + e.getMaxEmprunt() + ";" + e.getAdressePostale() + ";" + e.getDureePret();
                } 
                else if (lect instanceof Enseignant) {
                    Enseignant ens = (Enseignant) lect;
                    // Format : TYPE;NOM;EMAIL;INSTITUT;MAX;TEL;DUREE
                    ligne = "ENSEIGNANT;" + ens.getNom() + ";" + ens.getEmail() + ";" + ens.getInstitut() + ";" 
                    + ens.getMaxEmprunt() + ";" + ens.getTelephone() + ";" + ens.getDureePret();
                }
                
                writer.println(ligne);
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur sauvegarde lecteurs : " + e.getMessage());
        }
    }
    
    
    private void sauvegarderPrets(Bibliotheque biblio) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("prets.txt"));
            
            for (Pret p : biblio.getToutLesPrets()) {
                // Ligne importante : p.getDatePret()
                // Java va écrire "2026-01-07" automatiquement grâce au toString() implicite
                String ligne = p.getLecteur().getEmail() + ";" + 
                p.getDocument().getReference() + ";" +
                p.getDatePret() + ";" + 
                p.isProlongation();
                
                writer.println(ligne);
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
    
    
    
    // =================================================================
    // Chargement des fichiers
    // =================================================================
    
    public void charger(Bibliotheque biblio) {
        System.out.println("Chargement des données...");
        chargerDocuments(biblio);
        chargerLecteurs(biblio);
        chargerPrets(biblio);
        System.out.println("Chargement terminé.");
    }
    
    /* 
    * La méthode repose principalement sur du Regex et Parsing
    * Ordre du document :
    * type, ref, titre, prix, nb, auteur/numero, taux/annee
    */
    private void chargerDocuments(Bibliotheque biblio) {
        File fichier = new File(FICHIER_DOCS);
        
        // DEBUG : Affiche le chemin absolu pour savoir où le fichier doit être
        System.out.println("Lecture du fichier : " + fichier.getAbsolutePath());
        
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
                int prix = (int) Float.parseFloat(infos[3]); //Obliger de caster pour les prix à virgules
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
    * Ordre du fichier texte :
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
    
    
    /* 
    * La méthode est plus complexe car on doit relier également les lecteurs aux prêts
    *  Pour faciliter la lecture on implémente d'autres méthodes plus bas
    */
    private void chargerPrets(Bibliotheque biblio) {
        File fichier = new File(FICHIER_PRETS);
        if (!fichier.exists()) return;
        
        try {
            Scanner scanner = new Scanner(fichier);
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String[] infos = ligne.split(";");
                
                String emailLecteur = infos[0];
                String refDoc = infos[1];
                String dateStr = infos[2];
                boolean isProlonge = Boolean.parseBoolean(infos[3]);
                
                //Recherche et lien avec les documents et lecteur
                Lecteur lect = trouverLecteur(biblio, emailLecteur);
                Document doc = trouverDocument(biblio, refDoc);
                
                if (lect != null && doc != null) {
                    
                    Pret p = new Pret(doc, lect);// Si on utilise requetePret() on risque de décrementer 2 fois
                    p.setDatePret(LocalDate.parse(dateStr));                    
                    p.setProlongation(isProlonge);
                    biblio.getToutLesPrets().add(p);
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erreur lecture prêts : " + e.getMessage());
        }
    }
    
    // =================================================================
    // Méthodes pour aider à charger les prêts
    // =================================================================
    
    private Lecteur trouverLecteur(Bibliotheque biblio, String email) {
        for (Lecteur lect : biblio.obtenirToutLesLecteurs()) {
            if (lect.getEmail().equals(email)) {
                return lect;
            }
        }
        return null;
    }
    
    private Document trouverDocument(Bibliotheque biblio, String ref) {
        for (Document doc : biblio.obtenirToutLesDocuments()) {
            if (doc.getReference().equals(ref)) {
                return doc;
            }
        }
        return null;
    }
    
}