package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

public class ComparateurImagesHachageDifference extends ComparateurImages{
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
        return false;
    }
}
