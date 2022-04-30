/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Navegacion;


/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLPrincipalController implements Initializable {

    @FXML
    private Button id_listaProblemas;
    @FXML
    private Button id_problemasAleatorios;
    @FXML
    private Button id_estadisticas;

    private Navegacion datos; //creacion del Map
    private Stage primaryStage;
    private Scene scene;
    @FXML
    private AnchorPane id_split;
    @FXML
    private Label id_salir;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            datos = Navegacion.getSingletonNavegacion();
            
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private void switchToScene(MouseEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    

    @FXML
    private void listaProblemas(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLProblemasLista");
    }

    @FXML
    private void problemasAleatorios(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLProblemaAleatorio");
        primaryStage.setResizable(true);
    }

    @FXML
    private void estadisticas(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLEstadisticas");
    }

    @FXML
    private void modificarPerfil(MouseEvent event) {
        //switchToScene(event, "FXMLModificarPerfil");
    }

    private void cerrarSesion(MouseEvent event) throws IOException {
        switchToScene(event, "FXMLInicio");
    }

    @FXML
    private void salir(MouseEvent event) {
        id_salir.getScene().getWindow().hide();
    }

}