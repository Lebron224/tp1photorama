package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

/**
 * Comparateur d'images utilisant l'algorithme de hachage par moyenne.
 * Redimensionne chaque image en 8x8 pixels, convertit en niveaux de gris,
 * puis compare les hachages binaires.
 */
public class ComparateurImagesHachageMoyenne extends ComparateurImages {
    /**
     * Compare deux images à l'aide de leur hachage moyen.
     *
     * @param chemin1 Chemin vers la première image
     * @param chemin2 Chemin vers la deuxième image
     * @return true si les images sont similaires
     * @throws IOException si la lecture des images échoue
     */
    @Override
    public boolean imagesSimilaires(String chemin1, String chemin2) throws IOException {
        // Étape 1 : Lecture et redimensionnement des images en 8x8 pixels
        int[][] image1 = GestionnaireImages.toPixels(
                GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin1), 8, 8));
        int[][] image2 = GestionnaireImages.toPixels(
                GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin2), 8, 8));

        // Étape 2 : Calcul de la luminance moyenne de chaque image
        double moyenne1 = calculMoyenne(image1);
        double moyenne2 = calculMoyenne(image2);

        // Étape 3 : Génération du hachage (0 ou 1) pour chaque pixel
        int[][] hachage1 = calculTabHachage(image1, moyenne1);
        int[][] hachage2 = calculTabHachage(image2, moyenne2);

        // Étape 4 : Comparaison des deux tableaux pour determiner le nombre de bit différents
        int differences = 0;
        for (int i = 0; i < hachage1.length; i++) {
            for (int j = 0; j < hachage1[0].length; j++) {
                if (hachage1[i][j] != hachage2[i][j]) {
                    differences++;
                }
            }
        }

        // Étape 5 : Vérification selon le seuil de tolérance
        return differences <= getMaxCases();
    }

    /**
     * Calcule la moyenne de luminance (niveau de gris) d'une image.
     *
     * @param tab Tableaux de pixels en niveaux de gris
     * @return Moyenne arithmétique des valeurs
     */
    public double calculMoyenne(int[][] tab) {
        int somme = 0;
        for (int[] ligne : tab) {
            for (int pixel : ligne) {
                somme += pixel;
            }
        }
        return (double) somme / (tab.length * tab[0].length);
    }

    /**
     * Génère le tableau de hachage à partir d'une image et de sa moyenne luminance.
     *
     * @param tab     Matrice de pixels
     * @param moyenne Moyenne de luminance
     * @return Matrice binaire (0 ou 1)
     */
    public int[][] calculTabHachage(int[][] tab, double moyenne) {
        int[][] hachage = new int[tab.length][tab[0].length];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[0].length; j++) {
                hachage[i][j] = (tab[i][j] <= moyenne) ? 0 : 1;
            }
        }
        return hachage;
    }
}
