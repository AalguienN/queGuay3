/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FXMLInicioController implements Initializable{
    
    @FXML
    private TextField id_usuario;
    @FXML
    private PasswordField id_contraseña;
    @FXML
    private Button id_iniciarSesion;
    @FXML
    private Label id_usuarioIncorrecto;
    @FXML
    private Label id_contraseñaIncorrecta;
    @FXML
    private Button id_cancelar;
    
    private Navegacion datos; //creacion del Map
    private Stage primaryStage;
    private Scene scene;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            datos = Navegacion.getSingletonNavegacion();
            
            /*String nickName = "a";
            String email = "email@domain.es";
            String password = "a";
            LocalDate birthdate = LocalDate.now().minusYears(18);
            User result = datos.registerUser(nickName, email, password, birthdate);*/
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLInicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
                id_usuario.focusedProperty().addListener((observable,oldValue,newValue) -> {
            if(!newValue){
                id_usuarioIncorrecto.visibleProperty().set(false);
            }
            
        });
    }
       //CAMBIAR ESCENA: parametros son el evento causante y el nombre del fichero .fxml
    private void switchToScene(ActionEvent event, String name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private void switchToScene(KeyEvent event, String name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    
    @FXML
    private void iniciarSesion(ActionEvent event) throws IOException {
        id_usuarioIncorrecto.visibleProperty().set(false); //siempre que le des a iniciar sesión desaparece el mensaje de error
        id_contraseñaIncorrecta.visibleProperty().set(false); //siempre que le des a iniciar sesión desaparece el mensaje de error
        if(!datos.exitsNickName(id_usuario.textProperty().getValueSafe())){ //comprueba si no existe el usuario en la base de datos y muestra el mensaje de error
           id_usuarioIncorrecto.visibleProperty().set(true); 
        }else{ // comprueba si la contraseña no coincide con el usuario y muestra el mensaje de error
            User user = datos.loginUser(id_usuario.textProperty().getValueSafe(),
            id_contraseña.textProperty().getValueSafe());
                if(user == null){
                        id_contraseñaIncorrecta.visibleProperty().set(true); 
                }else{ //si todo está bien, te envía al principal
                    switchToScene(event, "FXMLPrincipal_1   ");
                }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        id_cancelar.getScene().getWindow().hide();
    }   
    
    @FXML
    private void registrase(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLRegistro");
    }
    
    void initStage(Stage stage) {
        primaryStage = stage;
    }

    @FXML
    private void teclaEnter(KeyEvent event) throws IOException{ 
        if(event.getCode()==ENTER){
            id_usuarioIncorrecto.visibleProperty().set(false); //siempre que le des a iniciar sesión desaparece el mensaje de error
            id_contraseñaIncorrecta.visibleProperty().set(false); //siempre que le des a iniciar sesión desaparece el mensaje de error
            if(!datos.exitsNickName(id_usuario.textProperty().getValueSafe())){ //comprueba si no existe el usuario en la base de datos y muestra el mensaje de error
                id_usuarioIncorrecto.visibleProperty().set(true); 
            }else{ // comprueba si la contraseña no coincide con el usuario y muestra el mensaje de error
                User user = datos.loginUser(id_usuario.textProperty().getValueSafe(),
                id_contraseña.textProperty().getValueSafe());
                    if(user == null){
                        id_contraseñaIncorrecta.visibleProperty().set(true); 
                    }else{ //si todo está bien, te envía al principal
                        switchToScene(event, "FXMLPrincipal_1");
                    }
            }
        }
    }
}