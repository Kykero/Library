package library;

public class Enseignant extends Lecteur {
    private String Telephone;
    private int DureePretEns; 
    
    public static final int MAX_EMPRUNT_DEFAUT = 1;
    public static final int DUREE_PRET_DEFAUT = 20; 
    
    // Constructeur par défaut
    public Enseignant(String nom, String email, String inst, String tel) {
        super(nom, email, inst, MAX_EMPRUNT_DEFAUT);
        this.Telephone = tel;
        this.DureePretEns = DUREE_PRET_DEFAUT;
    }

    // -Second constructeur nécessaire quand on charge les données
    public Enseignant(String nom, String email, String inst, int maxEmp, String tel, int duree) {
        super(nom, email, inst, maxEmp); 
        this.Telephone = tel;
        this.DureePretEns = duree;   
    }
    
    public String toString() { return super.toString() + " [Enseignant]"; }
    public int getDureePret() { return DureePretEns; }
    public void setDureePret(int jours) { this.DureePretEns = jours; }
    public String getTelephone() { return Telephone; }
}