package library;

import java.util.*;

public class Bibliotheque{ 
    
    // Oeuvres présentes dans la bibliothèque
    private List<Document> Documents;
    private List<Lecteur> Lecteurs;
    private List<Pret> Prets;
    
    public Bibliotheque() {
        this.Documents = new ArrayList<>();
        this.Lecteurs = new ArrayList<>();
        this.Prets = new ArrayList<>();
    }
    // --- Méthodes ---
    
    /* --- Gestion des Documents --- */ 
    public void AjouterDocument(Document doc) {
        Documents.add(doc);
        System.out.println("Document ajouté à la Bibliothèque : " + doc.getTitre());
    }
    
    public void supprimerDocument(Document d) {
        Documents.remove(d);
    }
    
    /* --- Gestion des Lecteurs --- */
    
    public void AjouterLecteur(Lecteur lect) {
        Lecteurs.add(lect);
        System.out.println("Lecteur ajouté : " + lect.getNom());
    }
    
    public String SupprimerLecteur(Lecteur lect ){
        if(lect.getDureePret() == 0){  // Vérifie que le lecteur possède aucun prêt
            Lecteurs.remove(lect);
        }else {
            return "Le lecteur possède un livre";
        }
        return "Lecteur annhéanti";
    }
    
    /* --- Gestion des Prêts --- */
    
    public int getNbEmpruntsEnCours(Lecteur lecteur) {
        int compteur = 0;
        for (Pret p : this.Prets) {
            // Vérification du lecteur
            if (p.getLecteur().equals(lecteur)) {
                compteur++;
            }
        }
        return compteur;
    }
    
    public boolean requetePret(Lecteur lecteur, Document doc) {
        //Vérification du  nombre de prêt du lecteur
        long nbActuel = getNbEmpruntsEnCours(lecteur);
        if (nbActuel >= lecteur.getMaxEmprunt()) {
            System.out.println("Refus : Le lecteur a atteint son quota de " + lecteur.getMaxEmprunt() + " documents.");
            return false;
        }
        // Ajoute le prêt dans la list (sauvegarde)
        Pret nouveauPret = new Pret(doc, lecteur);
        Prets.add(nouveauPret);
        
        // Mets à jour le stock
        doc.defNbExemplaire(doc.getNbExemplaire() - 1);
        System.out.println("Prêt validé pour : " + doc.getTitre());
        return true;
    }
    
    public void retourPret(Lecteur lect, Document doc) {
        Pret pretATrouver = null;
        for (Pret p : this.Prets) {
            if (p.getLecteur().equals(lect) && p.getDocument().equals(doc)) {
                pretATrouver = p;
                break; // Permet d'arrêter la boucle (sans devoir flag boolean)
            }
        }
        
        if (pretATrouver != null) {
            this.Prets.remove(pretATrouver);
            doc.defNbExemplaire(doc.getNbExemplaire() + 1);
            System.out.println("RETOUR : Le document '" + doc.getTitre() + "' a été rendu.");
        } else {
            System.out.println("ERREUR : Aucun prêt trouvé pour ce lecteur et ce document.");
        }
    }
    
    public void prolongationPret(Lecteur lecteur, Document document, int duree) {
        // On cherche le prêt
        for (Pret p : this.Prets) {
            if (p.getLecteur().equals(lecteur) && p.getDocument().equals(document)) {
                // On active la prolongation
                p.setProlongation(true, duree);
                System.out.println("PROLONGATION : Prêt prolongé pour " + document.getTitre() + "de : " + duree + " jours");
                return; // Permet de stopper la boucle (Pas nécessaire mais évite de potentiels problèmes de runtime ou gc) 
            }
        }
        System.out.println("Impossible de prolonger : Prêt non trouvé.");
    }
    

}