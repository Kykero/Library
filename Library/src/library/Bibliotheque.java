package library;

import java.time.LocalDate;
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
    
    /* ---------------------------*/
    /* --- Gestion des Documents --- */ 
    /* ---------------------------*/
    
    public void AjouterDocument(Document doc) {
        Documents.add(doc);
        System.out.println("Document ajouté à la Bibliothèque : " + doc.getTitre());
    }
    
    public void supprimerDocument(Document d) {
        Documents.remove(d);
    }
    
    /* ---------------------------*/
    /* --- Gestion des Lecteurs --- */
    /* ---------------------------*/
    
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
    
    public void modificationLecteur(Lecteur lect, int nouveauMaxEmprunt, int nouvelleDuree) {
        lect.setMaxEmprunt(nouveauMaxEmprunt);
        System.out.println("Paramètres modifiés pour : " + lect.getNom());
    }
    
    
    /* ---------------------------*/
    /* --- Gestion des Prêts --- */
    /* ---------------------------*/
    
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
    
    public void prolongationPret(Lecteur lecteur, Document document) {
        // On cherche le prêt
        for (Pret p : this.Prets) {
            if (p.getLecteur().equals(lecteur) && p.getDocument().equals(document)) {
                // On active la prolongation
                p.setProlongation(true);
                System.out.println("PROLONGATION : Prêt prolongé pour " + document.getTitre() + " de : " + 7 + "jours");
                return; // Permet de stopper la boucle (Pas nécessaire mais évite de potentiels problèmes de runtime ou gc) 
            }
        }
        System.out.println("Impossible de prolonger : Prêt non trouvé.");
    }
    
    public void declarationPerte(Lecteur lecteur, Document document) {
        double aPayer = 0;
        if (document instanceof Livre) { // Permet de déterminer la nature du document
            Livre livrePerdu = (Livre) document; // Cast pour accéder aux méthodes de Livre
            double majoration = livrePerdu.getPrix() * livrePerdu.getTauxRemboursement();
            aPayer = livrePerdu.getPrix() + majoration;
        } else {
            aPayer = document.getPrix(); // Prix d'un périodique
        }
        System.out.println("Perte d'un document : " + lecteur.getNom() + " doit payer " + aPayer + " euros.");
        
        // Mets à jour le stock
        Pret pretASupprimer = null;
        for (Pret p : this.Prets) {
            if (p.getLecteur().equals(lecteur) && p.getDocument().equals(document)) {
                pretASupprimer = p;
                break;
            }
        }
        if (pretASupprimer != null) { // Pour ne pas retirer un null ça n'a pas de sens
        this.Prets.remove(pretASupprimer);
    }
}

public void notificationLecteur() {
    LocalDate aujourdhui = LocalDate.now();
    System.out.println("--- ! LISTE DES RETARDS ! ---");
    boolean retardTrouve = false;
    
    for (Pret p : this.Prets) {
        if (p.getDateRetourPrevue().isBefore(aujourdhui)) { // is Before est une méthode de java time
            System.out.println("- ! RETARD : " + p.getLecteur().getNom() + " aurait dû rendre '" 
            + p.getDocument().getTitre() + "' le " + p.getDateRetourPrevue());
            retardTrouve = true;
        }
    }
    if (!retardTrouve) {
        System.out.println("Aucun retard à signaler.");
    }
}


/* ---------------------------*/
/* --- Utilitaires (debug notamment) --- */ 
/* ---------------------------*/

public List<Pret> getToutLesPrets() {return this.Prets;}

public List<Document> obtenirToutLesDocuments() {return this.Documents;}

public List<Lecteur> obtenirToutLesLecteurs() {return this.Lecteurs;}

public int getNbEmpruntsEnCours(Lecteur lecteur) {
    int compteur = 0;
    for (Pret p : this.Prets) {
        String emailPret = p.getLecteur().getEmail();
        String emailLecteurActuel = lecteur.getEmail();
        if (emailPret.equalsIgnoreCase(emailLecteurActuel)) {
            compteur++;
        }
    }
    return compteur;
}




}