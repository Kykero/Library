package library;

public class Lecteur {
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

    //Getteurs et Setters
    public String getNom() { return Nom; }
    public int getMaxEmprunt() { return MaxEmprunt; }
    public void setMaxEmprunt(int Max) { this.MaxEmprunt = Max; }

}
