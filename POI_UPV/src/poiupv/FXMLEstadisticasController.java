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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import java.util.Set;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DateCell;
import javafx.scene.text.Text;
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
    Map<LocalDate, Auxiliar> mapa = new HashMap<LocalDate, Auxiliar>();
    List<Session>  lista;
    List<LocalDate> listaFechas = new ArrayList();
    int HITS;
    int FAULTS;
    @FXML
    private Text id_porcentajeAcierto2;
    
    
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
			this.setStyle("-fx-background-color: pink");}}
              };
            };
          }});

        id_checkFecha.disableProperty().set(true);
        
        id_fechaFiltrar.valueProperty().addListener((o, oldV, newV) -> {
                if(newV!=null){
                    id_checkFecha.disableProperty().set(false);
                    
                if(id_checkFecha.isSelected()) {
                    id_barChar.getData().clear();
                LocalDate dia = id_fechaFiltrar.getValue();
                int aciertosAux = 0;
                int fallosAux = 0;

                XYChart.Series datos = new XYChart.Series();
                datos.setName(dia.toString());

                id_porcentajeAcierto1.setText("Sesion del: " + dia.toString());
                id_porcentajeAcierto2.setText("Porcentaje aciertos de sesion:");
                if(mapa.containsKey(dia)) {
                    Auxiliar aux = mapa.get(dia);
                    aciertosAux = aux.getAciertos();
                    fallosAux = aux.getFallos();

                    datos.getData().add(new XYChart.Data("ACIERTOS",aciertosAux));
                    datos.getData().add(new XYChart.Data("FALLOS",fallosAux));
                } else {
                    datos.getData().add(new XYChart.Data("ACIERTOS",0));
                    datos.getData().add(new XYChart.Data("FALLOS",0));
                }    

                id_barChar.getData().addAll(datos);

                if((aciertosAux+fallosAux) == 0) {id_porcentajeAcierto.setText("0%");
                } else {
                double valor = Math.round((double)aciertosAux/(aciertosAux+fallosAux) * 100);
                id_porcentajeAcierto.setText(Double.toString(valor)+ "%");
                }
                }}});
        
        id_checkFecha.selectedProperty().addListener((o, oldV, newV) -> {
                if(!id_checkFecha.isSelected()) {printChar();}
        });
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
    
    private void printChar() {
        LocalDate dia = LocalDate.now();
        
        XYChart.Series aciertos = new XYChart.Series();
        aciertos.setName("Aciertos");
        XYChart.Series fallos = new XYChart.Series();
        fallos.setName("Fallos");

        for(int j = 6; j >= 0; j--) {
            if(mapa.containsKey(dia)) {
                Auxiliar aux = mapa.get(dia);
                aciertos.getData().add(new XYChart.Data(dia.toString(),aux.getAciertos()));
                fallos.getData().add(new XYChart.Data(dia.toString(),aux.getFallos()));
            } else {
                aciertos.getData().add(new XYChart.Data(dia.toString(),0));
                fallos.getData().add(new XYChart.Data(dia.toString(),0));
            }
            dia = dia.minusDays(1);
        }
        
        id_barChar.getData().addAll(aciertos);
        id_barChar.getData().addAll(fallos);
    }
    
    @FXML
    private void filtrarFecha(ActionEvent event) {
            id_barChar.getData().clear();
            LocalDate dia = id_fechaFiltrar.getValue();
            int aciertosAux = 0;
            int fallosAux = 0;
            
            XYChart.Series datos = new XYChart.Series();
            datos.setName(dia.toString());
            
            id_porcentajeAcierto1.setText("Sesion del: " + dia.toString());
            id_porcentajeAcierto2.setText("Porcentaje aciertos de sesion:");
            if(mapa.containsKey(dia)) {
                Auxiliar aux = mapa.get(dia);
                aciertosAux = aux.getAciertos();
                fallosAux = aux.getFallos();
                
                datos.getData().add(new XYChart.Data("ACIERTOS",aciertosAux));
                datos.getData().add(new XYChart.Data("FALLOS",fallosAux));
            } else {
                datos.getData().add(new XYChart.Data("ACIERTOS",0));
                datos.getData().add(new XYChart.Data("FALLOS",0));
            }    
            
            id_barChar.getData().addAll(datos);
            
            if((aciertosAux+fallosAux) == 0) {id_porcentajeAcierto.setText("0%");
            } else {
            double valor = Math.round((double)aciertosAux/(aciertosAux+fallosAux) * 100);
            id_porcentajeAcierto.setText(Double.toString(valor)+ "%");
        }
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
            
            Auxiliar aux = mapa.get(sesion.getLocalDate());
            
            if(aux == null) {mapa.put(sesion.getLocalDate(), new Auxiliar(sesion.getHits(), sesion.getFaults()));
            }else {
                aux.setAciertos(aux.getAciertos()+ sesion.getHits());
                aux.setFallos(aux.getFallos()+ sesion.getFaults());
                mapa.put(sesion.getLocalDate(), aux);
            }
            
        }
        HITS += aciertos;
        FAULTS += fallos;
        
        if((HITS+FAULTS) == 0) {id_porcentajeAcierto.setText("0%");
        } else {
        double valor = Math.round((double)HITS/(HITS+FAULTS) * 100);
        id_porcentajeAcierto.setText(Double.toString(valor)+ "%");
        }
        
        printChar();
        
    }
    
    
    
}
