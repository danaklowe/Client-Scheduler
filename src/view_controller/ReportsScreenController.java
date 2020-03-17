
package view_controller;

import DAO.AggregatorApptImpl;
import DAO.UserImpl;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AggregatorAppt;
import model.User;

/**
 * FXML Controller class
 *
 * @author Dana K Lowe
 */
public class ReportsScreenController implements Initializable {
    Stage stage;
    Parent scene;
    ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    ObservableList<User> userList;
    ObservableList<String> dayList = FXCollections.observableArrayList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    
    @FXML
    private TableView<AggregatorAppt> appointmentTbl;

    @FXML
    private TableColumn<AggregatorAppt, String> customerCol;

    @FXML
    private TableColumn<AggregatorAppt, String> typeCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalDate> dateCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> startCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> endCol;

    @FXML
    private ComboBox<User> consultantCB;

    @FXML
    private Label presentationLbl;

    @FXML
    private Label consultationLbl;

    @FXML
    private Label scrumLbl;

    @FXML
    private Label miscLbl;

    @FXML
    private ComboBox<String> monthCB;
    
    @FXML
    private ComboBox<String> dayCB;

    @FXML
    private TableView<AggregatorAppt> appointmentTbl1;

    @FXML
    private TableColumn<AggregatorAppt, String> customerCol1;
    
    @FXML
    private TableColumn<AggregatorAppt, String> consultantCol1;

    @FXML
    private TableColumn<AggregatorAppt, String> typeCol1;

    @FXML
    private TableColumn<AggregatorAppt, LocalDate> dateCol1;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> startCol1;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> endCol1;
       
    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
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
            List<AggregatorAppt> monthApptList =  new ArrayList();
            ObservableList<AggregatorAppt> apptList = AggregatorApptImpl.getAllAggregatorAppts();
            monthCB.setItems(monthList);
            // Lambda expression for selecting Month combobox item and comparing that item to the observable list of all appointments 
            monthCB.getSelectionModel().selectedItemProperty().addListener((Observable event) -> {
            String selectedMonth = monthCB.getSelectionModel().getSelectedItem();
            for(AggregatorAppt a : apptList) {
                if (a.getLocalZdtStart().getMonth().getDisplayName(TextStyle.FULL, Locale.US).equals(selectedMonth)){
                    monthApptList.add(a);
                }
            }
            
            int presentations = 0;
            int consultations = 0;
            int scrums = 0;
            int miscs = 0;
            
            for(AggregatorAppt a : monthApptList){
                switch (a.getType()) {
                    case "Presentation":
                        presentations++;
                        break; 
                    case "Consultation":
                        consultations++;
                        break;
                    case "Scrum":
                        scrums++;
                        break;
                    case "Miscellaneous":
                        miscs++;
                        break;
                }
            }
            
            presentationLbl.setText(String.valueOf(presentations));
            consultationLbl.setText(String.valueOf(consultations));
            scrumLbl.setText(String.valueOf(scrums));
            miscLbl.setText(String.valueOf(miscs));
            monthApptList.clear();
            });
            
            userList = UserImpl.getAllUsers();
            consultantCB.setItems(userList);
            ObservableList<AggregatorAppt> userApptList =  FXCollections.observableArrayList();
            // Lambda expression for selecting Consultant combobox item and comparing that item to the observable list of all appointments 
            consultantCB.getSelectionModel().selectedItemProperty().addListener((Observable event) -> {
                userApptList.clear();
                User selectedUser = consultantCB.getSelectionModel().getSelectedItem();
                for(AggregatorAppt a : apptList){
                    if (a.getUserId() == selectedUser.getUserId()){
                        userApptList.add(a);
                    }
                }
                appointmentTbl.setItems(userApptList);
                customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                dateCol.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
                startCol.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
                endCol.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
            });
            
            dayCB.setItems(dayList);
            ObservableList<AggregatorAppt> dayApptList = FXCollections.observableArrayList();
            // Lambda expression for selecting Day combobox item and comparing that item to the observable list of all appointments 
            dayCB.getSelectionModel().selectedItemProperty().addListener((Observable event) -> {
                dayApptList.clear();
                String selectedDay = dayCB.getSelectionModel().getSelectedItem();
                for(AggregatorAppt a : apptList){
                    if (a.getLocalZdtStart().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US).equals(selectedDay)){
                        dayApptList.add(a);
                    }
                }
                if(!dayApptList.isEmpty()){
                appointmentTbl1.setItems(dayApptList);
                customerCol1.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                consultantCol1.setCellValueFactory(new PropertyValueFactory<>("userName"));
                typeCol1.setCellValueFactory(new PropertyValueFactory<>("type"));
                dateCol1.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
                startCol1.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
                endCol1.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
                }
                
            });

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReportsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ReportsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
