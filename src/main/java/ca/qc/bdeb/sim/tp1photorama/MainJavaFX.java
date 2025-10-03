package ca.qc.bdeb.sim.tp1photorama;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;

public class MainJavaFX extends Application {

    private Gallerie gallerie;
    private ComparateurImages comparateur;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        var root = new BorderPane();
        var scene = new Scene(root, 900, 600);

        // Création de l'interface utilisateur
        VBox miseEnPagePrincipale = creerMiseEnPagePrincipale();
        miseEnPagePrincipale.setLayoutY(30);    miseEnPagePrincipale.setLayoutX(30);
        root.getChildren().add(miseEnPagePrincipale);

        // Affichage de l'image polaroid
        ImageView vuePolaroid = creerVuePolaroid();
        vuePolaroid.setX(400);  vuePolaroid.setY(15);
        root.getChildren().add(vuePolaroid);

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
        titre.setFont(Font.font(30));

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
        choixDetection.setOnAction(actionEvent -> gererChoixDetection(choixDetection.getValue()));


        Text texteTolerance = new Text("Tolérance aux différences:");

        HBox boutonsTolerance = creerBoutonsTolerance();

        Button boutonOuvrirDossier = new Button("Ouvrir un dossier...");
        boutonOuvrirDossier.setOnAction(event -> ouvrirGalerie());

        sectionGalerie.getChildren().addAll(
                titreGalerie, texteDetectionDoublons, choixDetection, texteTolerance, boutonsTolerance, boutonOuvrirDossier
        );

        return sectionGalerie;
    }

    private ChoiceBox<String> creerChoixDetection() {
        ChoiceBox<String> choix = new ChoiceBox<>();
        choix.getItems().addAll(null, "Pixels", "Hachage (Moyenne)", "Hachage (Différences)");
        choix.setConverter(new StringConverter<>() {
            @Override
            public String toString(String s) {
                return (s == null) ? "Choisissez une option..." : s;
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });
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

        hbox.getChildren().addAll(toleranceFaible, toleranceElevee);

        groupeBoutton.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {

            }
        });

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
                comparateur = new ComparateurImagesPixels();
                break;
            case "Hachage (Moyenne)":
                comparateur = new ComparateurImagesHachageMoyenne();
                break;
            case "Hachage (Différences)":
                comparateur = new ComparateurImagesHachageDifference();
                break;
            case null, default:
                afficherErreur("Veuillez choisir une option...");
                break;
        }
    }

    private void ouvrirGalerie() {
        String cheminDossier = choisirDossier();
        if (cheminDossier != null) {
            // Logique pour gérer le dossier sélectionné
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
        Alert alerte = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alerte.showAndWait();
    }
}
