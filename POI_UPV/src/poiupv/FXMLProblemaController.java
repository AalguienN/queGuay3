/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Answer;
import model.Navegacion;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author Adrián
 */
public class FXMLProblemaController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private Label posicion;
    @FXML
    private ToggleGroup Respuesta;
    
    private Navegacion navegador;
    @FXML
    private Label id_TituloProblema;
    @FXML
    private Label id_EnunciadoProblema;
    @FXML
    private RadioButton id_respuesta1;
    @FXML
    private RadioButton id_respuesta2;
    @FXML
    private RadioButton id_respuesta3;
    @FXML
    private RadioButton id_respuesta4;
    
    
    
    private Problem problemaActual;
    private List<Answer> respuestasList;
    
    private Stage primaryStage;
    
    private BooleanProperty respuestaSeleccionada;
    @FXML
    private Button id_confirmRepsButton;
    
    //SII problema >= 0 saca problema de la lista
    //SII problema = -1 problema aleatorio
    public void setProblemaActual(int problema){
        Random rand = new Random();
        try {
            navegador = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemasListaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        List<Problem> lista = navegador.getProblems();
        if (problema == -1)
            problema = rand.nextInt(lista.size());
        this.problemaActual = lista.get(problema);
        id_TituloProblema.setText("Problema "+(problema+1));
        
        id_EnunciadoProblema.setText((problemaActual).getText());
       
        respuestasList = problemaActual.getAnswers();
        id_respuesta1.setText(respuestasList.get(0).getText());
        id_respuesta2.setText(respuestasList.get(1).getText());
        id_respuesta3.setText(respuestasList.get(2).getText());
        id_respuesta4.setText(respuestasList.get(3).getText());  
    }
    @FXML
    void zoomIn(MouseEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(MouseEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la posicion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la posicion del POI
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    private void initData() {
        hm.put("2F", new Poi("2F", "Edificion del DSIC", 325, 225));
        hm.put("Agora", new Poi("Agora", "Agora", 600, 360));
        map_listview.getItems().add(hm.get("2F"));
        map_listview.getItems().add(hm.get("Agora"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
            
        //Navegador
        try {
            navegador = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemasListaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.1);
        zoom_slider.setMax(1);
        zoom_slider.setValue(0.5);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        List<Problem> lista = navegador.getProblems();

        //En el caso de que no fuera cargado un problema anteriormente
        //if(problemaActual == null) problemaActual = lista.get(0);
        
        respuestaSeleccionada = new SimpleBooleanProperty();
        respuestaSeleccionada.setValue(Boolean.FALSE);
        //Binding botón respuestas
        id_confirmRepsButton.disableProperty().bind(respuestaSeleccionada.not());
        
        

    }

    private void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void cerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }

    private void acercaDe(ActionEvent event) {
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

    @FXML
    private void BorrarSeleccion(ActionEvent event) {
        id_respuesta1.setSelected(false);
        id_respuesta2.setSelected(false);
        id_respuesta3.setSelected(false);
        id_respuesta4.setSelected(false);
        
        respuestaSeleccionada.setValue(Boolean.FALSE);
        
        
    }

    @FXML
    private void ConfirmarRespuesta(ActionEvent event) {
        if(compararRespuesta()) System.out.println("Correcto!");
        else System.out.println("Falso :(");
    }

    @FXML
    private void volverMenu(ActionEvent event) throws IOException {
        switchToScene(event,"FXMLPrincipal_1");
    }
    
    //Está feo pero está
    private boolean compararRespuesta(){
        if(id_respuesta1.isSelected() == problemaActual.getAnswers().get(0).getValidity()
        && id_respuesta2.isSelected() == problemaActual.getAnswers().get(1).getValidity()
        && id_respuesta3.isSelected() == problemaActual.getAnswers().get(2).getValidity()
        && id_respuesta4.isSelected() == problemaActual.getAnswers().get(3).getValidity())
            return true;
        
        return false;
    }

    @FXML
    private void respuestaSeleccionada(ActionEvent event) {
        respuestaSeleccionada.setValue(Boolean.TRUE);
    }
}
