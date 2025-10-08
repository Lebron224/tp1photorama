package ca.qc.bdeb.sim.tp1photorama;

import java.io.IOException;

/**
 * Classe abstraite représentant un comparateur d'images.
 * Cette classe sert de base pour implémenter différents algorithmes
 * permettant de comparer deux images et déterminer leur similarité.
 */
public abstract class ComparateurImages {

    /**
     * Seuil de différence maximale autorisé entre deux pixels.
     * Utilisé pour déterminer si deux pixels sont considérés comme différents.
     */
    private int seuilDifference;

    /**
     * Pourcentage maximal de cases différentes autorisé entre deux images
     * pour qu'elles soient considérées comme similaires.
     */
    private double maxPourcentage;

    /**
     * Nombre maximum de cases (ou blocs) utilisées pour comparer les images.
     */
    private int maxCases;

    /**
     * Retourne le seuil de différence entre deux pixels.
     *
     * @return le seuil de différence
     */
    public int getSeuilDifference() {
        return seuilDifference;
    }

    /**
     * Définit le seuil de différence entre deux pixels.
     *
     * @param seuilDifference le seuil à définir
     */
    public void setSeuilDifference(int seuilDifference) {
        this.seuilDifference = seuilDifference;
    }

    /**
     * Retourne le pourcentage maximal de différences toléré entre deux images.
     *
     * @return le pourcentage maximal
     */
    public double getMaxPourcentage() {
        return maxPourcentage;
    }

    /**
     * Définit le pourcentage maximal de différences toléré entre deux images.
     *
     * @param maxPourcentage le pourcentage à définir
     */
    public void setMaxPourcentage(double maxPourcentage) {
        this.maxPourcentage = maxPourcentage;
    }

    /**
     * Retourne le nombre maximal de cases utilisées pour la comparaison.
     *
     * @return le nombre maximal de cases
     */
    public int getMaxCases() {
        return maxCases;
    }

    /**
     * Définit le nombre maximal de cases utilisées pour la comparaison.
     *
     * @param maxCases le nombre de cases à définir
     */
    public void setMaxCases(int maxCases) {
        this.maxCases = maxCases;
    }

    /**
     * Méthode abstraite à implémenter pour comparer deux images.
     *
     * @param chemin1 Chemin de la première image
     * @param chemin2 Chemin de la deuxième image
     * @return true si les images sont considérées comme similaires, false sinon
     * @throws IOException si une erreur de lecture des fichiers image survient
     */
    public abstract boolean imagesSimilaires(String chemin1, String chemin2) throws IOException;

    /**
     * Méthode abstraite à implémenter pour calculer le tableau de hachage
     * d'une image, qui représentera son contenu de manière simplifiée.
     *
     * @param chemin Chemin de l'image
     * @return un tableau 2D représentant le hachage de l'image
     * @throws IOException si une erreur de lecture du fichier image survient
     */
    public abstract int[][] calculTabHachageUnImage(String chemin) throws IOException;
}
