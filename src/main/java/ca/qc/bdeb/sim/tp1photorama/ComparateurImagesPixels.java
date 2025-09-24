package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public class ComparateurImagesPixels extends ComparateurImages{
    @Override
    public boolean isPixelFaible() {
        return super.isPixelFaible();
    }

    @Override
    public void setPixelFaible(boolean pixelFaible) {
        super.setPixelFaible(pixelFaible);
    }

    @Override
    public boolean imagesSimilaires(String chemin1, String chemin2) throws IOException {
        //Convertissement des images en tableau de pixel à l'aide de la classe GestionnaireImgaes
            int[][] image1 = GestionnaireImages.toPixels(GestionnaireImages.lireImage(chemin1));
            int[][] image2 = GestionnaireImages.toPixels(GestionnaireImages.lireImage(chemin2));

            double pourcentageDifference;
            int nbrPixelDifferent = 0;

            if ((image1.length * image1[0].length) != (image2.length * image2[0].length)){
                return false;
            }

        for (int i = 0; i < image1.length; i++) {
            for (int j = 0; j < image1[0].length ; j++) {
                int difference = Math.abs(image1[i][j] - image2[i][j]);
                if (isPixelFaible() && difference > 20){
                    nbrPixelDifferent++;
                } else if (!isPixelFaible() && difference > 30) {
                    nbrPixelDifferent++;
                }
            }
        }

        pourcentageDifference = ((double) nbrPixelDifferent /(image1.length * image1[0].length)) * 100;

        if (isPixelFaible() && pourcentageDifference > 10){
            return false;
        } else return !isPixelFaible() && pourcentageDifference > 40;
    }
}
