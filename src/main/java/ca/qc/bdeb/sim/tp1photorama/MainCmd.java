package ca.qc.bdeb.sim.tp1photorama;


import java.io.IOException;

public class MainCmd {
    public static void main(String[] args) throws IOException {
        ComparateurImages compPixel = new ComparateurImagesPixels();
        ComparateurImages compMoyenne = new ComparateurImagesHachageMoyenne();
        ComparateurImages compDifference = new ComparateurImagesHachageDifference();
        Object[][] debugPixel = {
            {5,20,"debogage/pixels1.png","debogage/pixels2.png"},
            {5,20,"debogage/pixels1.png","debogage/pixels3.png"},
            {5,40,"debogage/pixels1.png","debogage/pixels4.png"},
            {5,20,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,49,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,50,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,51,"debogage/pixels1.png","debogage/pixels4.png"}
        };

        for (Object[] o : debugPixel){
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1];
            String img1 = (String) o[2];
            String img2 = (String) o[3];
            String resultat = compPixel.imagesSimilaires(img1,img2) ? "SIMILAIRES" : "DIFFERENTS";
            compPixel.setSeuilDifference(seuil); compPixel.setMaxPourcentage(maxPourcentage);
            System.out.println("=== 1. Test des différences de pixels ===\n");
            System.out.println("seuil="+seuil+", max pourcent="+maxPourcentage+", "+img1+" vs "+img2+" "+resultat);
        }

        String[] debugHachage = {
                "debogage/moyenne-test1.png",
                "debogage/moyenne-test2.png",
                "debogage/moyenne-test3.png",
                "debogage/diff-test1.png",
                "debogage/diff-test2.png",
                "debogage/diff-test3.png"
        };

        for (String s : debugHachage){

        }



    }
}
