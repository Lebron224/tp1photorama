package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

/**
 * Comparateur d'images utilisant l'algorithme de hachage par différence verticale.
 * On compare chaque pixel à celui juste en dessous, dans une image redimensionnée à 8x9.
 */
public class ComparateurImagesHachageDifference extends ComparateurImages {
    /**
     * Compare deux images en utilisant le hachage par différence verticale.
     * Redimensionne les images à 8x9, calcule le hachage 8x8, puis compare les hachages.
     *
     * @param chemin1 Chemin du fichier de la première image
     * @param chemin2 Chemin du fichier de la deuxième image
     * @return true si les images sont considérées similaires
     * @throws IOException si une image ne peut pas être lue
     */
    @Override
    public boolean imagesSimilaires(String chemin1, String chemin2) throws IOException {
        // Lecture et redimensionnement des images à 8 colonnes x 9 lignes
        int[][] image1 = GestionnaireImages.toPixels(
                GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin1), 8, 9));
        int[][] image2 = GestionnaireImages.toPixels(
                GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin2), 8, 9));

        // Calcul des tableaux de hachage (8x8) pour chaque image
        int[][] hachage1 = calculTabHache(image1);
        int[][] hachage2 = calculTabHache(image2);

        // Comparaison des deux hachages : on compte le nombre de bits différents
        int differences = 0;
        for (int i = 0; i < hachage1.length; i++) {
            for (int j = 0; j < hachage1[0].length; j++) {
                if (hachage1[i][j] != hachage2[i][j]) {
                    differences++;
                }
            }
        }

        // Retourne true si les différences sont en dessous du seuil de tolérance
        return differences <= getMaxCases();
    }

    /**
     * Calcule un tableau de hachage 8x8 basé sur la comparaison verticale des pixels.
     * Pour chaque pixel (i, j), on le compare à celui juste en dessous (i+1, j).
     * Si le pixel courant est plus foncé ou égal (plus petit ou égal) à celui du bas, on met 0, sinon 1.
     *
     * @param tab Tableaux 8x9 de pixels de l'image à hacher
     * @return Tableaux de hachage 8x8 contenant des 0 ou 1
     */
    private int[][] calculTabHache(int[][] tab) {
        int[][] hachage = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tab[i][j] <= tab[i + 1][j]) {
                    hachage[i][j] = 0;
                } else {
                    hachage[i][j] = 1;
                }
            }
        }

        return hachage;
    }

    public int[][] calculTabHachageUnImage(String chemin) throws IOException{
        int[][] tab = GestionnaireImages.toPixels(GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin),8,9));
        return calculTabHache(tab);
    }
}
