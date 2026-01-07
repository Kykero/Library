package library;

import java.time.*; // Permet de pouvoir calculer les durées de prêt et les dates de retour prévu

public class Pret {
    private Document Document;
    private Lecteur Lecteur;
    private LocalDate DatePret;
    private LocalDate DateRetourPrevue; 
    private boolean Prolongation;
    private final static int JourProlongation = 7;
    
    public Pret(Document document, Lecteur lecteur) {
        this.Document = document;
        this.Lecteur = lecteur;
        this.DatePret = LocalDate.now();
        // Calcul date retour basé sur le type de lecteur
        this.DateRetourPrevue = this.DatePret.plusDays(Lecteur.getDureePret());
        this.Prolongation = false;
    }
    
    // Méthode pour prolonger le prêt, on ne mets pas de jours maximum
    public void setProlongation(boolean b) {
        b = true;
        this.Prolongation = b;
        if(b) {
            this.DateRetourPrevue = this.DateRetourPrevue.plusDays(JourProlongation); // On fixe le délai de prolongation
        }
    }
    
    public void setDatePret(LocalDate nouvelleDate) {
        this.DatePret = nouvelleDate;
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
}
