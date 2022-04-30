/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLInicio.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Iniciar Sesión");
        stage.setScene(scene);
        FXMLInicioController inicio = loader.getController();
        inicio.initStage(stage);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
