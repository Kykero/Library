/**
* Classe permettant de faire  des prêts et des prolongations
*/

package modele;

import java.time.*; // Permet de pouvoir calculer les durées de prêt et les dates de retour prévu

public class Pret {
    private Document Document;
    private Lecteur Lecteur;
    private LocalDate DatePret;
    private LocalDate DateRetourPrevue; 
    private boolean Prolongation;
    
    private int nbProlongations = 0;      
    private int joursSupplementaires = 0; 
    
    // Constructeur principal
    public Pret(Document document, Lecteur lecteur) {
        this.Document = document;
        this.Lecteur = lecteur;
        this.DatePret = LocalDate.now();
        this.DateRetourPrevue = this.DatePret.plusDays(Lecteur.getDureePret());
    }
    
    //toString || Je m'en sers pour débugger !
    public String toString(){
        return Document.getTitre() + " emprunté par " + Lecteur.getNom();
    }
    
    // Getteurs et Setters
    public Document getDocument() { return Document; }
    public Lecteur getLecteur() { return Lecteur; }
    public LocalDate getDateRetourPrevue() { return DateRetourPrevue; }
    public LocalDate getDatePret() {return DatePret;}
    public boolean isProlongation() { return Prolongation; }
    public void setDatePret(LocalDate nouvelleDate) {
        this.DatePret = nouvelleDate;
        this.DateRetourPrevue = this.DatePret.plusDays(Lecteur.getDureePret());
    }
    
    //Prolongation dynamique (non plus fixé par un booléan comme avant, sur consigne de la professeure)
    public int getNbProlongations() { return nbProlongations; }
    public void setNbProlongations(int n) { this.nbProlongations = n; }
    
    public int getJoursSupplementaires() { return joursSupplementaires; }
    public void setJoursSupplementaires(int j) { this.joursSupplementaires = j; }    
    public void ajouterProlongation(int jours) {
        this.nbProlongations++;
        this.joursSupplementaires += jours;
    }
}
