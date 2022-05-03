/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import com.sun.javafx.logging.PlatformLogger.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.abs;
import java.lang.System.Logger;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author marta
 */
public class FXMLModificarController implements Initializable {
    
   //properties to control valid fieds values. 
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalsPassword;  
    private BooleanProperty validAge;
    
    //VARIABLES PARA CAMBIAR DE ESCENA
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    //VARIABLES PARA VENTANAS EMERGENTES
    Alert alerta = new Alert(AlertType.WARNING);
    String mensaje;
    
    //Objeto Navegacion
    Navegacion navegador;
    
    //When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;
     
    
    //CAMBIAR ESCENA: parametros son el evento causante y el nombre del fichero .fxml
    private void switchToScene(ActionEvent event, String name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public void checkEmail() {
        if(!User.checkEmail(id_correo.getText())) {
            validEmail.setValue(Boolean.FALSE);
            mensaje = "El correo no es válido.";
            id_correo.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }else{
            validEmail.setValue(Boolean.TRUE);
            id_correo.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }
    }
     
    //COMPROBAR CONTRASEÑA VÁLIDA
    public void checkPassword() {
        if(!User.checkPassword(id_contraseña.getText())) {
            validPassword.setValue(Boolean.FALSE);
            mensaje = "La contraseña no es válida. La contraseña debe contener [8-20] caracteres, contener al menos una letra mayúscula, una letra minúscula, un dígito y un caracter especial [!@#$%&*()-+=]. No debe contener espacios";
            id_contraseña.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }else {
            validPassword.setValue(Boolean.TRUE);
            id_contraseña.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }
    }
    
    //COMPROBAR CONTRASEÑAS IGUALES
    public void checkEqualsPassword() {
        if("".equals(id_contraseña1.getText())){
            equalsPassword.setValue(Boolean.FALSE);
            id_contraseña1.styleProperty().setValue("-fx-background-color: #FCE5E0");
        } else if(id_contraseña.textProperty().getValueSafe().compareTo(id_contraseña1.textProperty().getValueSafe()) == EQUALS){
            equalsPassword.setValue(Boolean.TRUE);
            id_contraseña1.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }else {
            equalsPassword.setValue(Boolean.FALSE);
            mensaje = "No coincide con la contraseña. Compruebe que haya escrito la misma contraseña.";
            id_contraseña1.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait(); 
        }
    }
    
   

    @FXML
    private Button id_buttonA;
    @FXML
    private Button id_buttonC;
    @FXML
    private TextField id_nombre;
    @FXML
    private TextField id_correo;
    @FXML
    private PasswordField id_contraseña;
    @FXML
    private PasswordField id_contraseña1;
    @FXML
    private DatePicker id_FechaNacimiento;
    @FXML
    private ImageView id_imagen;
    @FXML
    private Label id_SelecImagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //inicializar Navegacion (navegador)
        try {
            navegador = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            java.util.logging.Logger.getLogger(FXMLRegistroController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //INICIALIZA LAS VENTANAS EMERGENTES -> ESTO ESTA CAUSANDO EL ERROR...
        alerta.setTitle("Datos inválidos");
        alerta.setHeaderText(null);
        
        //variables valid_
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalsPassword = new SimpleBooleanProperty();
 
        validAge = new SimpleBooleanProperty();
        
        //inicializadas a FALSE
        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        equalsPassword.setValue(Boolean.FALSE);
        validAge.setValue(Boolean.FALSE);

        //AND de todas las condiciones, la comprobacion final es sobre validFields
        BooleanBinding validFields = Bindings.and(validEmail, validPassword).and(equalsPassword).and(validAge);
                

        
        id_correo.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue){checkEmail(); 
            }});
        
        id_contraseña.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue){checkPassword(); 
            }});
        
        id_contraseña1.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue){checkEqualsPassword(); 
            }});
        
        
        //comprobacion boton aceptar
        id_buttonA.disableProperty().bind(Bindings.not(validFields));
        
    }    

    @FXML
    private void handleAcceptOnAction(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLPrincipal_1");
    }

    @FXML
    private void handleCancelOnAction(ActionEvent event) throws IOException {
         switchToScene(event, "FXMLInicio"); 
    }

    @FXML
    private void handleDate(ActionEvent event) {
       
        }   
    

    @FXML
    private void handlePressedAction(MouseEvent event) {}
         
        

}
    

