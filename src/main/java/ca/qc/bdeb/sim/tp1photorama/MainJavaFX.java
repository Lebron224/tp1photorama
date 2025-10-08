package ca.qc.bdeb.sim.tp1photorama;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe principale de l'application JavaFX Photorama.
 * Permet à l'utilisateur d'ouvrir un dossier d'images, d'afficher les photos,
 * et de détecter les doublons à l'aide de différents comparateurs.
 */
public class MainJavaFX extends Application {

    // Conteneur principal des images affichées
    private VBox gallerieBox;

    // Conteneur pour les doublons affichés
    private VBox doublonsBox;

    // Image par défaut (polaroid) affichée avant ouverture de galerie
    private final ImageView vuePolaroid = creerVuePolaroid();

    // État actuel de la tolérance sélectionnée
    private boolean isToleranceElevee = false;

    // Objet représentant la galerie en cours
    private Gallerie gallerie;

    // Comparateur utilisé pour détecter les doublons
    private ComparateurImages comparateur = new ComparateurImagesPixels();

    // Racine de la scène JavaFX
    private final BorderPane root = new BorderPane();

    // Scène principale
    private final Scene scene = new Scene(root, 900, 650);

    /**
     * Point d'entrée de l'application JavaFX.
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode appelée au démarrage de l'application JavaFX.
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        VBox miseEnPagePrincipale = creerRoot();
        miseEnPagePrincipale.setLayoutY(30);
        miseEnPagePrincipale.setLayoutX(30);
        root.getChildren().add(miseEnPagePrincipale);

        vuePolaroid.setX(400);
        vuePolaroid.setY(15);
        root.getChildren().add(vuePolaroid);

        // Permet de quitter avec la touche Échap
        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        // Clique pour effacer les messages d'erreur
        root.setOnMouseClicked(mouseEvent -> effacerErreur());

        // Affiche la scène
        stage.setTitle("Photorama");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Crée la structure de base de l'interface.
     * @return VBox contenant le titre et le menu
     */
    private VBox creerRoot() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);

        vbox.getChildren().add(creerTitre()); // Logo + Titre
        vbox.getChildren().add(creerMenu());  // Menu principal

        return vbox;
    }

    /**
     * Crée l'en-tête contenant le logo et le nom de l'application.
     * @return HBox avec logo et titre
     */
    private HBox creerTitre() {
        HBox enTete = new HBox(10);
        enTete.setAlignment(Pos.CENTER_LEFT);

        Image logo = new Image("logo.png");
        ImageView vueLogo = new ImageView(logo);
        vueLogo.setPreserveRatio(true);
        vueLogo.setFitHeight(50);

        Text titre = new Text("Photorama");
        titre.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        titre.setFill(Color.DARKBLUE);

        enTete.getChildren().addAll(vueLogo, titre);
        return enTete;
    }

    /**
     * Crée le menu permettant d'ouvrir une galerie et de configurer les options.
     * @return VBox contenant le menu
     */
    private VBox creerMenu() {
        VBox sectionGalerie = new VBox(5);
        sectionGalerie.setAlignment(Pos.CENTER);

        Text titreGalerie = new Text("Ouvrir une galerie");
        titreGalerie.setFont(Font.font(20));

        Text texteDetectionDoublons = new Text("Détection de doublons:");

        ChoiceBox<String> choixComparateur = creerChoixComparateur();

        // Event Handler qui appelle gererChoixComparateur pour gérer les changements de choix de comparateur
        choixComparateur.setOnAction(event -> {
            effacerErreur();
            gererChoixComparateur(choixComparateur.getValue());
        });

        Text texteTolerance = new Text("Tolérance aux différences:");

        HBox boutonsTolerance = creerBoutonsTolerance();

        Button boutonOuvrirDossier = new Button("Ouvrir un dossier...");

        // Event Handler qui appelle ouvrirNouvelleGallerie pour créer une nouvelle gallerie
        boutonOuvrirDossier.setOnAction(event -> {
            effacerErreur();
            try {
                ouvrirNouvelleGalerie();
            } catch (IOException e) {
                afficherErreur("Erreur lors de l'ouverture de la gallerie d'images");
            } catch (Exception e) {
                afficherErreur("Erreur détecté! Veuillez réessayez!");
            }
        });

        sectionGalerie.getChildren().addAll(
                titreGalerie,
                texteDetectionDoublons, choixComparateur,
                texteTolerance, boutonsTolerance,
                boutonOuvrirDossier
        );

        return sectionGalerie;
    }

    /**
     * Crée les choix disponibles pour les comparateurs.
     * @return ChoiceBox contenant les options
     */
    private ChoiceBox<String> creerChoixComparateur() {
        ChoiceBox<String> choix = new ChoiceBox<>();
        choix.getItems().addAll("Pixels", "Hachage (Moyenne)", "Hachage (Différences)");
        choix.setValue("Pixels"); // Option par défaut
        return choix;
    }

    /**
     * Crée les boutons radio pour choisir le niveau de tolérance.
     * @return HBox contenant les boutons radio
     */
    private HBox creerBoutonsTolerance() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        ToggleGroup groupe = new ToggleGroup();
        RadioButton faible = new RadioButton("Faible");
        RadioButton elevee = new RadioButton("Élevée");

        faible.setToggleGroup(groupe);
        elevee.setToggleGroup(groupe);
        faible.setSelected(true); // Par défaut

        hbox.getChildren().addAll(faible, elevee);

        // Event Handler qui appelle la classe gereChangementTolerance pour gérer les changements de choix de tolérance
        groupe.selectedToggleProperty().addListener((obs, oldToggle, newToggle) ->
                gererChangementTolerance(groupe));

        // Initialisation du comparateur
        gererChangementTolerance(groupe);

        return hbox;
    }

    /**
     * Crée l'image polaroid par défaut.
     * @return ImageView de l'image
     */
    private ImageView creerVuePolaroid() {
        Image img = new Image("polaroid.png");
        ImageView vue = new ImageView(img);
        vue.setPreserveRatio(true);
        vue.setFitHeight(350);
        return vue;
    }

    /**
     * Met à jour le comparateur selon le choix de l'utilisateur.
     * @param choix Comparateur sélectionné
     */
    private void gererChoixComparateur(String choix) {
        switch (choix) {
            case "Pixels" -> comparateur = new ComparateurImagesPixels();
            case "Hachage (Moyenne)" -> comparateur = new ComparateurImagesHachageMoyenne();
            case "Hachage (Différences)" -> comparateur = new ComparateurImagesHachageDifference();
        }
        appliquerTolerance();
    }

    /**
     * Gère le changement de tolérance (faible ou élevée).
     * @param groupeBoutton groupe de boutons radio
     */
    private void gererChangementTolerance(ToggleGroup groupeBoutton) {
        RadioButton rb = (RadioButton) groupeBoutton.getSelectedToggle();
        isToleranceElevee = rb.getText().equals("Élevée");
        appliquerTolerance();
        effacerErreur();
    }

    /**
     * Applique les paramètres de tolérance selon le comparateur sélectionné.
     */
    private void appliquerTolerance() {
        if (comparateur instanceof ComparateurImagesPixels) {
            comparateur.setSeuilDifference(isToleranceElevee ? 30 : 20);
            comparateur.setMaxPourcentage(isToleranceElevee ? 40 : 10);
        } else {
            comparateur.setMaxCases(isToleranceElevee ? 15 : 10);
        }
    }

    /**
     * Ouvre un dossier choisi par l'utilisateur et charge les images.
     */
    private void ouvrirNouvelleGalerie() throws IOException {
        String chemin = choisirDossier();
        if (chemin != null) {
            this.gallerie = new Gallerie(chemin, comparateur);

            if (gallerieBox != null) gallerieBox.getChildren().clear();
            if (doublonsBox != null) doublonsBox.getChildren().clear();

            root.getChildren().remove(vuePolaroid);
            afficherImages();
        }
    }

    /**
     * Ouvre un sélecteur de dossier et retourne le chemin.
     * @return Chemin absolu du dossier ou null
     */
    private String choisirDossier() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Sélectionnez un dossier d'images");
        chooser.setInitialDirectory(new File("."));

        File dossier = chooser.showDialog(null);
        return dossier != null ? dossier.getAbsolutePath() : null;
    }

    /**
     * Affiche un message d'erreur dans l'interface.
     * @param message le message à afficher
     */
    private void afficherErreur(String message) {
        var text = new Text(message);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setFill(Color.RED);

        HBox box = new HBox(text);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setBackground(new Background(
                new BackgroundFill(Color.web("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)
        ));

        root.setBottom(box);
    }

    /**
     * Efface le message d'erreur affiché (le cas échéant).
     */
    private void effacerErreur() {
        root.setBottom(null);
    }

    /**
     * Affiche les images principales (sans doublons) dans la fenêtre.
     */
    private void afficherImages() {
        ArrayList<ArrayList<String>> groupes = gallerie.getGroupesImages();

        Text titre = new Text("Mes photos (" + groupes.size() + ")");
        titre.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        titre.setFill(Color.DARKBLUE);

        var centerView = new ImageView(new Image("file:" + groupes.getFirst().getFirst()));
        centerView.setPreserveRatio(true);
        centerView.setFitWidth(565);
        centerView.setFitHeight(350);

        if (doublonsBox != null) {
            doublonsBox.getChildren().clear();
        }

        afficherDoublons(groupes, centerView, 0);

        HBox photoBox = new HBox(10);
        photoBox.setAlignment(Pos.CENTER);
        photoBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(photoBox);
        scrollPane.setMinViewportHeight(100);
        scrollPane.setMinViewportWidth(500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        for (int i = 0; i < groupes.size(); i++) {
            ImageView imgView = getImageView(groupes, i);
            photoBox.getChildren().add(imgView);

            final int index = i;
            imgView.setOnMouseClicked(e -> {
                centerView.setImage(new Image("file:" + groupes.get(index).getFirst()));
                doublonsBox.getChildren().clear();
                afficherDoublons(groupes, centerView, index);
            });
        }

        this.gallerieBox = new VBox(5, centerView, titre, scrollPane);
        gallerieBox.setAlignment(Pos.CENTER);
        gallerieBox.setPadding(new Insets(20));
        gallerieBox.setLayoutX(600);
        gallerieBox.setLayoutY(245);

        root.getChildren().add(gallerieBox);
    }

    /**
     * Crée une ImageView pour une miniature de photo.
     * @param groupes liste de groupes d'images
     * @param i index du groupe
     * @return ImageView de la première image du groupe
     */
    private ImageView getImageView(ArrayList<ArrayList<String>> groupes, int i) {
        String chemin = groupes.get(i).getFirst();
        ImageView imgView = new ImageView(new Image("file:" + chemin));
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(150);
        imgView.setFitHeight(100);
        return imgView;
    }

    /**
     * Affiche les doublons d'une image sous forme de miniatures.
     * @param groupes liste des groupes
     * @param centerImageView image principale
     * @param index index du groupe sélectionné
     */
    private void afficherDoublons(ArrayList<ArrayList<String>> groupes, ImageView centerImageView, int index) {
        HBox boxDoublons = new HBox(5);
        boxDoublons.setAlignment(Pos.CENTER);
        boxDoublons.setPadding(new Insets(5));

        ScrollPane scrollPane = new ScrollPane(boxDoublons);
        scrollPane.setMinViewportHeight(50);
        scrollPane.setMinViewportWidth(500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        for (int i = 1; i < groupes.get(index).size(); i++) {
            ImageView imgView = new ImageView(new Image("file:" + groupes.get(index).get(i)));
            imgView.setPreserveRatio(true);
            imgView.setFitWidth(100);
            imgView.setFitHeight(50);
            imgView.setOnMouseClicked(e -> centerImageView.setImage(imgView.getImage()));

            boxDoublons.getChildren().add(imgView);
        }

        Text titre = new Text("Doublons détectés :");
        titre.setFont(Font.font("Georgia", FontWeight.BOLD, 10));
        titre.setFill(Color.DARKBLUE);

        this.doublonsBox = new VBox(5, titre, scrollPane);
        doublonsBox.setPadding(new Insets(10));
        doublonsBox.setLayoutX(350);
        doublonsBox.setLayoutY(500);

        root.getChildren().add(doublonsBox);
    }
}
