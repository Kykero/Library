package library;
public class Etudiant extends Lecteur {
    private String AdressePostale;
    private int DureePretEtu; // En jours

    public Etudiant(String nom, String email, String inst, int maxEmp, String adr, int duree) {
        super(nom, email, inst, maxEmp);
        this.AdressePostale = adr;
        this.DureePretEtu = duree;
    }

    // Getteurs et Setters
    public int getDureePret() {return DureePretEtu;}
    public void setDureePretEtu(int d) { this.DureePretEtu = d; }
}
