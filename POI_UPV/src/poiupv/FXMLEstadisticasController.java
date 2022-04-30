/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Navegacion;
import java.util.Random; //Borrar en el futuro
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * FXML Controller class
 *
 * @author Adrián
 */
public class FXMLEstadisticasController implements Initializable {

    private Navegacion datos;
    private Stage primaryStage;
    private Scene scene;
    @FXML
    private Button id_volverMenuPrincipalButton;
    @FXML
    private Label id_porcentajeAcierto;
    @FXML
    private CheckBox id_checkFecha;
    @FXML
    private DatePicker id_fechaFiltrar;
    @FXML
    private BarChart<?, ?> id_barChar;
    @FXML
    private Label id_porcentajeAcierto1;
    @FXML
    private NumberAxis id_yAxis;
    @FXML
    private CategoryAxis id_xAxis;
    
    private LocalDate actual;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            datos = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        printChar();
    }    

    public void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    private void volverMenuPrincipal(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLPrincipal");

    }
    
    private void printChar(){
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Porcentaje de acierto");
                
        actual = LocalDate.now();
        Random rand = new Random();
        for (int i = 6; i >= 0; i--){
            int randInt = rand.nextInt(100); 
            LocalDate dia = actual;
            dia = dia.minusDays(i);
            series1.getData().add(new XYChart.Data(dia.toString(),randInt));
        }
        id_barChar.getData().addAll(series1);
    }

    @FXML
    private void filtrarFecha(ActionEvent event) {
            id_barChar.getData().clear();
            printChar();
    }
    
    
}
