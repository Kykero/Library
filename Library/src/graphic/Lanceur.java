package graphic;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Lanceur {

    public static void main(String[] args) {
        // Appliquer le look moderne du système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        // Démarrer la fenêtre principale
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}
