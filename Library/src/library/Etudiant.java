package library;
public class Etudiant extends Lecteur {
    private String AdressePostale;
    private int DureePretEtu; // En jours
    public static final int MAX_EMPRUNT_DEFAUT = 1; 
    public static final int DUREE_PRET_DEFAUT = 14;

    public Etudiant(String nom, String email, String inst, int maxEmp, String adr, int duree) {
        super(nom, email, inst, maxEmp);
        this.AdressePostale = adr;
        this.DureePretEtu = duree;
    }

    public String toString() {return super.toString() + " [Etudiant]";}

    // Getteurs et Setters
    public int getDureePret() {return DureePretEtu;}
    public String getAdressePostale(){return AdressePostale;};

    public void setDureePret(int jours) { this.DureePretEtu = jours; }
}
