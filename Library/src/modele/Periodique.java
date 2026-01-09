/**
 * Classe permettant de construire un périodique
 * Un périodique possède un numéro et une année de parution
 */

package modele;

public class Periodique extends Document {
    private int Numero;
    private int Annee_Parution;

    public Periodique(String ref, String titre, int prix, int numero, int annee) {

        // Les Periodique sont uniques en exemplaire !
        super(ref, titre, prix, 1);
        this.Numero = numero;
        this.Annee_Parution = annee;
    }

    //toString
    public String toString(){
        return this.titre + " - N°" + this.Numero;
    }

    // Getteurs et setters
    public int getNumero() {return Numero;}
    public int getAnneeParution() {return Annee_Parution;}

    public void setNumero(int numero) {this.Numero = numero;}
    public void setAnneeParution(int annee) {this.Annee_Parution = annee;}

}

