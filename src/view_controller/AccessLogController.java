
package view_controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AccessLog;

/**
 * FXML Controller class
 *
 * @author Dana K Lowe
 */
public class AccessLogController implements Initializable {
    Parent scene;
    Stage stage;
    String logLine;
    ObservableList<AccessLog> logList = FXCollections.observableArrayList();
    AccessLog aLog;
    
    @FXML
    private TableView<AccessLog> loginTbl;

    @FXML
    private TableColumn<AccessLog, String> timeStampCol;

    @FXML
    private TableColumn<AccessLog, String> userNameCol;

    @FXML
    private TableColumn<AccessLog, String> pwCol;

    @FXML
    private TableColumn<AccessLog, String> successCol;

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            File logFile = new File("src/files/loginAttempts.txt");
            Scanner scanner = new Scanner(logFile);
            //skip log file column headers on first line of .txt file
            scanner.nextLine();
            while(scanner.hasNextLine()){
                logLine = scanner.nextLine();
                Scanner subScanner = new Scanner(logLine);
                
                String timeStamp = subScanner.next();
                String userName = subScanner.next();
                String pw = subScanner.next();
                String success = subScanner.next();
                
                aLog = new AccessLog(timeStamp, userName, pw, success);
                
                logList.add(aLog);
            }
            
            loginTbl.setItems(logList);
            timeStampCol.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
            userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
            pwCol.setCellValueFactory(new PropertyValueFactory<>("password"));
            successCol.setCellValueFactory(new PropertyValueFactory<>("success"));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AccessLogController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }    
    
}
