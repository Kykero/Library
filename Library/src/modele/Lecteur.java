/**
 * Classe permettant de créer un lecteur
 * Elle est la classe mère de Enseignant et Etudiant
 */

package modele;

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
    public String getInstitut(){return Institut;}
    
    public void setMaxEmprunt(int Max) { this.MaxEmprunt = Max; }
    
    //Méthode Abstract (j'ai enfin compris !)
    public abstract int getDureePret(); 
    // On possède 2 types de Durée de Prêts, donc on abstract dans la classe "mère" pour pouvoir faire référence aux méthodes getdureePret des classes child
  
}
