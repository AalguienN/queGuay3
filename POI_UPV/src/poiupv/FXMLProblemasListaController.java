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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author marta
 */
public class FXMLProblemasListaController implements Initializable {
    
    private Stage stage;
    private Scene primaryScene;
    private Parent root;
    
    private double valorHB;
    private int valor;
    
    Navegacion navegador;
    
    @FXML
    private VBox id_vBox;
    @FXML
    private AnchorPane id_AnchorPane;
    User usuario;
    int aciertos;
    int fallos;
    
    
    
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

        
        int talla = lista.size();
        
        //AQUI SE CREAN LOS TITLEDPANE
        for(int i = 0;i < talla; i++){
            int j = i;
            //crean los elementos a incluir
            TitledPane titledPane = new TitledPane();
            Label etiqueta = new Label();
            
            //para que el texto quede más ajustado al titledpane
            valor = lista.get(i).getText().length();
            
            //propiedades del boton
            Button boton = new Button("Hacer Problema");
            boton.setPrefWidth(200);
            boton.setAlignment(Pos.CENTER);
            
            boton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    
                    try {
                        switchToProblema(event, j); 
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(FXMLProblemasListaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    
                }
            
            });
            
            //propiedades de la etiqueta
            etiqueta.setPrefWidth(400);
            etiqueta.setPrefHeight(valor);
            etiqueta.setTextAlignment(TextAlignment.JUSTIFY);
            etiqueta.setWrapText(true);
            etiqueta.setText(lista.get(i).getText());
            
            //propiedades del titledpane
            titledPane.setText("Problema " + (i+1));

            //se crea un vbox dentro del titlepane
            VBox content = new VBox();
            
            //características del vbox
            if(valor >= 300) {content.setPrefHeight(valor/2);}
            
            //para que el texto quede más ajustado al titledpane
            else if(valor > 200){content.setPrefHeight(valor/1.5);}
            else {content.setPrefHeight(valor);}
            content.setPrefWidth(100);
            content.setAlignment(Pos.TOP_LEFT);
            
            //se meten etiqueta y boton dentro del vbox
            content.getChildren().add(etiqueta);
            content.getChildren().add(boton);
            content.setMargin(boton, new Insets(0,0,0,65)); //para margenes

            //se incluye el vbox dentro del titledpane
            titledPane.setContent(content);
            
            //mas propiedades del titledpane
            titledPane.setPrefWidth(360);
            titledPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
            titledPane.setExpanded(true);
            titledPane.setAnimated(true);
            titledPane.setExpanded(false);
            
            //se vincula el vbox del anchorPane y el titledPane
            VBox root= id_vBox;
            
            //para que el texto quede más ajustado al titledpane
            valorHB = root.getPrefHeight() + valor/1.3;
            
            root.setPrefHeight(valorHB);
            id_AnchorPane.setPrefHeight(valorHB);
            VBox.setMargin(titledPane, new Insets(10, 25, 10, 10));
            root.getChildren().add(titledPane);

        
                }
        
            id_vBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
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
        
        
    }    

    @FXML
    private void handleMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
        Parent root = loader.load();
        FXMLPrincipalController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void handleProblemaAleatorio(ActionEvent event) throws IOException {
        switchToProblema(event, -1);
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
        controller.pasarDatos(usuario, aciertos, fallos);
        
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setFullScreen(!stage.isFullScreen());
        stage.show();
        
    }
    
     public void pasarDatos(User u, int aciertos, int fallos) {
        usuario = u;
        this.aciertos = aciertos;
        this.fallos = fallos;
    }
    
}
