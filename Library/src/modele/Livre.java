/**
 * Classe permettant de construire un livre
 * Un livre possède en plus un taux de remboursement
 */

package modele;
public class Livre extends Document {
    
    /*
    * Attributs
    */
   private String Nom_Auteur;
   private float Taux_Remboursement;

   /* Constructeur par défaut */
   public Livre(String ref, String titre, int prix, int nb, String auteur, float taux) {
        super(ref, titre, prix, nb);
        this.Nom_Auteur = auteur;
        this.Taux_Remboursement = taux;
    }

    //toString
    public String toString(){
        return this.titre + " (" + this.Nom_Auteur + ")";
    }


    /* Getteurs */
    public float getTauxRemboursement() { return Taux_Remboursement; }
    public String getNomAuteur() { return Nom_Auteur; }
}
