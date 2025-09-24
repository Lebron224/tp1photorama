package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public abstract class ComparateurImages {
    private boolean isPixelFaible;

    public boolean isPixelFaible() {
        return isPixelFaible;
    }

    public void setPixelFaible(boolean pixelFaible) {
        isPixelFaible = pixelFaible;
    }
    public abstract boolean imagesSimilaires(String chemin1, String chemin2) throws IOException;
}
