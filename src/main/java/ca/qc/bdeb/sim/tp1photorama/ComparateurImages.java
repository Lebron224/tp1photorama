package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public abstract class ComparateurImages {
    private boolean isToleranceFaible;

    public boolean isToleranceFaible() {
        return isToleranceFaible;
    }

    public void setToleranceFaible(boolean toleranceFaible) {
        isToleranceFaible = toleranceFaible;
    }
    public abstract boolean imagesSimilaires(String chemin1, String chemin2) throws IOException;
}
