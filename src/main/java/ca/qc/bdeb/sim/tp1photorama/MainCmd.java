package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public class MainCmd {
    public static void main(String[] args) {
        ComparateurImages comp = new ComparateurImagesPixels();
        try {
            boolean similaire = comp.imagesSimilaires("airbnb-mini/a1.jpg","airbnb-mini/a3.jpg");
            if (similaire){
                System.out.println("Similaire");
            }else System.out.println("T crasse tu sais pas code");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
