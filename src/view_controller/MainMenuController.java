
package view_controller;

import DAO.AggregatorApptImpl;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.AggregatorAppt;

/**
 *
 * @author Dana K Lowe
 */
public class MainMenuController implements Initializable {
    Stage stage;
    Parent scene;
    
    @FXML
    void appointmentsBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/AppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void customersBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/CustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void reportsBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/ReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void accessLogBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/AccessLog.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            AggregatorAppt aggAppt = null;
        try {
            String vUser = LoginController.getVerifiedUser();
            ObservableList<AggregatorAppt> aggList = AggregatorApptImpl.getAllAggregatorAppts();
            Instant instant = Instant.now();
            ZoneId zid = ZoneId.systemDefault();
            ZonedDateTime zdtUTC = instant.atZone(ZoneId.of("UTC"));
            ZonedDateTime zdtLocal = zdtUTC.withZoneSameInstant(zid);
            
            for(AggregatorAppt a : aggList){
                  // if statement to check 1. userName matches, 2. appointment occurs after now, 3. appointment occurs within 15 minutes of now
                  if(a.getUserName().equals(vUser) && zdtLocal.isBefore(a.getLocalZdtStart()) && a.getLocalZdtStart().minusMinutes(15).isBefore(zdtLocal)){
                      aggAppt = a;
                      break;
                }
            }
            if (aggAppt != null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointment Soon");
            alert.setContentText("You have an appointment with " + aggAppt.getCustomerName() + " at " + aggAppt.getApptStartTime() + " today.");
            alert.showAndWait();
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }    
    
}
