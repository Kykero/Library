package library;
public class Enseignant extends Lecteur {
    
    private String Telephone;
    private int DureePretEns; // En jours
    
    public static final int MAX_EMPRUNT_DEFAUT = 1;
    public static final int DUREE_PRET_DEFAUT = 20; 
    
    public Enseignant(String nom, String email, String inst, int maxEmp, String tel, int duree) {
        super(nom, email, inst, maxEmp);
        this.Telephone = tel;
        this.DureePretEns = duree;
    }
    
    //toString
    public String toString() {return super.toString() + " [Enseignant]";}
    
    // Getteurs et Setters
    public int getDureePret() {return DureePretEns;}
    public String getTelephone() { return Telephone; }

    public void setDureePret(int jours) {this.DureePretEns = jours;}
    
}