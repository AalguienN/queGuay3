/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author marta
 */
public class FXMLProblemasListaController implements Initializable {
    
    private Stage stage;
    private Scene primaryScene;
    private Parent root;

    Navegacion navegador;
    
    @FXML
    private TitledPane id_desp1;
    @FXML
    private Label id_lab1;
    @FXML
    private TitledPane id_desp2;
    @FXML
    private Label id_lab2;
    @FXML
    private TitledPane id_desp3;
    @FXML
    private Label id_lab3;
    @FXML
    private TitledPane id_desp4;
    @FXML
    private Label id_lab4;
    
    
    //CAMBIAR ESCENA: parametros son el evento causante y el nombre del fichero .fxml
    public void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        primaryScene = new Scene(root);
        stage.setScene(primaryScene);
        stage.setResizable(false);
        stage.show();
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            navegador = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemasListaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        List<Problem> lista = navegador.getProblems();

        
        id_lab1.setText((lista.get(0)).getText());
        id_lab2.setText((lista.get(1)).getText());
        id_lab3.setText((lista.get(2)).getText());
        id_lab4.setText((lista.get(3)).getText());
        
    }    

    @FXML
    private void handleMenu(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLPrincipal");
    }

    @FXML
    private void handleProblemaAleatorio(ActionEvent event) throws IOException {
        switchToProblema(event, -1);
        
    }

    @FXML
    private void handleBoton1(ActionEvent event) throws IOException {
        switchToProblema(event,0);
    }

    @FXML
    private void handleBoton2(ActionEvent event) throws IOException {
        switchToProblema(event,1);
    }

    @FXML
    private void handleBoton3(ActionEvent event) throws IOException {
        switchToProblema(event,2);
    }

    @FXML
    private void handleBoton4(ActionEvent event) throws IOException {
        switchToProblema(event,3);
    }
    

    //esta función carga una escena con un problema específico (marcado por n)
    //SII n <= listaDeProblemas.lenth
    private void switchToProblema(ActionEvent event, int n) throws IOException{
        //switchToScene(event, "FXMLProblema");
        
        List<Problem> lista = navegador.getProblems();
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLProblema.fxml"));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        FXMLProblemaController controller = loader.getController();
        //esta línea es para pasar referencia del problema concreto que vamos a realizar
        //Estamos pasando información entre escenas!!!!
        controller.setProblemaActual(n);
        
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        
    }
}
