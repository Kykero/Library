package library;

public class MainApp {

    public static void main(String[] args) {
        // 1. Initialisation
        System.out.println("=============================================");
        System.out.println("    DEMARRAGE DU SYSTEME DE BIBLIOTHEQUE     ");
        System.out.println("=============================================");
        
        Bibliotheque maBiblio = new Bibliotheque();
        GestionDonnees gestion = new GestionDonnees();

        // 2. Chargement des données (Documents, Lecteurs, Prêts existants)
        System.out.println("\n--- 1. CHARGEMENT DES FICHIERS ---");
        gestion.charger(maBiblio);
        
        // Vérification visuelle
        System.out.println("> Documents chargés : " + maBiblio.obtenirToutLesDocuments().size());
        System.out.println("> Lecteurs chargés  : " + maBiblio.obtenirToutLesLecteurs().size());
        System.out.println("> Prêts en cours    : " + maBiblio.getToutLesPrets().size());

        // 3. Affichage du contenu (Test des toString)
        System.out.println("\n--- 2. LISTE DES DOCUMENTS (Extrait) ---");
        for (int i = 0; i <  maBiblio.obtenirToutLesDocuments().size(); i++) {
            System.out.println(" - " + maBiblio.obtenirToutLesDocuments().get(i));
        }

        // =========================================================
        // SCÉNARIOS DE TEST 
        // =========================================================

        // On récupère des objets précis pour tester (Alice et Harry Potter)
        Lecteur alice = trouverLecteur(maBiblio, "alice.dupont@univ.fr");
        Lecteur bob = trouverLecteur(maBiblio, "bob.martin@etud.fr"); // Lui a un quota de 1
        Document harryPotter = trouverDocument(maBiblio, "LIV_001");
        Document cleanCode = trouverDocument(maBiblio, "LIV_005");

        if (alice == null || harryPotter == null) {
            System.err.println("ERREUR CRITIQUE : Données de test introuvables. Vérifiez les fichiers txt.");
            return;
        }

        System.out.println("\n--- 3. TEST : EMPRUNT (Succès) ---");
        // Alice veut emprunter Harry Potter (LIV_001)
        // Le stock devrait baisser
        System.out.println("Stock avant : " + harryPotter.getNbExemplaire());
        maBiblio.requetePret(alice, harryPotter);
        System.out.println("Stock après : " + harryPotter.getNbExemplaire());

        System.out.println("\n--- 4. TEST : EMPRUNT (Echec Quota) ---");
        // Bob a déjà 1 livre dans prets.txt (LIV_003). Son max est 1.
        // Il essaie d'en prendre un 2ème.
        maBiblio.requetePret(bob, cleanCode); // Doit afficher un REFUS

        System.out.println("\n--- 5. TEST : PROLONGATION ---");
        // On prolonge le prêt qu'on vient de faire pour Alice
        maBiblio.prolongationPret(alice, harryPotter);

        System.out.println("\n--- 6. TEST : RETOUR ---");
        // Alice rend Harry Potter
        maBiblio.retourPret(alice, harryPotter);
        System.out.println("Stock après retour : " + harryPotter.getNbExemplaire());

        System.out.println("\n--- 7. TEST : DÉCLARATION DE PERTE ---");
        // Imaginons que Bob a perdu son livre (LIV_003 emprunté dans le fichier)
        Document livrePerdu = trouverDocument(maBiblio, "LIV_003");
        if (livrePerdu != null) {
            // Doit afficher le prix + majoration
            maBiblio.declarationPerte(bob, livrePerdu);
        }

        System.out.println("\n--- 8. TEST : MODIFICATION LECTEUR ---");
        // On change les droits de Bob pour qu'il puisse emprunter plus
        System.out.println("Max emprunt Bob avant : " + bob.getMaxEmprunt());
        maBiblio.modificationLecteur(bob, 5, 21);
        System.out.println("Max emprunt Bob après : " + bob.getMaxEmprunt());


        // =========================================================
        // GESTION DES RETARDS
        // =========================================================
        System.out.println("\n--- 9. ALERTES RETARDS ---");
        // Doit afficher Marie Curie (qui a emprunté en 2023 dans le fichier)
        maBiblio.notificationLecteur();

        
        // =========================================================
        // SAUVEGARDE FINALE
        // =========================================================
        System.out.println("\n--- 10. SAUVEGARDE ---");
        gestion.sauvegarder(maBiblio);
        System.out.println("Test terminé. Vérifiez les fichiers txt pour voir les changements.");
    }

    // --- Méthodes utilitaires pour le Main (Simule le choix dans l'interface) ---

    private static Lecteur trouverLecteur(Bibliotheque biblio, String email) {
        for (Lecteur l : biblio.obtenirToutLesLecteurs()) {
            if (l.getEmail().equals(email)) return l;
        }
        return null;
    }

    private static Document trouverDocument(Bibliotheque biblio, String ref) {
        for (Document d : biblio.obtenirToutLesDocuments()) {
            if (d.getReference().equals(ref)) return d;
        }
        return null;
    }
}