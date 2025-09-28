package ca.qc.bdeb.sim.tp1photorama;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Cette classe est responsable de charger les images d'un dossier donné
 * et de les regrouper en fonction de leur similarité à l'aide d'un {@link ComparateurImages}.
 * Les groupes d'images similaires sont enregistrés sous forme de liste de listes.
 * Une image unique aura son propre groupe.
 */
public class Gallerie {

    /** Objet utilisé pour comparer deux images et déterminer si elles sont similaires. */
    private ComparateurImages comparateur;

    /** Liste de groupes d'images similaires. Chaque groupe est une liste de noms de fichiers. */
    private ArrayList<ArrayList<String>> groupesImages;

    /** Chemin du dossier contenant les images à analyser. */
    private String dossier;

    /**
     * Constructeur de la classe Gallerie.
     *
     * @param dossier      Chemin vers le dossier contenant les images.
     * @param comparateur  Objet permettant de comparer deux images.
     * @throws IOException Si le dossier est invalide ou en cas d'erreur de lecture.
     */
    public Gallerie(String dossier, ComparateurImages comparateur) throws IOException {
        this.dossier = dossier;
        this.comparateur = comparateur;

        // Lecture et filtrage des images valides du dossier
        ArrayList<String> imagesValides = lectureFichier();

        // Regroupement des images similaires
        this.groupesImages = regrouperImages(imagesValides);
    }

    /**
     * Retourne les groupes d'images similaires trouvés dans le dossier.
     *
     * @return Liste de groupes d'images (chaque groupe est une liste de noms de fichiers).
     */
    public ArrayList<ArrayList<String>> getGroupesImages() {
        return groupesImages;
    }

    /**
     * Lit tous les fichiers du dossier, garde uniquement les images avec les extensions
     * .png ou .jpg, les trie alphabétiquement et retourne leur chemin complet.
     *
     * @return Liste des chemins complets des images valides.
     * @throws FileNotFoundException Si le dossier fourni n'est pas un répertoire valide.
     */
    public ArrayList<String> lectureFichier() throws FileNotFoundException {
        var repertoire = new File(dossier);

        // Vérification que le dossier existe et est valide
        if (repertoire.listFiles() == null) {
            throw new FileNotFoundException(dossier + " n'est pas un répertoire");
        }

        ArrayList<String> imagesValides = new ArrayList<>();

        // Parcours des fichiers du dossier
        for (var fichier : repertoire.listFiles()) {
            var nomFichier = fichier.getName();

            // Filtrage des fichiers avec extension .png ou .jpg
            if (nomFichier.toLowerCase().endsWith(".png") || nomFichier.toLowerCase().endsWith(".jpg")) {
                imagesValides.add(nomFichier);
            }
        }

        // Tri alphabétique des noms de fichiers
        Collections.sort(imagesValides);

        // Ajout du chemin complet à chaque image
        imagesValides.replaceAll(s -> dossier + "/" + s);

        return imagesValides;
    }

    /**
     * Regroupe les images similaires ensemble en utilisant le comparateur fourni.
     * Chaque groupe contiendra une ou plusieurs images semblables.
     * Les images uniques auront leur propre groupe.
     *
     * @param imagesValides Liste des chemins complets des images à regrouper.
     * @return Liste de groupes d'images similaires.
     * @throws IOException Si une erreur survient lors de la comparaison des images.
     */
    public ArrayList<ArrayList<String>> regrouperImages(ArrayList<String> imagesValides) throws IOException {
        ArrayList<ArrayList<String>> groupesImages = new ArrayList<>();

        while (!imagesValides.isEmpty()) {
            // Prend la première image comme point de référence
            String imageCourante = imagesValides.get(0);

            // Création d'un nouveau groupe pour cette image
            ArrayList<String> groupe = new ArrayList<>();
            groupe.add(imageCourante);

            // Liste des images similaires à supprimer plus tard
            ArrayList<String> imagesASupprimer = new ArrayList<>();

            // Comparaison avec toutes les autres images
            for (int i = 1; i < imagesValides.size(); i++) {
                String autresImages = imagesValides.get(i);

                // Vérification de la similarité entre imageCourante et autresImages
                if (comparateur.imagesSimilaires(imageCourante, autresImages)) {
                    groupe.add(autresImages);
                    imagesASupprimer.add(autresImages); // Marque cette image pour suppression
                }
            }

            // Retirer toutes les images de ce groupe de la liste principale
            imagesValides.remove(imageCourante);
            imagesValides.removeAll(imagesASupprimer);

            // Ajout du groupe final à la liste des groupes
            groupesImages.add(groupe);
        }

        return groupesImages;
    }
}
