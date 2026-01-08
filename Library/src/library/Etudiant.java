package library;

public class Etudiant extends Lecteur {
    private String AdressePostale;
    private int DureePretEtu; 

    // Imposé par le sujet un durée par défaut
    public static final int MAX_EMPRUNT_DEFAUT = 1; 
    public static final int DUREE_PRET_DEFAUT = 14;


    public Etudiant(String nom, String email, String inst, String adr) {
        super(nom, email, inst, MAX_EMPRUNT_DEFAUT);
        this.AdressePostale = adr;
        this.DureePretEtu = DUREE_PRET_DEFAUT;
    }

    // Second constructeur nécessaire pour charger des données déjà existantes
    public Etudiant(String nom, String email, String inst, int maxEmp, String adr, int duree) {
        super(nom, email, inst, maxEmp); // On passe le vrai max lu dans le fichier
        this.AdressePostale = adr;
        this.DureePretEtu = duree;       // On passe la vraie durée lue dans le fichier
    }

    public String toString() { return super.toString() + " [Etudiant]"; }
    public int getDureePret() { return DureePretEtu; }
    
    public void setDureePret(int jours) { this.DureePretEtu = jours; }
    public String getAdressePostale(){ return AdressePostale; }
}