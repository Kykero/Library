package library;

public class Periodique extends Document {
    private int Numero;
    private int Annee_Parution;

    public Periodique(String ref, String titre, int prix, int numero, int annee) {

        // Les Periodique sont uniques en exemplaire !
        super(ref, titre, prix, 1);
        this.Numero = numero;
        this.Annee_Parution = annee;
    }

    // Getteurs
    public int getNumero() {return Numero;}
    public int getAnneeParution() {return Annee_Parution;}

}

