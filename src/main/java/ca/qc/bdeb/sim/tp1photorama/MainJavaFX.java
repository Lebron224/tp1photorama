package ca.qc.bdeb.sim.tp1photorama;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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



        Stage.setTitle("Photorama");
        Stage.setScene(scene);
        Stage.show();
    }
}
