/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Navegacion;
import model.Problem;
import model.Session;
import model.User;


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
    private Label id_salir;
    User usuario;
    int aciertos;
    int fallos;
    @FXML
    private MenuButton id_MenuButton;
    @FXML
    private ImageView id_avatar;
    
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
        
        id_estadisticas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) newScene.getWindow();
                stage.setMaximized(false);
                stage.setWidth(700);
                stage.setHeight(440);
                    });
                });
        
        id_estadisticas.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

    
    public void pasarDatos(User u, int aciertos, int fallos) {
        usuario = u;
        this.aciertos = aciertos;
        this.fallos = fallos;
        id_MenuButton.setText(usuario.getNickName());
        id_avatar.setImage(usuario.getAvatar());
    }
    
    public void pasarDatosA(User u) {
        usuario = u;   
    }

    @FXML
    private void listaProblemas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLProblemasLista.fxml"));
        Parent root = loader.load();
        FXMLProblemasListaController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void problemasAleatorios(ActionEvent event) throws IOException {
        switchToProblema(event, -1);
        primaryStage.setResizable(true);
    }

    @FXML
    private void estadisticas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEstadisticas.fxml"));
        Parent root = loader.load();
        FXMLEstadisticasController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

/*    @FXML
    private void modificarPerfil(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModificar.fxml"));
        Parent root = loader.load();
        FXMLModificarController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    */
/*
    @FXML
    private void cerrarSesion(MouseEvent event) throws IOException {
        LocalDateTime tiempo = LocalDateTime.now();
        Session sesion = new Session(tiempo, aciertos, fallos);
        try {
            usuario.addSession(sesion);
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
        Parent root = loader.load();
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
*/
    private void salir(MouseEvent event) {
        LocalDateTime tiempo = LocalDateTime.now();
        Session sesion = new Session(tiempo, aciertos, fallos);
        try {
            usuario.addSession(sesion);
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        
        id_salir.getScene().getWindow().hide();
    }

    //esta función carga una escena con un problema específico (marcado por n)
    //SII n <= listaDeProblemas.lenth
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
        controller.pasarDatos(usuario, aciertos, fallos);
        
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        
        primaryStage.setResizable(true);
        primaryStage.show();
        
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        LocalDateTime tiempo = LocalDateTime.now();
        Session sesion = new Session(tiempo, aciertos, fallos);
        try {
            usuario.addSession(sesion);
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
        Parent root = loader.load();
        primaryStage = (Stage) id_listaProblemas.getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void modificarPerfil(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModificar.fxml"));
        Parent root = loader.load();
        FXMLModificarController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) id_listaProblemas.getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void salirEscritorio(ActionEvent event) {
        LocalDateTime tiempo = LocalDateTime.now();
        Session sesion = new Session(tiempo, aciertos, fallos);
        try {
            usuario.addSession(sesion);
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLProblemaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
}