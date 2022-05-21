/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.paint.Color;
import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Answer;
import model.Navegacion;
import model.Problem;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import model.Session;
import model.User;



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
    
    private List<Integer> intList;
    
    private Problem problemaActual;
    private List<Answer> respuestasList;
    
    private Stage primaryStage;
    private Scene scene;
    
    //Booleanos
    private BooleanProperty respuestaSeleccionada;
    
    @FXML
    private Button id_confirmRepsButton;
    
    private Line linePainting;
    private Circle circlePainting;
    
    //Para pintar
    private double iniX;
    private double iniY;
    @FXML
    private Pane mapaPane;
    @FXML
    private ImageView MapaScrollPaneID;
    
    
    @FXML
    private ToggleGroup ToolBar;
    @FXML
    private ToggleButton ToggPuntoID1;
    @FXML
    private ToggleButton ToggBLapizID;
    @FXML
    private ToggleButton ToggCompID;

    User usuario;
    int aciertos;
    int fallos;
    
    List<Object> dibList;
    @FXML
    private ToggleButton ToggTextID;
    @FXML
    private ToggleButton ToggAngID;
    @FXML
    private ToggleButton ToggGomaID;
    @FXML
    private ColorPicker ColorPickerID;
    @FXML
    private TextField TamLineaFieldID;
    @FXML
    private Circle circuloMuestraID;
    @FXML
    private TextField TamFuenteFieldID;
    @FXML
    private Label LabelMuestraID;
    
    private int tamanoLinea;
    private int tamanoFuente;
    
    private boolean mouseOnClick;
    @FXML
    private ImageView angulosID;
    @FXML
    private ToggleButton ToggMovID;
    
    private boolean compasInteractable;
    @FXML
    private ToggleButton ToggCursorID;
    
    private String formaPunto;
    @FXML
    private MenuButton FormaPuntoID;
    
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
        
        intList = new ArrayList<Integer>();
        
        for (int i = 0; i<respuestasList.size(); i++) intList.add(i);     
        
        System.out.println(intList.toString());
        Collections.shuffle(intList); 
        System.out.println(intList.toString());
        
        id_respuesta1.setText(respuestasList.get(intList.get(0)).getText());
        id_respuesta2.setText(respuestasList.get(intList.get(1)).getText());
        id_respuesta3.setText(respuestasList.get(intList.get(2)).getText());
        id_respuesta4.setText(respuestasList.get(intList.get(3)).getText());  
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
        
        
        //Pintar - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //Listeners
        respuestaSeleccionada = new SimpleBooleanProperty();
        respuestaSeleccionada.setValue(Boolean.FALSE);
        //Binding botón respuestas
        id_confirmRepsButton.disableProperty().bind(respuestaSeleccionada.not());
        
        angulosID.visibleProperty().bind(ToggAngID.selectedProperty());
        //Lista de objetos de pintura
        dibList = new ArrayList<Object>();
        
        
        map_scrollpane.pannableProperty().bind(ToggMovID.selectedProperty());
        
        compasInteractable = false;
        angulosID.setOnMouseDragged(e->{
           if (ToggAngID.selectedProperty().getValue()){
            angulosID.setX(e.getX());
            angulosID.setY(e.getY());
           }
        });
        
        angulosID.disableProperty().bind(ToggCursorID.selectedProperty().not());
        
        
        //map_scrollpane.setPannable(false);
        
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
        
        
        id_EnunciadoProblema.sceneProperty().addListener((obs, oldScene, newScene) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(e -> {
                    LocalDateTime tiempo = LocalDateTime.now();
                    Session sesion = new Session(tiempo, aciertos, fallos);
                    try {
                        usuario.addSession(sesion);
                    } catch (NavegacionDAOException ex) {
                        Logger.getLogger(FXMLProblemaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Platform.exit();
                    System.exit(0);
                    });
                });
            });

        try{   
            tamanoFuente = Integer.parseInt(TamFuenteFieldID.getText());
        } catch (final NumberFormatException e){}
        try{   
            tamanoLinea = Integer.parseInt(TamLineaFieldID.getText());
        } catch (final NumberFormatException e){}
    }
    @FXML
    private void muestraPosicion(MouseEvent event) {
        posicion.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
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
        id_respuesta1.disableProperty().setValue(Boolean.TRUE);
        id_respuesta2.disableProperty().setValue(Boolean.TRUE);
        id_respuesta3.disableProperty().setValue(Boolean.TRUE);
        id_respuesta4.disableProperty().setValue(Boolean.TRUE);
        
        if(compararRespuesta()) {
            aciertos++;
            System.out.println("Correcto!");
        } else {
            fallos++;
            System.out.println("Falso :(");
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) throws IOException {
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
    

    @FXML
    private void respuestaSeleccionada(ActionEvent event) {
        respuestaSeleccionada.setValue(Boolean.TRUE);
    }
    
    public void pasarDatos(User u, int aciertos, int fallos) {
        usuario = u;
        this.aciertos = aciertos;
        this.fallos = fallos;
    }
    
    //Está feo pero está
    private boolean compararRespuesta(){
        if(id_respuesta1.isSelected() == problemaActual.getAnswers().get(intList.get(0)).getValidity()
        && id_respuesta2.isSelected() == problemaActual.getAnswers().get(intList.get(1)).getValidity()
        && id_respuesta3.isSelected() == problemaActual.getAnswers().get(intList.get(2)).getValidity()
        && id_respuesta4.isSelected() == problemaActual.getAnswers().get(intList.get(3)).getValidity())
            return true;
        
        return false;
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


    //Zona de pintura
    @FXML
    private void MdragEnMapa(MouseEvent event) {
        //Línea
        if (ToggBLapizID.selectedProperty().getValue()){
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
        }
        //Punto
        if (ToggCompID.selectedProperty().getValue()){
            double actX = event.getX()-iniX;
            double actY = event.getY()-iniY;
            double radio = Math.sqrt(actX*actX + actY*actY);
            circlePainting.setRadius(radio);
            
            event.consume();
            
        }
        mouseOnClick = true;
}

    @FXML
    private void MReleaseEnMapa(MouseEvent event) {
        mouseOnClick = false;
        //System.out.println(mouseOnClick);
    }

    @FXML
    private void MclickEnMapa(MouseEvent event) {
        mouseOnClick = true;
        
        //System.out.println(mouseOnClick);
        //Línea
        if (ToggBLapizID.selectedProperty().getValue()){
            linePainting = new Line(event.getX(), event.getY(),event.getX(),event.getY());
            linePainting.setStroke(ColorPickerID.getValue());
            mapaPane.getChildren().add(linePainting);
            dibList.add(linePainting);
            linePainting.setStrokeWidth(tamanoLinea);
            
            linePainting.setOnMouseEntered(e->{
                //System.out.println(mouseOnClick);
                
                if (ToggGomaID.selectedProperty().getValue())
                    mapaPane.getChildren().remove((Node)e.getSource());

            });
            
            linePainting.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem borrarItem = new MenuItem("eliminar");
                menuContext.getItems().add(borrarItem);
                borrarItem.setOnAction(ev -> {
                    mapaPane.getChildren().remove((Node)e.getSource());
                    ev.consume();
                });
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                menuContext.setX(e.getX());
                menuContext.setY(e.getY());
                menuContext.show(linePainting,  stage.getX()+e.getSceneX(), stage.getY()+e.getSceneY());
            });
        }
        //Compás
        if (ToggCompID.selectedProperty().getValue()){
            circlePainting = new Circle(1);
            circlePainting.setFill(Color.TRANSPARENT);
            circlePainting.setStroke(ColorPickerID.getValue());
            circlePainting.setStrokeWidth(tamanoLinea);
            
            mapaPane.getChildren().add(circlePainting);
            dibList.add(circlePainting);
            
            circlePainting.setCenterX(event.getX());
            circlePainting.setCenterY(event.getY());
            iniX = event.getX();
            iniY = event.getY();
            
            circlePainting.setOnMouseEntered(e->{
                //System.out.println(mouseOnClick);
                
                if (ToggGomaID.selectedProperty().getValue())
                    mapaPane.getChildren().remove((Node)e.getSource());

            });
            
            circlePainting.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem borrarItem = new MenuItem("eliminar");
                menuContext.getItems().add(borrarItem);
                borrarItem.setOnAction(ev -> {
                    mapaPane.getChildren().remove((Node)e.getSource());
                    ev.consume();
                });
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                menuContext.setX(e.getX());
                menuContext.setY(e.getY());
                menuContext.show((Node)e.getSource(),  stage.getX()+e.getSceneX(), stage.getY()+e.getSceneY());
            });
        }
        //Punto
        if(ToggPuntoID1.selectedProperty().getValue()){
            Shape pin = new Rectangle();
            if (formaPunto == "Cuadrado"){
                Rectangle auxPin = new Rectangle();
                
                auxPin.setHeight(4);
                auxPin.setWidth(4);
                auxPin.setX(event.getX()-2);
                auxPin.setY(event.getY()-2);
                
                pin = auxPin;
            }
            else {
                Circle auxPin = new Circle();
                
                auxPin.setRadius(2);
                auxPin.setCenterX(event.getX());
                auxPin.setCenterY(event.getY());
                
                pin = auxPin;
            }
            
            pin.setStroke(ColorPickerID.getValue());
            pin.setFill(Color.TRANSPARENT);

            pin.getStyleClass().clear();
            mapaPane.getChildren().add(pin);
            dibList.add(pin);
            
            pin.setOnMouseEntered(e->{
                //System.out.println(mouseOnClick);
                
                if (ToggGomaID.selectedProperty().getValue())
                    mapaPane.getChildren().remove((Node)e.getSource());

            });
            
            pin.setOnContextMenuRequested(e -> {
                ContextMenu menuContext = new ContextMenu();
                MenuItem borrarItem = new MenuItem("eliminar");
                menuContext.getItems().add(borrarItem);
                borrarItem.setOnAction(ev -> {
                    mapaPane.getChildren().remove((Node)e.getSource());
                    ev.consume();
                });
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                menuContext.setX(e.getX());
                menuContext.setY(e.getY());
                menuContext.show((Node)e.getSource(),  stage.getX()+e.getSceneX(), stage.getY()+e.getSceneY());
            });
        }  
        
        //texto
        if (ToggTextID.selectedProperty().getValue()){
            TextField texto = new TextField();
            
            mapaPane.getChildren().add(texto);
            texto.setLayoutX(event.getX());
            texto.setLayoutY(event.getY());
            texto.requestFocus();
            
            texto.setOnAction(e->{
                Text textoT = new Text (texto.getText());
                textoT.setX(texto.getLayoutX());
                textoT.setY(texto.getLayoutY());
                textoT.setStyle("-fx-font-family:Gafata; -fx-font-size: "+tamanoFuente+";");
                textoT.setFill(ColorPickerID.getValue());
                mapaPane.getChildren().add(textoT);
                mapaPane.getChildren().remove(texto);
                e.consume();
            });
            
            ToggTextID.selectedProperty().set(false);
        }
    }

    @FXML
    private void clearMap(ActionEvent event) {
        for (int i = 0; i<dibList.size(); i++)
            mapaPane.getChildren().remove(dibList.get(i));
        
        for (int i = 0; i<dibList.size(); i++)
            dibList.remove(dibList.get(i));
    }

    @FXML
    private void grosorLineaAct(ActionEvent event) {
        int num = 1;
        try{        
            num = Integer.parseInt(TamLineaFieldID.getText());
        } catch (final NumberFormatException e){}
        if (num < 3)
            num = 3;
        if (num > 20)
            num = 20;
        
        tamanoLinea = num;
        TamLineaFieldID.setText(""+num);
        
        circuloMuestraID.setRadius(num);
        
    }

    @FXML
    private void tamanoFuenteAct(ActionEvent event) {
        int num = 1;
        try{   
            num = Integer.parseInt(TamFuenteFieldID.getText());
        } catch (final NumberFormatException e){}
        if (num < 5)
            num = 5;
        if (num > 100)
            num = 100;
        
        
        tamanoFuente = num;
        TamFuenteFieldID.setText(""+num);
        LabelMuestraID.setFont(new Font(LabelMuestraID.getFont().getName(), num));
    }

    @FXML
    private void angulosDragAct(MouseEvent event) {
    }

    @FXML
    private void cursorAct(ActionEvent event) {
        ToggCursorID.selectedProperty().setValue(Boolean.TRUE);
    }

    @FXML
    private void FormaPuntoAct(ActionEvent event) {
        
    }

    @FXML
    private void FormaPuntoCirculo(ActionEvent event) {
        FormaPuntoID.setText("Circulo");
        formaPunto = "Circulo";
    }

    @FXML
    private void FormaPuntoCuadrado(ActionEvent event) {
        FormaPuntoID.setText("Cuadrado");
        formaPunto = "Cuadrado";
    }

    

}
