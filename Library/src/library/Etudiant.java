package library;

public class Etudiant extends Lecteur {
    private String AdressePostale;
    private int DureePretEtu; 

    // Imposé par le sujet une durée par défaut
    public static final int MAX_EMPRUNT_DEFAUT = 1; 
    public static final int DUREE_PRET_DEFAUT = 14;


    public Etudiant(String nom, String email, String inst, String adr) {
        super(nom, email, inst, MAX_EMPRUNT_DEFAUT);
        this.AdressePostale = adr;
        this.DureePretEtu = DUREE_PRET_DEFAUT;
    }

    // Second constructeur nécessaire pour charger des données déjà existantes
    public Etudiant(String nom, String email, String inst, int maxEmp, String adr, int duree) {
        super(nom, email, inst, maxEmp); 
        this.AdressePostale = adr;
        this.DureePretEtu = duree;
    }

    //toString
    public String toString() { return super.toString() + " [Etudiant]"; }

    //Getteurs et Setters
    public int getDureePret() { return DureePretEtu; }
    public void setDureePret(int jours) { this.DureePretEtu = jours; }
    public String getAdressePostale(){ return AdressePostale; }
}