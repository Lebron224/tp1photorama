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

public class MainJavaFX extends Application {
    private VBox gallerieBox;
    private VBox doublonsBox;
    private final ImageView vuePolaroid = creerVuePolaroid();
    private boolean isToleranceElevee = false;
    private Gallerie gallerie;
    private ComparateurImages comparateur = new ComparateurImagesPixels();

    public static void main(String[] args) {
        launch(args);
    }

    private final BorderPane root = new BorderPane();
    private final Scene scene = new Scene(root, 900, 600);

    @Override
    public void start(Stage stage) {
        // Création de l'interface utilisateur
        VBox miseEnPagePrincipale = creerMiseEnPagePrincipale();
        miseEnPagePrincipale.setLayoutY(30);
        miseEnPagePrincipale.setLayoutX(30);
        root.getChildren().add(miseEnPagePrincipale);

        // Affichage de l'image polaroid

        vuePolaroid.setX(400);
        vuePolaroid.setY(15);
        root.getChildren().add(vuePolaroid);


        // Quitter le programme avec la touche Escape
        root.setOnKeyPressed((keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        }));

        // Affichage de la scène
        stage.setTitle("Photorama");
        stage.setScene(scene);
        stage.show();
    }

    private VBox creerMiseEnPagePrincipale() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);

        // Ajout du logo et du titre
        HBox enTete = creerEnTete();
        vbox.getChildren().add(enTete);

        // Section "Ouvrir une galerie" et options
        VBox sectionGalerie = creerSectionGalerie();
        vbox.getChildren().add(sectionGalerie);

        return vbox;
    }

    private HBox creerEnTete() {
        HBox enTete = new HBox();
        enTete.setAlignment(Pos.CENTER_LEFT);
        enTete.setSpacing(10);

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

    private VBox creerSectionGalerie() {
        VBox sectionGalerie = new VBox();
        sectionGalerie.setSpacing(5);
        sectionGalerie.setAlignment(Pos.CENTER);

        Text titreGalerie = new Text("Ouvrir une galerie");
        titreGalerie.setFont(Font.font(20));

        Text texteDetectionDoublons = new Text("Détection de doublons:");

        ChoiceBox<String> choixDetection = creerChoixDetection();
        choixDetection.setOnAction(actionEvent -> {
            effacerErreur();
            gererChoixDetection(choixDetection.getValue());
        });


        Text texteTolerance = new Text("Tolérance aux différences:");

        HBox boutonsTolerance = creerBoutonsTolerance();

        Button boutonOuvrirDossier = new Button("Ouvrir un dossier...");
        boutonOuvrirDossier.setOnAction(event -> {
            effacerErreur();
            try {
                ouvrirGalerie();
            } catch (IOException e) {
                afficherErreur("Erreur lors de l'ouverture de la gallerie d'images");
            }
        });

        sectionGalerie.getChildren().addAll(
                titreGalerie, texteDetectionDoublons, choixDetection, texteTolerance, boutonsTolerance, boutonOuvrirDossier
        );

        return sectionGalerie;
    }

    private ChoiceBox<String> creerChoixDetection() {
        ChoiceBox<String> choix = new ChoiceBox<>();
        choix.getItems().addAll("Pixels", "Hachage (Moyenne)", "Hachage (Différences)");
        choix.setValue(choix.getItems().getFirst());
        return choix;
    }

    private HBox creerBoutonsTolerance() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        ToggleGroup groupeBoutton = new ToggleGroup();
        RadioButton toleranceFaible = new RadioButton("Faible");
        RadioButton toleranceElevee = new RadioButton("Élevée");

        toleranceFaible.setToggleGroup(groupeBoutton);
        toleranceElevee.setToggleGroup(groupeBoutton);

        // Pré-sélectionner "Faible"
        toleranceFaible.setSelected(true);

        hbox.getChildren().addAll(toleranceFaible, toleranceElevee);

        // On délègue le traitement à une méthode séparée
        groupeBoutton.selectedToggleProperty().addListener(
                (obs, ancienToggle, nouveauToggle) -> gererChangementTolerance(groupeBoutton)
        );

        // Appeler manuellement une première fois pour initialiser comparateur
        gererChangementTolerance(groupeBoutton);

        return hbox;
    }

    private ImageView creerVuePolaroid() {
        Image imagePolaroid = new Image("polaroid.png");
        ImageView vuePolaroid = new ImageView(imagePolaroid);
        vuePolaroid.setPreserveRatio(true);
        vuePolaroid.setFitHeight(350);
        return vuePolaroid;
    }

    private void gererChoixDetection(String choix) {
        switch (choix) {
            case "Pixels":
                this.comparateur = new ComparateurImagesPixels();
                break;
            case "Hachage (Moyenne)":
                this.comparateur = new ComparateurImagesHachageMoyenne();
                break;
            case "Hachage (Différences)":
                this.comparateur = new ComparateurImagesHachageDifference();
                break;
        }
        appliquerTolerance();
    }

    private void gererChangementTolerance(ToggleGroup groupeBoutton) {
        RadioButton rb = (RadioButton) groupeBoutton.getSelectedToggle();

        isToleranceElevee = rb.getText().equals("Élevée");

        appliquerTolerance();
        effacerErreur();
    }

    private void appliquerTolerance() {
        if (comparateur instanceof ComparateurImagesPixels) {
            if (!isToleranceElevee) { // Faible
                comparateur.setSeuilDifference(20);
                comparateur.setMaxPourcentage(10);
            } else {  // Élevée
                comparateur.setSeuilDifference(30);
                comparateur.setMaxPourcentage(40);
            }
        } else {
            if (!isToleranceElevee) { // Faible
                comparateur.setMaxCases(10);
            } else comparateur.setMaxCases(15); // Élevée
        }
    }


    private void ouvrirGalerie() throws IOException {
        String cheminDossier = choisirDossier();
        if (cheminDossier != null) {
            if (gallerieBox != null) {
                gallerieBox.getChildren().clear();
            } else if (doublonsBox != null) {
                doublonsBox.getChildren().clear();
            }

            this.gallerie = new Gallerie(cheminDossier, comparateur);
            root.getChildren().remove(vuePolaroid);
            afficherImages();
        }
    }

    private String choisirDossier() {
        DirectoryChooser selecteurDossier = new DirectoryChooser();
        selecteurDossier.setTitle("Sélectionnez un dossier d'images");
        selecteurDossier.setInitialDirectory(new File("."));
        File dossierSelectionne = selecteurDossier.showDialog(null);

        if (dossierSelectionne != null) {
            return dossierSelectionne.getAbsolutePath();
        }
        return null;
    }

    private void afficherErreur(String message) {
        var messageErreur = new Text(message);
        messageErreur.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        messageErreur.setFill(Color.RED); // texte rouge vif

        // Conteneur avec fond rouge pâle
        HBox box = new HBox(messageErreur);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10)); // un peu d’espace autour du texte
        box.setBackground(new Background(
                new BackgroundFill(Color.web("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)
        ));

        root.setBottom(box);
    }

    private void effacerErreur() {
        root.setBottom(null);
    }

    private void afficherImages() {
        ArrayList<ArrayList<String>> tab = gallerie.getGroupesImages();

        // Titre
        Text text = new Text("Mes photos (" + tab.size() + ")");
        text.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        text.setFill(Color.DARKBLUE);

        // Image centrale
        var centerImageView = new ImageView();
        centerImageView.setPreserveRatio(true);
        centerImageView.setFitWidth(565);
        centerImageView.setFitHeight(350);
        centerImageView.setImage(new Image("file:"+tab.getFirst().getFirst()));
        afficherDoublons(tab, centerImageView);


        // HBox pour les miniatures
        var photoBox = new HBox(10);
        photoBox.setAlignment(Pos.CENTER);
        photoBox.setPadding(new Insets(10));

        // ScrollPane pour les miniatures
        var scrollPane = new ScrollPane(photoBox);
        scrollPane.setMinViewportHeight(100); scrollPane.setMinViewportWidth(500);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Ajouter les images principales (première de chaque groupe)
        for (int i = 0; i < tab.size(); i++) {
            var imgView = getImageView(tab, i);

            photoBox.getChildren().add(imgView);

            // Quand on clique sur une image
            int index = i;
            imgView.setOnMouseClicked(e -> {
                centerImageView.setImage(new Image("file:" + tab.get(index).getFirst()));
                doublonsBox.getChildren().clear();
                afficherDoublons(tab, centerImageView);
            });
        }

        // Organisation de la mise en page
        this.gallerieBox = new VBox(5, centerImageView, text, scrollPane);
        gallerieBox.setAlignment(Pos.CENTER);
        gallerieBox.setPadding(new Insets(20));
        gallerieBox.setLayoutX(600);   gallerieBox.setLayoutY(245);

        root.getChildren().add(gallerieBox); // 🔹 affichage dans la fenêtre principale
    }

    private ImageView getImageView(ArrayList<ArrayList<String>> tab, int i) {
        String chemin = tab.get(i).getFirst();
        var imgView = new ImageView(new Image("file:" + chemin));
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(150);
        imgView.setFitHeight(100);
        return imgView;
    }

    private void afficherDoublons(ArrayList<ArrayList<String>> groupes, ImageView centerImageView) {
        var boxDoublons = new HBox(10);
        boxDoublons.setAlignment(Pos.CENTER);
        boxDoublons.setPadding(new Insets(10));

        // Récupère l’image centrale
        var imageCentrale = centerImageView.getImage();
        if (imageCentrale == null) return;

        // 🔹 Trouver le chemin correspondant à l’image centrale
        String urlImageCentrale = imageCentrale.getUrl(); // ex: file:/C:/images/a.png
        if (urlImageCentrale == null) return;

        // 🔹 Trouver le groupe de doublons qui contient cette image
        for (ArrayList<String> groupe : groupes) {
            for (String chemin : groupe) {
                if (urlImageCentrale.equalsIgnoreCase("file:"+chemin)) { // compare par nom ou chemin relatif
                    // Ce groupe est celui de l’image centrale
                    for (String doublon : groupe) {
                        var imgView = new ImageView(new Image("file:" + doublon));
                        imgView.setPreserveRatio(true);
                        imgView.setFitWidth(120);
                        imgView.setFitHeight(80);

                        // Clique sur un doublon pour le mettre au centre
                        imgView.setOnMouseClicked(e ->
                                centerImageView.setImage(new Image("file:" + doublon))
                        );

                        boxDoublons.getChildren().add(imgView);
                    }
                    break; // on arrête après avoir trouvé le bon groupe
                }
            }
        }

        // 🔹 Affiche les doublons en bas
        Text textDoublons = new Text("Doublons détectés :");
        textDoublons.setFill(Color.DARKBLUE);
        textDoublons.setFont(Font.font("Georgia", FontWeight.BOLD, 10));

        this.doublonsBox = new VBox(5, textDoublons, boxDoublons);
        doublonsBox.setPadding(new Insets(10));
        doublonsBox.setLayoutX(350);    doublonsBox.setLayoutY(500);

        root.getChildren().add(doublonsBox);
    }

}
