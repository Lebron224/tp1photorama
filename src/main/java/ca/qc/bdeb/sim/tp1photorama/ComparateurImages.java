package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public abstract class ComparateurImages {
    private int seuilDifference;
    private double maxPourcentage;
    private int maxCases;

    public int getSeuilDifference() {
        return seuilDifference;
    }

    public void setSeuilDifference(int seuilDifference) {
        this.seuilDifference = seuilDifference;
    }

    public double getMaxPourcentage() {
        return maxPourcentage;
    }

    public void setMaxPourcentage(double maxPourcentage) {
        this.maxPourcentage = maxPourcentage;
    }

    public int getMaxCases() {
        return maxCases;
    }

    public void setMaxCases(int maxCases) {
        this.maxCases = maxCases;
    }

    public abstract boolean imagesSimilaires(String chemin1, String chemin2) throws IOException;
}
