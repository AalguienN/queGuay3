/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import DBAccess.NavegacionDAOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.abs;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Navegacion;
import model.User;

/**
 * FXML Controller class
 *
 * @author varios
 */
public class FXMLRegistroController implements Initializable {
    
    //properties to control valid fieds values. 
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty equalsPassword;  
    private BooleanProperty validName;
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
    private Label id_ErrorEdad;
    @FXML
    private ImageView id_imagen;
    @FXML
    private Label id_SelecImagen;
    
   //CAMBIAR ESCENA: parametros son el evento causante y el nombre del fichero .fxml
    private void switchToScene(ActionEvent event, String name) throws IOException {
  
        Parent root = FXMLLoader.load(getClass().getResource(name+".fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //COMPROBACION EMAIL VÁLIDO...[¿COMPROBAR REPETIDO?]
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
    
    //COMPROBAR NOMBRE VÁLIDO Y NO REPETIDO
    public void checkName() {
        if(navegador.exitsNickName(id_nombre.getText()) && !User.checkNickName(id_nombre.getText())) {
            validName.setValue(Boolean.FALSE);
            mensaje = "El nombre de usuario no está disponible o no es válido. El nombre debe contener [6-15] caracteres, letras mayúsculas, minúsculas o guiones '-' y '_'";
            id_nombre.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        } else if ("".equals(id_nombre.getText())) {
            validName.setValue(Boolean.FALSE);
            id_nombre.styleProperty().setValue("-fx-background-color: #FCE5E0");
        } else {
            validName.setValue(Boolean.TRUE);
            id_nombre.styleProperty().setValue("-fx-background-color: #CDFFD0");
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
    
    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        
        
        
        //inicializar Navegacion (navegador)
        try {
            navegador = Navegacion.getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(FXMLRegistroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //INICIALIZA LAS VENTANAS EMERGENTES -> ESTO ESTA CAUSANDO EL ERROR...
        alerta.setTitle("Datos inválidos");
        alerta.setHeaderText(null);
        
        //variables valid_
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();   
        equalsPassword = new SimpleBooleanProperty();
        validName = new SimpleBooleanProperty();
        validAge = new SimpleBooleanProperty();
        
        //inicializadas a FALSE
        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        equalsPassword.setValue(Boolean.FALSE);
        validName.setValue(Boolean.FALSE);
        validAge.setValue(Boolean.FALSE);

        //AND de todas las condiciones, la comprobacion final es sobre validFields
        BooleanBinding validFields = Bindings.and(validEmail, validPassword).and(equalsPassword).and(validName).and(validAge);
                
        //LISTENERS de Nombre, Corre, Contraseña, Comprobacion Contraseña...
        id_nombre.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue){checkName(); 
            }});
        
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
    
    

    @FXML   //CANCELAR... sustituir el string por nombre del .fxml de la ventana INICIAR SESION
    private void handleCancelOnAction(ActionEvent event) throws IOException {
        switchToScene(event, "FXMLInicio");
    }

    @FXML   //ACEPTAR... sustituir el string por nombre del .fxml de la ventana FUNCIONES
    private void handleAcceptOnAction(ActionEvent event) throws IOException, NavegacionDAOException {
        navegador.registerUser(id_nombre.getText(), id_correo.getText(), id_contraseña.getText(), id_imagen.getImage(), id_FechaNacimiento.getValue());
        switchToScene(event, "FXMLPrincipal");
    }

    @FXML   //SELECCIONAR IMAGEN... falta mantener el ratio de imagen (sino el resto de bloques se descolocan)
    private void handlePressedAction(MouseEvent event) throws FileNotFoundException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona imagen de perfil");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        
        Image imagen = new Image(new FileInputStream(selectedFile));
        id_imagen.setImage(imagen);  
    }

    @FXML   //SELECCION DE FECHA NACIMIENTO
    private void handleDate(ActionEvent event) {
        LocalDate fecha = id_FechaNacimiento.getValue();
        LocalDate actual = LocalDate.now();
        
        int años = actual.getYear() - fecha.getYear();
        int meses = abs(actual.getMonthValue() - fecha.getMonthValue());
        int dias = abs(actual.getDayOfMonth() - fecha.getDayOfMonth());
 
        if(años >= 16) {
            validAge.setValue(Boolean.TRUE);
        }else if (años == 15 && meses == 0 && dias == 0) {
            validAge.setValue(Boolean.TRUE);
        }else {
            validAge.setValue(Boolean.FALSE);
            mensaje = "El usuario debe ser mayor de edad.";
            alerta.showAndWait();
        }
    }
}
