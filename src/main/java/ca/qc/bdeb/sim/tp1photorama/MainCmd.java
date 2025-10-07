package ca.qc.bdeb.sim.tp1photorama;


import java.io.IOException;

public class MainCmd {
    public static void main(String[] args) throws IOException {
        ComparateurImages compPixel = new ComparateurImagesPixels();
        ComparateurImages compMoyenne = new ComparateurImagesHachageMoyenne();
        ComparateurImages compDifference = new ComparateurImagesHachageDifference();
        Object[][] debug = {
            {5,0.2,"debogage/pixels1.png","debogage/pixels2.png"},
            {5,0.2,"debogage/pixels1.png","debogage/pixels3.png"},
            {5,0.2,"debogage/pixels1.png","debogage/pixels4.png"},
            {5,0.5,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.49,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.5,"debogage/pixels1.png","debogage/pixels4.png"},
            {128,0.51,"debogage/pixels1.png","debogage/pixels4.png"}
        };

        for (Object[] o : debug){
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1];
            String img1 = (String) o[2];
            String img2 = (String) o[3];
            compPixel.setSeuilDifference(seuil); compPixel.setMaxPourcentage(maxPourcentage);
            System.out.println("seuil="+seuil+", max pourcent="+maxPourcentage+", "+img1+" vs "+img2+" "+ compPixel.imagesSimilaires(img1,img2));
        }



    }
}
