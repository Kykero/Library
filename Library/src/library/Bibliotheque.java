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
        if(lect.getDureePret() == 0){
            Lecteurs.remove(lect);
        }else {
            return "Le lecteur possède un livre";
        }
        return "Lecteur annhéanti";
    }

}