package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public class ComparateurImagesHachageMoyenne extends ComparateurImages{
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
        int[][] image1 = GestionnaireImages.toPixels(GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin1),8,8));
        int[][] image2 = GestionnaireImages.toPixels(GestionnaireImages.redimensionner(GestionnaireImages.lireImage(chemin2),8,8));

        double moyenne1; double moyenne2; int[][] hachage1; int[][] hachage2;


        moyenne1 = calculMoyenne(image1);
        moyenne2 = calculMoyenne(image2);

        hachage1 = calculTabHachage(image1,moyenne1);
        hachage2 = calculTabHachage(image2, moyenne2);

        int differences = 0;

        for (int i = 0; i < image1.length; i++) {
            for (int j = 0; j < image1[0].length; j++) {
                if (hachage1[i][j] != hachage2[i][j]){
                    differences++;
                }
            }
        }

        if (isPixelFaible() && differences <= 10){
            return true;
        }else return !isPixelFaible() && differences <= 15;
    }

    public double calculMoyenne(int[][] tab){
        int somme = 0;

        for(int[] i : tab){
            for (int j = 0; j < tab[0].length; j++) {
                somme += i[j];
            }
        }
        return (double) somme /(tab.length * tab[0].length);
    }

    public int[][] calculTabHachage(int[][] tab, double moyenne){
        for (int i = 0; i < tab.length; i++){
            for (int j = 0; j < tab[0].length; j++) {
                if (tab[i][j] <= moyenne){
                    tab[i][j] = 0;
                }else tab[i][j] = 1;
            }
        }
        return tab;
    }
}
