/**
 * Classe de gestion centrale de la bibliothèque.
 * Elle stocke les listes en mémoire et applique les règles métier
 * (Vérification des quotas, calcul des stocks, gestion des retards).
 */

package gestion;

import java.time.LocalDate;
import java.util.*;

import modele.Document;
import modele.Lecteur;
import modele.Livre;
import modele.Pret;

public class Bibliotheque{ 
    
    // Inventaire de la bibliothèque
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
        if(lect.getDureePret() == 0){  // Vérifie que le lecteur possède aucun prêt sinon les oeuvres disparaît avec lui
            Lecteurs.remove(lect);
        }else {
            return "Le lecteur possède un livre";
        }
        return "Lecteur annhéanti";
    }
    
    //TODO : Check la fonction car elle me parait bizarre
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
    
public boolean prolongationPret(Lecteur lecteur, Document document) {

    // On parcourt la liste des prêts
    for (Pret p : this.Prets) { 
        boolean memeLecteur = p.getLecteur().getEmail().equals(lecteur.getEmail()); // On hard-code la comparaison avec le mail (j'ai eu des problèmes sinon)
        boolean memeDoc = p.getDocument().getReference().equals(document.getReference());

        if (memeLecteur && memeDoc) {

            if (p.isProlongation()) {
                System.out.println("REFUS : Ce prêt est déjà prolongé.");
                return false; // On refuse
            }

            p.setProlongation(true);
            System.out.println("SUCCÈS : Prêt prolongé pour " + document.getTitre());
            return true; // On dit à l'interface que c'est bon
        }
    }
    System.out.println("ERREUR : Prêt introuvable.");
    return false; // On dit à l'interface que ça a échoué
}
    
    public void declarationPerte(Lecteur lecteur, Document document) {
        double aPayer = 0;
        if (document instanceof Livre) { // Permet de déterminer la nature du document (très pratique la doc java quand même)
            Livre livrePerdu = (Livre) document; // Obligé de caster sinon on a aucun accès aux méthodes associés aux livres
            double majoration = livrePerdu.getPrix() * livrePerdu.getTauxRemboursement();
            aPayer = livrePerdu.getPrix() + majoration;
        } else {
            aPayer = document.getPrix(); 
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
        if (p.getDateRetourPrevue().isBefore(aujourdhui)) { // is Before est une méthode déjà implémenté par java.time
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


/* ---------------------------*/
/* Méthodes pour interface graphique  
/* ---------------------------*/

public void toutEffacer() {
    this.Documents.clear();
    this.Lecteurs.clear();
    this.Prets.clear();
    System.out.println("Mémoire de la bibliothèque vidée.");
}

}