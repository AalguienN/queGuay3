/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Navegacion;
import poiupv.Poi;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLProblemaAleatorioController implements Initializable {

    @FXML
    private Slider id_zoomSlider;
    private Group zoomGroup;
    @FXML
    private ScrollPane id_scrollPane;
    
    private Navegacion datos; //creacion del Map
    private Stage primaryStage;
    private Scene scene;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        id_zoomSlider.setMin(0.5);
        id_zoomSlider.setMax(1.5);
        id_zoomSlider.setValue(1.0);
        id_zoomSlider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(id_scrollPane.getContent());
        id_scrollPane.setContent(contentGroup);
    }    

    @FXML
    private void zoomIn(MouseEvent event) {
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = id_zoomSlider.getValue();
        id_zoomSlider.setValue(sliderVal += 0.1);
    }
    @FXML
    private void zoomOut(MouseEvent event) {
        double sliderVal = id_zoomSlider.getValue();
        id_zoomSlider.setValue(sliderVal + -0.1);
    }
    
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = id_scrollPane.getHvalue();
        double scrollV = id_scrollPane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        id_scrollPane.setHvalue(scrollH);
        id_scrollPane.setVvalue(scrollV);
    }
    
    private void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void irAMenu(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLPrincipal");
    }

    @FXML
    private void irAListaProblemas(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLProblemasLista");
    }
    
    
}
