package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;



//It would be great slide stages slowly and beautiful
//I tried
//I could not

public class ZeroController {

    //friction method
    public static void jumpToStage(Stage prevStage, String fxml) throws IOException {
        Stage stage = loadStage(fxml);
        stage.show();
        prevStage.hide();
    }


    public static void jumpToWaitingStage(String fxml) throws IOException {
        Stage stage = loadStage(fxml);
        stage.showAndWait();
    }

    public static void jumpToWaitingNoResizableStage(String fxml) throws IOException {
        Stage stage = loadStage(fxml);
        stage.setResizable(false);
        stage.showAndWait();
    }

    public static Stage loadStage(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ZeroController.class.getResource(fxml));

        loader.load();

        Parent root = loader.getRoot();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Natural language lab");
        stage.getIcons().add(new Image("/assets/imgfiles/atom.png"));
        return stage;
    }


}
