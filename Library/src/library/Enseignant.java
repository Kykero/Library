package library;
public class Enseignant extends Lecteur {
    private String Telephone;
    private int DureePretEns; // En jours

    public Enseignant(String nom, String email, String inst, int maxEmp, String tel, int duree) {
        super(nom, email, inst, maxEmp);
        this.Telephone = tel;
        this.DureePretEns = duree;
    }

    // Getteurs
    public int getDureePret() {
        return DureePretEns;}
}