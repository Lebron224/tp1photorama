package ca.qc.bdeb.sim.tp1photorama;


public class MainCmd {
    public static void main(String[] args) {
        ComparateurImages comp = new ComparateurImagesPixels();
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

        }

    }
}
