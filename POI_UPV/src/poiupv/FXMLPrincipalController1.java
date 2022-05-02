/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;


/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLPrincipalController1 implements Initializable {

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
    @FXML
    private MenuButton id_MenuButton;
    @FXML
    private MenuItem id_cierre;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            datos = Navegacion.getSingletonNavegacion();
            
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLPrincipalController1.class.getName()).log(Level.SEVERE, null, ex);
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
        switchToProblema(event, -1);
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


    @FXML
    private void salir(MouseEvent event) {
        id_salir.getScene().getWindow().hide();
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLRegistro.fxml"));
        primaryStage = (Stage)id_MenuButton.getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private void switchToProblema(ActionEvent event, int n) throws IOException{
        //switchToScene(event, "FXMLProblema");
        
        List<Problem> lista = datos.getProblems();
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLProblema.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        FXMLProblemaController controller = loader.getController();
        //esta línea es para pasar referencia del problema concreto que vamos a realizar
        //Estamos pasando información entre escenas!!!!
        controller.setProblemaActual(n);
        
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
        
    }
}