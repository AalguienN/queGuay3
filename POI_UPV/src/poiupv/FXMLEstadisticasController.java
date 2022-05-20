/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DateCell;
import javafx.util.Callback;
import model.Session;
import model.User;

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
    
    User usuario;
    int aciertos;
    int fallos;
    List<Session>  lista;
    List<LocalDate> listaFechas = new ArrayList();
    int HITS;
    int FAULTS;
    
    
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
        
        id_porcentajeAcierto1.sceneProperty().addListener((obs, oldScene, newScene) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(e -> {
                    LocalDateTime tiempo = LocalDateTime.now();
                    Session sesion = new Session(tiempo, aciertos, fallos);
                    try {
                        usuario.addSession(sesion);
                    } catch (NavegacionDAOException ex) {
                        java.util.logging.Logger.getLogger(FXMLProblemaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    Platform.exit();
                    System.exit(0);
                        });
                    });
                });
        
        //Colores para las fechas en las que ha habido sesion
        id_fechaFiltrar.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
              return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                  super.updateItem(item, empty);
                  
                  if (!listaFechas.isEmpty()) {
			if(listaFechas.contains(item)) {
			this.setStyle("-fx-background-color: yellow");}}
              };
            };
          }});

            
        
        
    }   
    
    

    @FXML
    private void volverMenuPrincipal(ActionEvent event) throws IOException {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
                    Parent root = loader.load();
                    FXMLPrincipalController controlador = loader.getController();
                    controlador.pasarDatos(usuario, aciertos, fallos);
                    primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.setResizable(false);
                    primaryStage.show();

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
    
    public void pasarDatos(User u, int aciertos, int fallos) {
        usuario = u;
        this.aciertos = aciertos;
        this.fallos = fallos;
        
        lista = usuario.getSessions();
        
        for(int i = 0; i < lista.size();i++) {
            Session sesion = lista.get(i);
            HITS += sesion.getHits();
            FAULTS += sesion.getFaults();
            listaFechas.add(sesion.getLocalDate());
        }
        HITS += aciertos;
        FAULTS += fallos;

        System.out.println(HITS + " " + FAULTS);
        System.out.println();
        
        if((HITS+FAULTS) == 0) {id_porcentajeAcierto.setText("0%");
        } else {
        double valor = Math.round((double)HITS/(HITS+FAULTS) * 100);
        id_porcentajeAcierto.setText(Double.toString(valor));
        }
    }
    
    
    
}
