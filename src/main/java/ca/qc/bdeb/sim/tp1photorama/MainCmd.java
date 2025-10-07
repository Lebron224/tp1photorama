package ca.qc.bdeb.sim.tp1photorama;


import java.io.IOException;

public class MainCmd {
    public static void main(String[] args) throws IOException {
        var compPixel = new ComparateurImagesPixels();
        var compMoyenne = new ComparateurImagesHachageMoyenne();
        var compDifference = new ComparateurImagesHachageDifference();
        Object[][] debugPixel = {
            {5,0.2,"debogage/pixels1.png","debogage/pixels2.png"},
            {5,0.2,"debogage/pixels1.png","debogage/pixels3.png"},
            {5,0.4,"debogage/pixels1.png","debogage/pixels4.png"},
            {5,0.2,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.49,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.50,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.51,"debogage/pixels1.png","debogage/pixels4.png"}
        };

        System.out.println("=== 1. Test des différences de pixels ===");
        for (Object[] o : debugPixel){
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1] * 100;
            String img1 = (String) o[2];
            String img2 = (String) o[3];
            String resultat = compPixel.imagesSimilaires(img1,img2) ? "SIMILAIRES" : "DIFFERENTS";
            compPixel.setSeuilDifference(seuil); compPixel.setMaxPourcentage(maxPourcentage);
            System.out.println("seuil="+seuil+", max pourcent="+maxPourcentage+", "+img1+" vs "+img2+" "+resultat);
        }

        String[] debugHachageMoyenne = {
                "debogage/moyenne-test1.png",
                "debogage/moyenne-test2.png",
                "debogage/moyenne-test3.png"
        };
        System.out.println("=== 2. Affichage des valeurs de hachage ===");
        for (String s : debugHachageMoyenne){
            System.out.println(s+":");
            int[][] tab = compMoyenne.calculTabHachageUnImage(s);
            for (int[] i : tab) {
                for (int j = 0; j < tab[0].length; j++) {
                    if (i[j] == 0) {
                        System.out.print(" ");
                    } else System.out.print("1");
                }
                System.out.println();
            }
        }

        String[] debugHachageDifferences = {
                "debogage/diff-test1.png",
                "debogage/diff-test2.png",
                "debogage/diff-test3.png"
        };
        for (String s : debugHachageDifferences){
            System.out.println(s+":");
            int[][] tab = compDifference.calculTabHachageUnImage(s);
            for (int[] i : tab){
                for (int j = 0; j < tab[0].length; j++) {
                    if (i[j] == 0){
                        System.out.print(" ");
                    } else System.out.print("1");
                }
                System.out.println();
            }
        }



    }
}
