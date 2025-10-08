package ca.qc.bdeb.sim.tp1photorama;

import java.io.File;
import java.io.IOException;

/**
 * Classe de test en ligne de commande pour l'application Photorama.
 * Permet de tester les différents comparateurs d'images.
 */
public class MainCmd {

    /**
     * Méthode principale exécutée en ligne de commande.
     * Effectue plusieurs tests :
     * 1. Comparaison de pixels entre images
     * 2. Affichage des hachages pour tests visuels
     * 3. Analyse de performances des comparateurs sur un vrai dossier d'images
     */
    public static void main(String[] args) throws IOException {
        final var cheminDossier = "airbnb-petit";

        // Instanciation des différents comparateurs
        var compPixel = new ComparateurImagesPixels();
        var compMoyenne = new ComparateurImagesHachageMoyenne();
        var compDifference = new ComparateurImagesHachageDifference();

        // === 1. Tests de comparaison par pixels avec différents seuils et pourcentages ===
        Object[][] debugPixel = {
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels2.png"},
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels3.png"},
                {5, 0.4, "debogage/pixels1.png", "debogage/pixels4.png"},
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.49, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.50, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.51, "debogage/pixels1.png", "debogage/pixels4.png"},
        };

        System.out.println("=== 1. Test des différences de pixels ===");
        for (Object[] o : debugPixel) {
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1] * 100;
            String img1 = (String) o[2];
            String img2 = (String) o[3];

            compPixel.setSeuilDifference(seuil);
            compPixel.setMaxPourcentage(maxPourcentage);

            String resultat = compPixel.imagesSimilaires(img1, img2) ? "SIMILAIRES" : "DIFFERENTS";
            System.out.printf("seuil=%d, max%%=%.2f, %s vs %s => %s%n", seuil, maxPourcentage / 100, img1, img2, resultat);
        }

        // === 2. Affichage des hachages générés par les algorithmes ===
        System.out.println("=== 2. Affichage des valeurs de hachage ===");
        String[] debugHachageMoyenne = {"debogage/moyenne-test1.png", "debogage/moyenne-test2.png", "debogage/moyenne-test3.png"};
        String[] debugHachageDifferences = {"debogage/diff-test1.png", "debogage/diff-test2.png", "debogage/diff-test3.png"};

        afficherHachages(debugHachageMoyenne, compMoyenne);
        afficherHachages(debugHachageDifferences, compDifference);

        // === 3. Analyse des performances sur le dossier airbnb-petit avec différents paramètres ===
        System.out.println("=== 3. Analyse de la qualité des différents algorithmes pour airbnb-petit ===");

        // Tests pour le comparateur par pixels avec différents paramètres
        Object[][] tabComp = {
                {8, 0.2, new ComparateurImagesPixels()},
                {20, 0.5, new ComparateurImagesPixels()},
        };

        for (Object[] o : tabComp) {
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1] * 100;
            ComparateurImagesPixels comp = (ComparateurImagesPixels) o[2];

            comp.setSeuilDifference(seuil);
            comp.setMaxPourcentage(maxPourcentage);

            Gallerie gallerie = new Gallerie(cheminDossier, comp);
            System.out.printf("Algo: Comparateur Pixels (seuil=%d, max%%=%.2f)%n", seuil, maxPourcentage / 100);
            testGallerieAirbnbPetitHachage(gallerie);
        }

        // Tests pour les comparateurs par hachage
        Object[][] tabHachage = {
                {8, new ComparateurImagesHachageMoyenne()},
                {16, new ComparateurImagesHachageMoyenne()},
                {8, new ComparateurImagesHachageDifference()},
                {16, new ComparateurImagesHachageDifference()},
        };

        for (Object[] o : tabHachage) {
            int maxCases = (int) o[0];
            ComparateurImages comp = (ComparateurImages) o[1];
            comp.setMaxCases(maxCases);

            Gallerie gallerie = new Gallerie(cheminDossier, comp);

            if (comp instanceof ComparateurImagesHachageMoyenne) {
                System.out.printf("Algo: Comparateur Hachage Moyenne (cases diff max=%d)%n", maxCases);
            } else if (comp instanceof ComparateurImagesHachageDifference) {
                System.out.printf("Algo: Comparateur Hachage Différences (cases diff max=%d)%n", maxCases);
            }

            testGallerieAirbnbPetitHachage(gallerie);
        }
    }

    /**
     * Affiche les groupes de doublons détectés dans une galerie d’images.
     * @param gallerie galerie d’images à analyser
     */
    private static void testGallerieAirbnbPetitHachage(Gallerie gallerie) {
        for (int i = 0; i < gallerie.getGroupesImages().size(); i++) {
            System.out.print("[" + (i + 1) + "] ");
            for (int j = 0; j < gallerie.getGroupesImages().get(i).size(); j++) {
                String img = new File(gallerie.getGroupesImages().get(i).get(j)).getName();
                System.out.print(img + " ");
            }
            System.out.println();
        }
    }

    /**
     * Affiche le tableau de hachage (0/1) pour chaque image donnée.
     * @param cheminsImages chemins vers les images à analyser
     * @param hachageur l’algorithme de hachage à utiliser
     * @throws IOException si une image est introuvable
     */
    public static void afficherHachages(String[] cheminsImages, ComparateurImages hachageur) throws IOException {
        for (String chemin : cheminsImages) {
            System.out.println(chemin + ":");
            int[][] tab = hachageur.calculTabHachageUnImage(chemin);

            for (int[] ligne : tab) {
                for (int val : ligne) {
                    System.out.print(val == 0 ? " " : "1");
                }
                System.out.println();
            }
        }
    }
}
