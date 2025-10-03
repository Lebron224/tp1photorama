package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

/**
 * Comparateur d'images basé sur les pixels individuels.
 * Compare la différence de luminosité pixel par pixel.
 */
public class ComparateurImagesPixels extends ComparateurImages {
    /**
     * Compare deux images pixel par pixel et détermine si elles sont similaires.
     *
     * @param chemin1 Chemin vers la première image
     * @param chemin2 Chemin vers la deuxième image
     * @return true si les images sont similaires selon le seuil de tolérance
     * @throws IOException si une image ne peut pas être lue
     */
    @Override
    public boolean imagesSimilaires(String chemin1, String chemin2) throws IOException {
        // Lecture et transformation des images en tableaux de pixels
        int[][] image1 = GestionnaireImages.toPixels(GestionnaireImages.lireImage(chemin1));
        int[][] image2 = GestionnaireImages.toPixels(GestionnaireImages.lireImage(chemin2));

        // Vérifie que les images ont la même dimension
        if ((image1.length != image2.length) || (image1[0].length != image2[0].length)) {
            return false;
        }

        // Méthode qui retourne le pourcentage de différence
        double pourcentageDifference = calculPourcentageDifference(image1, image2);

        // Retourne true si les images sont similaires selon la tolérance
        return pourcentageDifference <= getMaxPourcentage();
    }

    /**
     * Calcule le pourcentage de différence en comparant les deux tableaux
     *
     * @param image1 Tableaux en pixel de l'image 1
     * @param image2 Tableaux en pixel de l'image 2
     * @return le pourcentage de différence pour déterminer la similitude des images
     */
    private double calculPourcentageDifference(int[][] image1, int[][] image2) {
        int nbrPixelDifferent = 0;

        // Parcourt les pixels et compte ceux qui dépassent le seuil de tolérance
        for (int i = 0; i < image1.length; i++) {
            for (int j = 0; j < image1[0].length; j++) {
                int difference = Math.abs(image1[i][j] - image2[i][j]);
                if (difference > getSeuilDifference()) {
                    nbrPixelDifferent++;
                }
            }
        }

        // Calcule et retourne le pourcentage de pixels différents
        return ((double) nbrPixelDifferent / (image1.length * image1[0].length)) * 100;
    }
}
