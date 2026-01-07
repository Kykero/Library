public interface Interface_Library {

    /*
     * Gestion des Lecteurs (Etudiant et Professeurs)
    */
    void ajouterLecteur(Lecteur Lecteur);
    void supprimerLecteur(String email);
    void modifierParametresLecteur(int newMax, int newTimeStudent, int newTimeTeacher);
    
    /*  
     * Gestion des Documents (Livres et Périodiques)
    */
   void ajouterDocument(Document doc);
   void supprimerDocument(String reference);

   /*
    * Méthodes pour les prêts
   */
  boolean effectuerPret(String refDocument, String emailLecteur);
  void effectuerRetour(String refDoc, String emailLecteur);
  void prolongerPret(String refDocument, String emailLecteur);
  double declarerPerte(String refDocument, String emailLecteur);

  /*
   * Obtenir informations (ref UML)
  */
 List<Document> ListDocuments();
 List<Lecteur> ListLecteurs();
 List<Lecteur> LecteursEnRetard();

 
}
