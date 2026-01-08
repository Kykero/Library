/**
 * Classe Document
 * Permet de définir/construire un document
 * C'est la classe mère des classes Enseignant et Etudiant
 */

package modele;
public class Document{

    // Attributs d'un document standard
    protected String reference;
    protected String titre;
    protected double prix;
    protected int nbExemplaire;

    /*
     * Constructeurs de la classe Document
    */
   public Document(String reference, String titre, double prix, int nbExemplaires) {
    this.reference = reference;
    this.titre = titre;
    this.prix = prix;
    this.nbExemplaire = nbExemplaires;
   }

   //toString
   public String toString(){
    return this.titre;
   }

   /*
    * Méthodes de gestion des documents
    */
   public boolean estDisponible(){return this.nbExemplaire > 0;}

   public void ajouterExemplaire(){this.nbExemplaire++;}

   public void retirerExemplaire(){this.nbExemplaire--;}

   // Getteurs et Setters
   public String getRefereString(){return reference;}
   public String getTitre(){return titre;}
   public double getPrix(){return prix;}
   public int getNbExemplaire(){return nbExemplaire;}
   public String getReference(){return reference;}

   public void defNbExemplaire(int nbExemplaire){this.nbExemplaire = nbExemplaire;}
}
