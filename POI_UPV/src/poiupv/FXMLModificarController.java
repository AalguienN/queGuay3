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
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author marta
 */
public class FXMLModificarController implements Initializable {
    
   //properties to control valid fieds values. 
    private Boolean validPassword;
    private Boolean validEmail;
    private Boolean equalsPassword;  
    private Boolean validAge;
    
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
    
    User usuario;
    int aciertos;
    int fallos;

    @FXML
    private Button id_buttonA;
    @FXML
    private Button id_buttonC;
    @FXML
    private TextField id_nombre;
    @FXML
    private TextField id_correo;
    @FXML
    private PasswordField id_contrase??a;
    @FXML
    private PasswordField id_contrase??a1;
    @FXML
    private DatePicker id_FechaNacimiento;
    @FXML
    private ImageView id_imagen;
    @FXML
    private Label id_SelecImagen;
    
    private Stage primaryStage;


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
        
        //INICIALIZA LAS VENTANAS EMERGENTES
        alerta.setTitle("Datos inv??lidos");
        alerta.setHeaderText(null);
        alerta.setResizable(true);
        alerta.getDialogPane().setPrefSize(350, 150);
        
        //variables valid_
        validEmail = false;
        validPassword = false;   
        equalsPassword = false;
        validAge = false;
        
        id_correo.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue)checkEmail();
        });
        
        id_contrase??a.focusedProperty().addListener((observable, oldValue, newValue)-> {
            if(!newValue){checkPassword();}
        });
        
        id_contrase??a1.focusedProperty().addListener((observable, oldValue, newValue)-> {
           if(!newValue){checkEqualsPassword();}
            });
        
        
        //comprobacion boton aceptar
        //id_buttonA.disableProperty().bind(Bindings.not(validFields));
        
        id_buttonA.sceneProperty().addListener((obs, oldScene, newScene) -> {
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
    private void handleAcceptOnAction(ActionEvent event) throws IOException, NavegacionDAOException {
        if(validPassword){usuario.setPassword(id_contrase??a.getText());}
        if(validAge){usuario.setBirthdate(id_FechaNacimiento.getValue());}
        if(validEmail){usuario.setEmail(id_correo.getText());}
        usuario.setAvatar(id_imagen.getImage());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
        Parent root = loader.load();
        FXMLPrincipalController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Men??");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void handleCancelOnAction(ActionEvent event) throws IOException { 
         FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPrincipal.fxml"));
        Parent root = loader.load();
        FXMLPrincipalController controlador = loader.getController();
        controlador.pasarDatos(usuario, aciertos, fallos);
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Men??");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @FXML
    private void handleDate(ActionEvent event) {
        LocalDate fecha = id_FechaNacimiento.getValue();
        LocalDate actual = LocalDate.now();
        
        int a??os = actual.getYear() - fecha.getYear();
        int meses = abs(actual.getMonthValue() - fecha.getMonthValue());
        int dias = abs(actual.getDayOfMonth() - fecha.getDayOfMonth());
 
        if(a??os >= 16) {
            validAge = true;
        }else if (a??os == 15 && meses == 0 && dias == 0) {
            validAge = true;
        }else {
            validAge = false;
            mensaje = "El usuario debe ser mayor de edad. Introduzca una fecha v??lida.";
            alerta.setContentText(mensaje);
            alerta.showAndWait();
            }
       
        }   
    

    @FXML
    private void handlePressedAction(MouseEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona imagen de perfil");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Im??genes", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        
        Image imagen = new Image(new FileInputStream(selectedFile));
        id_imagen.setImage(imagen);
        
    }
    
     public void pasarDatos(User u, int aciertos, int fallos) {
        usuario = u;
        this.aciertos = aciertos;
        this.fallos = fallos;
        
        id_nombre.setText(usuario.getNickName());
        id_correo.setText(usuario.getEmail());
        id_contrase??a.setText(usuario.getPassword());
        id_FechaNacimiento.setValue(usuario.getBirthdate());
        id_imagen.setImage(usuario.getAvatar());
        
    }
    
    public void checkEmail() {
        if(!User.checkEmail(id_correo.getText())) {
            validEmail = false;
            mensaje = "El correo no es v??lido.";
            id_correo.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }else{
            validEmail = true;
            id_correo.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }
    }
     
    //COMPROBAR CONTRASE??A V??LIDA
    public void checkPassword() {
        if(!User.checkPassword(id_contrase??a.getText())) {
            validPassword = false;
            mensaje = "La contrase??a no es v??lida. La contrase??a debe contener [8-20] caracteres, contener al menos una letra may??scula, una letra min??scula, un d??gito y un caracter especial [!@#$%&*()-+=]. No debe contener espacios";
            id_contrase??a.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }else if(usuario.getPassword().equals(id_contrase??a.getText())) {
            validPassword = false;
            mensaje = "La contrase??a introducida no puede coincidir con la contrase??a activa actualmente. Introduce una contrase??a nueva";
            id_contrase??a.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }else {
            validPassword = true;
            id_contrase??a.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }
    }
    
    //COMPROBAR CONTRASE??AS IGUALES
    public void checkEqualsPassword() {
        if("".equals(id_contrase??a1.getText())){
            equalsPassword = false;
            id_contrase??a1.styleProperty().setValue("-fx-background-color: #FCE5E0");
        } else if(id_contrase??a.textProperty().getValueSafe().compareTo(id_contrase??a1.textProperty().getValueSafe()) == 0){
            equalsPassword = true;
            id_contrase??a1.styleProperty().setValue("-fx-background-color: #CDFFD0");
        }else {
            equalsPassword = false;
            mensaje = "No coincide con la contrase??a. Compruebe que haya escrito la misma contrase??a.";
            id_contrase??a1.styleProperty().setValue("-fx-background-color: #FCE5E0");
            alerta.setContentText(mensaje);
            alerta.showAndWait(); 
        }
    }    

}
    

