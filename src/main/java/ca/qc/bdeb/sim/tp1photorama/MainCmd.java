package ca.qc.bdeb.sim.tp1photorama;


import java.io.File;
import java.io.IOException;

public class MainCmd {
    public static void main(String[] args) throws IOException {
        final var cheminDossier = "C:\\Users\\bartl\\Desktop\\Tp1\\tp1photorama\\airbnb-petit";
        var compPixel = new ComparateurImagesPixels();
        var compMoyenne = new ComparateurImagesHachageMoyenne();
        var compDifference = new ComparateurImagesHachageDifference();
        Object[][] debugPixel = {
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels2.png"},
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels3.png"},
                {5, 0.4, "debogage/pixels1.png", "debogage/pixels4.png"},
                {5, 0.2, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.49, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.50, "debogage/pixels1.png", "debogage/pixels4.png"},
                {128, 0.51, "debogage/pixels1.png", "debogage/pixels4.png"},
        };

        System.out.println("=== 1. Test des differences de pixels ===");
        for (Object[] o : debugPixel) {
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1] * 100;
            String img1 = (String) o[2];
            String img2 = (String) o[3];
            String resultat = compPixel.imagesSimilaires(img1, img2) ? "SIMILAIRES" : "DIFFERENTS";
            compPixel.setSeuilDifference(seuil);
            compPixel.setMaxPourcentage(maxPourcentage);
            System.out.println("seuil=" + seuil + ", max pourcent=" + maxPourcentage/100 + ", " + img1 + " vs " + img2 + " " + resultat);
        }

        String[] debugHachageMoyenne = {"debogage/moyenne-test1.png", "debogage/moyenne-test2.png", "debogage/moyenne-test3.png",};
        String[] debugHachageDifferences = {"debogage/diff-test1.png", "debogage/diff-test2.png", "debogage/diff-test3.png"};

        System.out.println("=== 2. Affichage des valeurs de hachage ===");
        afficherHachages(debugHachageMoyenne,compMoyenne);
        afficherHachages(debugHachageDifferences,compDifference);

        System.out.println("=== 3. Analyse de la qualité des différents algos pour airbnb-petit ===");
        Object[][] tabComp = {
                {8, 0.2, new ComparateurImagesPixels()},
                {20, 0.5, new ComparateurImagesPixels()},
        };

        for (Object[] o : tabComp) {
            int seuil = (int) o[0];
            double maxPourcentage = (double) o[1] * 100;
            ComparateurImagesPixels comp = (ComparateurImagesPixels) o[2];
            comp.setSeuilDifference(seuil); comp.setMaxPourcentage(maxPourcentage);
            Gallerie gallerie = new Gallerie(cheminDossier, comp);

            System.out.println("Algo: Comparateur Pixels  (seuil différences=" + seuil + ", pourcentage différences max=" + maxPourcentage/100 +")");
            testGallerieAirbnbPetitHachage(gallerie);
        }

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

            if (comp instanceof ComparateurImagesHachageMoyenne){
                System.out.println("Algo: Comparateur Hachage Moyenne (cases différentes max="+maxCases+")");
            }else if (comp instanceof ComparateurImagesHachageDifference){
                System.out.println("Algo: Comparateur Hachage Différences (cases différentes max="+maxCases+")");
            }
            testGallerieAirbnbPetitHachage(gallerie);
        }
    }

    private static void testGallerieAirbnbPetitHachage(Gallerie gallerie) {
        for (int i = 0; i < gallerie.getGroupesImages().size(); i++) {
            System.out.print("["+(i + 1)+"] ");
            for (int j = 0; j < gallerie.getGroupesImages().get(i).size(); j++) {
                String img = new File(gallerie.getGroupesImages().get(i).get(j)).getName();
                System.out.print(img +" ");
            }
            System.out.println();
        }
    }

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
