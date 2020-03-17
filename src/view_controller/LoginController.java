
package view_controller;

import static DAO.UserImpl.verifyCredentials;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dana K Lowe
 */
public class LoginController implements Initializable {
    Stage stage;
    Parent scene;
    private static String verifiedUser;
    
    public static String getVerifiedUser(){
        return verifiedUser;
    }
    
    @FXML
    private TextField usernameTxt;

    @FXML
    private TextField passwordTxt;
    
    @FXML
    private Label notificationLbl;

    @FXML
    private Button loginBtn;
    
    @FXML
    private Button exitBtn;
    
    @FXML
    private Label label1, label2, label3, label4, label5;


    @FXML
    void loginBtnClicked(ActionEvent event) throws IOException, Exception {
        String enteredUser = usernameTxt.getText();
        String enteredPW = passwordTxt.getText();
        char goodAttempt = 'N';
        boolean tooLong = false;
        String filename = "src/files/loginAttempts.txt";
        FileWriter loginFW = new FileWriter(filename, true);
        PrintWriter loginPW = new PrintWriter(loginFW);
        
        try{
            if(enteredUser.length() > 15 || enteredPW.length() > 15){
                tooLong = true;
                notificationLbl.setText("Username and Password must both be less than 15 characters.");
            }else if (verifyCredentials(enteredUser, enteredPW)) {
                verifiedUser = enteredUser;
                goodAttempt = 'Y';
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view_controller/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }else if (Locale.getDefault().getLanguage().equals("es")){
                ResourceBundle rb = ResourceBundle.getBundle("view_controller/Lang", Locale.getDefault());
                notificationLbl.setText(rb.getString("Invalid") + " " + rb.getString("username") + " " + rb.getString("and/or") + " " + rb.getString("password"));
            }else{notificationLbl.setText("Invalid username and/or password.");}

        }catch(Exception e){
            notificationLbl.setText("Invalid username and/or password.");
        }
        
        if(!tooLong){
        loginPW.printf("%24s\t%-16s%-16s\t%-1s\n", Instant.now().toString(), enteredUser, enteredPW, goodAttempt);
        loginPW.close();
        }
    }
    
    
    @FXML
    void exitBtnClicked(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            rb = ResourceBundle.getBundle("view_controller/Lang", Locale.getDefault());
            if(Locale.getDefault().getLanguage().equals("es")){
            label1.setText(rb.getString("Prestige") + " " + rb.getString("Worldwide") + " " + rb.getString("Consulting"));
            label2.setText(rb.getString("Client") + " " + rb.getString("Scheduling") + " " + rb.getString("Database"));
            label3.setText(rb.getString("Consultant") + " " + rb.getString("Login"));
            label4.setText(rb.getString("Username"));
            label5.setText(rb.getString("Password"));
            loginBtn.setText(rb.getString("Login"));
            exitBtn.setText(rb.getString("Exit"));
        }}catch(Exception e){
            System.out.println("English not a specified locale.");
        }
    }    
    
}
