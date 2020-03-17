
package view_controller;

import DAO.AggregatorApptImpl;
import DAO.CustomerImpl;
import DAO.UserImpl;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.AggregatorAppt;
import model.Customer;
import model.User;

/**
 * FXML Controller class
 *
 * @author Dana K Lowe
 */
public class AppointmentScreenController implements Initializable {
   
    Stage stage;
    Parent scene;
    ZoneId zid = ZoneId.systemDefault();
    static AggregatorAppt selectedItem;
    ObservableList<AggregatorAppt> apptList;
    ObservableList<Customer> customerList;
    ObservableList<User> userList;
    ObservableList<String> apptTypeList = FXCollections.observableArrayList("Presentation", "Scrum", "Consultation", "Miscellaneous");
    ObservableList<String> hours = FXCollections.observableArrayList("09", "10", "11", "12", "13", "14", "15", "16");
    ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> duration = FXCollections.observableArrayList("15 minutes", "30 minutes", "45 minutes", "1 hour");
    static FilteredList<AggregatorAppt> filteredData;
    
    @FXML
    private ComboBox<Customer> customerCB;

    @FXML
    private ComboBox<User> consultantCB;
    
    @FXML
    private ComboBox<String> typeCB;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> startingHourCB;
    
    @FXML
    private ComboBox<String> startingMinuteCB;

    @FXML
    private ComboBox<String> durationCB;

    @FXML
    private TableView<AggregatorAppt> appointmentTbl;

    @FXML
    private TableColumn<AggregatorAppt, String> customerCol;

    @FXML
    private TableColumn<AggregatorAppt, String> consultantCol;
    
    @FXML
    private TableColumn<AggregatorAppt, String> typeCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalDate> dateCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> startCol;

    @FXML
    private TableColumn<AggregatorAppt, LocalTime> endCol;
    
    @FXML
    private RadioButton thisWeekRBtn;

    @FXML
    private ToggleGroup dateRange;

    @FXML
    private RadioButton thisMonthRBtn;

    @FXML
    private RadioButton noneRBtn;

    @FXML
    void addBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
        if (customerCB.getValue() == null || consultantCB.getValue() == null ||
                  typeCB.getValue() == null || datePicker.getValue() == null ||
                  startingHourCB.getValue() == null || startingMinuteCB.getValue() == null ||
                  durationCB.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing fields");
            alert.setContentText("Please ensure all fields are properly selected before adding the appointment.");
            alert.showAndWait();
            
//Else if statement to handle appointment booking attempts made after business hours(9am-5pm) (weekend restrictions made by datepicker implementation in initialize()).
        }else if (AggregatorApptImpl.isApptAfterHours(startingHourCB.getValue(), startingMinuteCB.getValue(), durationCB.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Consultant booked outside normal business hours");
            alert.setContentText("The proposed appointment time and duration would occur beyond normal business hours (MON - FRI 9am - 5pm)." + 
                    "\n\nPlease select a different appointment time and/or duration.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, consultantCB.getValue().getUserName() + "'s appointment with " + customerCB.getValue().getCustomerName() + " will be added to the appointment table.\n\nDo you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                int customerId = customerCB.getValue().getCustomerId();
                int userId = consultantCB.getValue().getUserId();
                String customerName = customerCB.getValue().getCustomerName();
                String userName = consultantCB.getValue().getUserName();
                String type = typeCB.getValue();
                LocalDate date = datePicker.getValue();
                String hour = startingHourCB.getValue();
                String minute = startingMinuteCB.getValue();
                String durationM = durationCB.getValue();
                long durationMins;
                switch (durationM){
                    case "15 minutes":
                        durationMins = 15;
                        break;
                    case "30 minutes":
                        durationMins = 30;
                        break;
                    case "45 minutes":
                        durationMins = 45;
                        break;
                    case "1 hour":
                        durationMins = 60;
                        break;
                    default:
                        durationMins = 0;
                }

                LocalDateTime startLDT = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
                LocalDateTime endLDT = startLDT.plusMinutes(durationMins);
                
                ZonedDateTime localZdtStart = startLDT.atZone(zid);
                ZonedDateTime localZdtEnd = endLDT.atZone(zid);

                AggregatorAppt enteredFields = new AggregatorAppt(0, customerId, userId, customerName, userName, type, localZdtStart, localZdtEnd);
                
// Code to handle double booking of a consultant, using an alert box and a static funtion to check time overlap.
                AggregatorAppt a = AggregatorApptImpl.isApptConflicting(apptList, enteredFields, userId);
                if(a != null){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Consultant double-booked");
                    alert2.setContentText(consultantCB.getValue().getUserName()+ " has a conflicting appointment with " + a.getCustomerName() + " on " + a.getApptDate() + "." +
                            "\n\nPlease select a different appointment time and/or duration that does not conflict.");
                    alert2.showAndWait();
                    
                }else{
                AggregatorApptImpl.addAggregatorAppt(enteredFields);
                apptList = AggregatorApptImpl.getAllAggregatorAppts();
                filterBySelectedRange();
                
    }}
            }
        
    }

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view_controller/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void deleteBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
        if(appointmentTbl.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No table item selected");
            alert.setContentText("Please first select an appointment entry from the table to delete.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, selectedItem.getUserName()+"'s appointment with " + selectedItem.getCustomerName() + " will be deleted. Do you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                AggregatorApptImpl.deleteAggregatorAppt(selectedItem);
                apptList = AggregatorApptImpl.getAllAggregatorAppts();
                filterBySelectedRange();
            }
        } 
    }

    @FXML
    void updateBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException, ParseException {
        if(appointmentTbl.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No table item selected");
            alert.setContentText("Please first select an appointment entry from the table to update.");
            alert.showAndWait();
        }else if (customerCB.getValue() == null || consultantCB.getValue() == null ||
                  typeCB.getValue() == null || datePicker.getValue() == null ||
                  startingHourCB.getValue() == null || startingMinuteCB.getValue() == null ||
                  durationCB.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing fields");
            alert.setContentText("Please ensure all fields are properly selected before updating the highlighted appointment entry.");
            alert.showAndWait();
//Else if statement to handle appointment booking attempts made after business hours(9am-5pm) (weekend restrictions made by datepicker implementation in initialize()).
        }else if (AggregatorApptImpl.isApptAfterHours(startingHourCB.getValue(), startingMinuteCB.getValue(), durationCB.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Consultant booked outside business hours");
            alert.setContentText("The proposed appointment time and duration would occur beyond normal business hours (MON - FRI 9am - 5pm)." + 
                    "\n\nPlease select a different appointment time and/or duration.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, selectedItem.getUserName() + "'s appointment with " + selectedItem.getCustomerName() + " will be updated to the newly selected fields.\n\nDo you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                String durationM = durationCB.getValue();
                long durationMins;
                switch (durationM){
                    case "15 minutes":
                        durationMins = 15;
                        break;
                    case "30 minutes":
                        durationMins = 30;
                        break;
                    case "45 minutes":
                        durationMins = 45;
                        break;
                    case "1 hour":
                        durationMins = 60;
                        break;
                    default:
                        durationMins = 0;
                }

                String startingTime = startingHourCB.getValue() + ":" + startingMinuteCB.getValue();

                long endingHrs = Long.parseLong(startingHourCB.getValue());
                long endingMins = Long.parseLong(startingMinuteCB.getValue());
                endingMins += durationMins;
                long hourInc = endingMins / 60;
                endingMins %= 60;
                endingHrs += hourInc;

                String endingTime = String.format("%02d", endingHrs) + ":" + String.format("%02d", endingMins);
                
                AggregatorAppt aggAppt = new AggregatorAppt(selectedItem.getAppointmentId(), customerCB.getValue().getCustomerId(), consultantCB.getValue().getUserId(), 
                        customerCB.getValue().getCustomerName(), consultantCB.getValue().getUserName(), typeCB.getValue(),
                        LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startingTime)).atZone(zid), LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endingTime)).atZone(zid));

// Code to handle double booking of a consultant, using an alert box and a static funtion to check time overlap.
                AggregatorAppt a = AggregatorApptImpl.isApptConflicting(apptList, aggAppt, consultantCB.getValue().getUserId());
                if(a != null) {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Consultant double-booked");
                    alert2.setContentText(consultantCB.getValue().getUserName()+ " has a conflicting appointment with " + a.getCustomerName() + " on " + a.getApptDate() + "." +
                            "\n\nPlease select a different appointment time and/or duration that does not conflict.");
                    alert2.showAndWait();
                    
                }else{
                AggregatorApptImpl.updateAggregatorAppt(aggAppt);
                apptList = AggregatorApptImpl.getAllAggregatorAppts();
                filterBySelectedRange();
                
            }  }
        }
    }
    

    @FXML
    void thisWeekRBtnClicked(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus7 = now.plusDays(7);
        filteredData = new FilteredList<>(apptList);
        // Lambda expression used to more efficiently select each observable list item that occurs within the week (next 7 days).
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = row.getLocalZdtStart().toLocalDateTime();

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus7);
        });
        appointmentTbl.setItems(filteredData);

    }

    @FXML
    void thisMonthRBtnClicked(ActionEvent event) {

        YearMonth thisMonth = YearMonth.now();
        LocalDateTime firstDayOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);
        
        filteredData = new FilteredList<>(apptList);
        // Lambda expression used to more efficiently select each observable list item that occurs within the current month.
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = row.getLocalZdtStart().toLocalDateTime();

            return rowDate.isAfter(firstDayOfMonth) && rowDate.isBefore(lastDayOfMonth);
        });
        appointmentTbl.setItems(filteredData);

    }
    
    @FXML
    void noneRBtnClicked(ActionEvent event) {
        filteredData = new FilteredList<>(apptList);

        appointmentTbl.setItems(filteredData);

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
// Creates a day cell factory that disables weekends to prevent booking appointments outside of Mon - Fri
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>(){
            public DateCell call(final DatePicker datePicker){
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);

                        // disable weekends
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
                            this.setDisable(true);
                        }
                    }  }; }
        };
        
// Assigns the newly created day cell factory to the datePicker object
        datePicker.setDayCellFactory(dayCellFactory);
// Prevents direct input of weekend dates into text field portion of datePicker
        datePicker.setEditable(false);
        
        try {
            apptList = AggregatorApptImpl.getAllAggregatorAppts();
            appointmentTbl.setItems(apptList);
            customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            consultantCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("apptDate"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
            
            customerList = CustomerImpl.getAllCustomers();
            customerCB.setItems(customerList);
            
            userList = UserImpl.getAllUsers();
            consultantCB.setItems(userList);
            
            typeCB.setItems(apptTypeList);
            
            startingHourCB.setItems(hours);
            startingMinuteCB.setItems(minutes);
            durationCB.setItems(duration);
            
            // Selecting tableview list item automatically populates update form's combobox selections & datepicker date. Uses addListener and a lambda expression.
            appointmentTbl.getSelectionModel().selectedItemProperty().addListener((Observable event) -> {
            selectedItem = appointmentTbl.getSelectionModel().getSelectedItem();
            
            if(selectedItem == null) {
                // add implementation to create default values for comboboxes & datepicker if updating/adding to the OL and nulling out selectedItem causes NPE issues--update: not needed
            }else{
                int selectedCustomerId = selectedItem.getCustomerId();
                for(Customer cust : customerList) {
                    if(cust.getCustomerId() == selectedCustomerId){
                        customerCB.setValue(cust);
                        break;
                    }
                }
                int selectedUserId = selectedItem.getUserId();
                for(User user : userList) {
                    if(user.getUserId() == selectedUserId){
                        consultantCB.setValue(user);
                        break;
                    }
                }
                typeCB.setValue(selectedItem.getType());
                datePicker.setValue(selectedItem.getApptDate());
                startingHourCB.setValue(String.format("%02d", selectedItem.getApptStartTime().getHour()));
                startingMinuteCB.setValue(String.format("%02d", selectedItem.getApptStartTime().getMinute()));
                String durationMins;
                switch ((int)selectedItem.getApptDuration()){
                    case 15:
                        durationMins = "15 minutes";
                        break;
                    case 30:
                        durationMins = "30 minutes";
                        break;
                    case 45:
                        durationMins = "45 minutes";
                        break;
                    case 60:
                        durationMins = "1 hour";
                        break;
                    default:
                        durationMins = "0 minutes";
                        break;
        }
                durationCB.setValue(durationMins);

            }});
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(AppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filterBySelectedRange(){
        RadioButton selectedRBtn = (RadioButton)dateRange.getSelectedToggle();
        String sRBtnName = selectedRBtn.getText();
        if (sRBtnName.equals("This month")){
            YearMonth thisMonth = YearMonth.now();
            LocalDateTime firstDayOfMonth = thisMonth.atDay(1).atStartOfDay();
            LocalDateTime lastDayOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);

            filteredData = new FilteredList<>(apptList);
            // Lambda expression used to more efficiently select each observable list item that occurs within the current month.
            filteredData.setPredicate(row -> {

                LocalDateTime rowDate = row.getLocalZdtStart().toLocalDateTime();

                return rowDate.isAfter(firstDayOfMonth) && rowDate.isBefore(lastDayOfMonth);
            });
            appointmentTbl.setItems(filteredData);        
            
        }else if(sRBtnName.equals("This week")){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowPlus7 = now.plusDays(7);
            filteredData = new FilteredList<>(apptList);
            // Lambda expression used to more efficiently select each observable list item that occurs within a week(next 7 days).
            filteredData.setPredicate(row -> {

                LocalDateTime rowDate = row.getLocalZdtStart().toLocalDateTime();

                return rowDate.isAfter(now) && rowDate.isBefore(nowPlus7);
            });
            appointmentTbl.setItems(filteredData);

        }else{
            filteredData = new FilteredList<>(apptList);
            appointmentTbl.setItems(filteredData);        }
    }
}
