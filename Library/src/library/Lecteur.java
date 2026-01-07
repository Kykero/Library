package library;

public abstract class Lecteur {
    protected String Nom;
    protected String Email;
    protected String Institut;
    protected int MaxEmprunt;

    public Lecteur(String nom, String email, String institut, int maxEmprunt) {
        this.Nom = nom;
        this.Email = email;
        this.Institut = institut;
        this.MaxEmprunt = maxEmprunt;
    }

    //toString
    public String toString(){
        return this.Nom + " (" + this.Email + ")";
    }

    //Getteurs et Setters
    public String getNom() { return Nom; }
    public String getEmail() {return Email;}
    public int getMaxEmprunt() { return MaxEmprunt; }
    public void setMaxEmprunt(int Max) { this.MaxEmprunt = Max; }

    //Méthode Abstract
    public abstract int getDureePret();
    public abstract int setDureePret(int jours);
}
