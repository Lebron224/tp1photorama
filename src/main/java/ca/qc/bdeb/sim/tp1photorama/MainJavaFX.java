package ca.qc.bdeb.sim.tp1photorama;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application {
    private Gallerie gallerieImages;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Stage) {
        var root = new BorderPane();
        var scene = new Scene(root,900,600);

        var vbox = new VBox();

        var logo = new Image("logo.png");
        var polaroid = new Image("polaroid.png");

        var viewLogo = new ImageView(logo);
        viewLogo.setPreserveRatio(true);
        viewLogo.setFitHeight(50);
        var viewPolaroid = new ImageView(polaroid);

        var hbox = new HBox();

        Text titre = new Text("Photorama"); titre.setFont(Font.font(30));

        hbox.getChildren().add(viewLogo); hbox.getChildren().add(titre);
        vbox.getChildren().add(hbox);

        vbox.setSpacing(15);
        var texte = new Text("Ouvrir une gallerie"); texte.setFont(Font.font(20));
        var texte2 = new Text("Detection de doublons:");
        vbox.getChildren().add(texte); vbox.getChildren().add(texte2);

        var choix = new ChoiceBox<>();
        vbox.getChildren().add(choix);

        var texte3 = new Text("Tolérance aux différences:");
        vbox.getChildren().add(texte3);

        var hbox2 = new HBox();

        var toleFaible = new CheckBox("Faible");
        var toleEleve = new CheckBox("Élevée");
        hbox2.getChildren().add(toleFaible); hbox2.getChildren().add(toleEleve);
        vbox.getChildren().add(hbox2);

        var boutton = new Button("Ouvrir un dossier...");
        vbox.getChildren().add(boutton);

        root.setLeft(vbox); root.setLayoutX(20); root.setLayoutY(30);





        Stage.setTitle("Photorama");
        Stage.setScene(scene);
        Stage.show();
    }
}
