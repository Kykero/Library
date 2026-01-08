package library;
public class Etudiant extends Lecteur {
    private String AdressePostale;
    private int DureePretEtu; // En jours
    public static final int MAX_EMPRUNT_DEFAUT = 1; 
    public static final int DUREE_PRET_DEFAUT = 14;

    public Etudiant(String nom, String email, String inst, String adr) {
        super(nom, email, inst, MAX_EMPRUNT_DEFAUT);
        this.AdressePostale = adr;
        this.DureePretEtu = DUREE_PRET_DEFAUT;
    }

    public String toString() {return super.toString() + " [Etudiant]";}

    // Getteurs et Setters
    public int getDureePret() {return DureePretEtu;}
    public String getAdressePostale(){return AdressePostale;};

    
}
