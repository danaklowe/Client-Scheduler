
package view_controller;

import DAO.AggregatorCustImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import DAO.CustomerImpl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.AggregatorCust;

/**
 * FXML Controller class
 *
 * @author Dana K Lowe
 */
public class CustomerScreenController implements Initializable {
    Parent scene;
    Stage stage;
    static ObservableList<AggregatorCust> agg;
    static AggregatorCust selectedItem;
    static int selectedCustomerId;
    static int selectedAddressId;
    static int selectedCityId;
    static int selectedCountryId;
    static AggregatorCust defaultItem = new AggregatorCust("", false, "", "", "", "", "", "", 0, 0, 0, 0);
            
    @FXML
    private TableView<AggregatorCust> customerTbl;

    @FXML
    private TableColumn<AggregatorCust, String> nameCol;

    @FXML
    private TableColumn<AggregatorCust, String> addressCol;

    @FXML
    private TableColumn<AggregatorCust, String> phoneCol;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField address1Txt;

    @FXML
    private TextField address2Txt;

    @FXML
    private TextField cityTxt;
    
    @FXML
    private TextField countryTxt;

    @FXML
    private TextField zipcodeTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private RadioButton activeRBtn;

    @FXML
    private ToggleGroup activityToggle;

    @FXML
    private RadioButton inactiveRBtn;

    @FXML
    void inactiveRBtnClicked(ActionEvent event) {

    }

    @FXML
    void activeRBtnClicked(ActionEvent event) {

    }

    @FXML
    void clearBtnClicked(ActionEvent event) {
        nameTxt.clear();
        address1Txt.clear();
        address2Txt.clear();
        cityTxt.clear();
        countryTxt.clear();
        zipcodeTxt.clear();
        phoneTxt.clear();
        activeRBtn.setSelected(false);
        inactiveRBtn.setSelected(false);
        
    }
    
    @FXML
    void addBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (nameTxt.getText() == null || nameTxt.getText().isEmpty() ||
            address1Txt.getText() == null || address1Txt.getText().isEmpty()||
//            address2Txt.getText() == null || address2Txt.getText().isEmpty() ||  secondary address can be empty when adding new customer
            cityTxt.getText() == null || cityTxt.getText().isEmpty() ||
            countryTxt.getText() == null || countryTxt.getText().isEmpty() ||
            zipcodeTxt.getText() == null || zipcodeTxt.getText().isEmpty() ||
            phoneTxt.getText() == null || phoneTxt.getText().isEmpty() ||
            (!activeRBtn.isSelected() && !inactiveRBtn.isSelected())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing fields");
            alert.setContentText("Please ensure all fields are properly entered and an activity state is selected.\n\nFields with an asterisk(*) are not required.");
            alert.showAndWait();
        }else if(!(phoneTxt.getText().matches("^[0-9-]*$") && phoneTxt.getText().length() > 2)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid phone number");
            alert.setContentText("Please enter only numerical values for Phone #.");
            alert.showAndWait();
        }else if(!((cityTxt.getText().matches("^[ A-Za-z]+$") && cityTxt.getText().length() > 1) && 
                 (countryTxt.getText().matches("^[ A-Za-z]+$") && countryTxt.getText().length() > 1))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid city and/or country");
            alert.setContentText("Please enter only alphabetical values and spaces for both City and Country.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\""+nameTxt.getText()+"\" will be added to the Customer table. Do you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                AggregatorCust enteredFields = new AggregatorCust (nameTxt.getText(), activeRBtn.isSelected(), address1Txt.getText(), address2Txt.getText(), cityTxt.getText(), 
                countryTxt.getText(), zipcodeTxt.getText(), phoneTxt.getText(), 0, 0, 0, 0);
                if(CustomerImpl.isCustomerNameTaken(agg, enteredFields)){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Customer name taken");
                    alert2.setContentText("Possible duplicate customer.\n\nPlease enter a different customer name.");
                    alert2.showAndWait();
                }else{
                    AggregatorCustImpl.addAggregator(enteredFields);
                    agg = AggregatorCustImpl.getAllAggregators();
                    customerTbl.setItems(agg);
                }
            }
        }
    }

    @FXML
    void deleteBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(customerTbl.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No table item selected");
            alert.setContentText("Please first select a customer entry from the table to delete.");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\""+selectedItem.getName()+"\" will be deleted. Do you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                AggregatorCustImpl.deleteAggregator(selectedItem);
                agg = AggregatorCustImpl.getAllAggregators();
                customerTbl.setItems(agg);
            }
        }    
    }

    @FXML
    void updateBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(customerTbl.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No table item selected");
            alert.setContentText("Please first select a customer entry from the table to update.");
            alert.showAndWait();
        }
        else if (nameTxt.getText() == null || nameTxt.getText().isEmpty() ||
            address1Txt.getText() == null || address1Txt.getText().isEmpty()||
//            address2Txt.getText() == null || address2Txt.getText().isEmpty() ||
            cityTxt.getText() == null || cityTxt.getText().isEmpty() ||
            countryTxt.getText() == null || countryTxt.getText().isEmpty() ||
            zipcodeTxt.getText() == null || zipcodeTxt.getText().isEmpty() ||
            phoneTxt.getText() == null || phoneTxt.getText().isEmpty() ||
            (!activeRBtn.isSelected() && !inactiveRBtn.isSelected())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing fields");
            alert.setContentText("Please ensure all fields are properly entered and a radio button is selected.\n\nFields with an asterisk(*) are not required.");
            alert.showAndWait();
        }else if(!(phoneTxt.getText().matches("^[0-9-]*$") && phoneTxt.getText().length() > 2)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid phone number");
            alert.setContentText("Please enter only numerical values for Phone #.");
            alert.showAndWait();
        }else if(!((cityTxt.getText().matches("^[ A-Za-z]+$") && cityTxt.getText().length() > 1) && 
                 (countryTxt.getText().matches("^[ A-Za-z]+$") && countryTxt.getText().length() > 1))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid city and/or country");
            alert.setContentText("Please enter only alphabetical values and spaces for both City and Country.");
            alert.showAndWait();
        }else{
        AggregatorCust updatedFields = new AggregatorCust (nameTxt.getText(), activeRBtn.isSelected(), address1Txt.getText(), address2Txt.getText(), cityTxt.getText(), 
                                                   countryTxt.getText(), zipcodeTxt.getText(), phoneTxt.getText(), selectedCustomerId, selectedAddressId, selectedCityId, selectedCountryId);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\""+selectedItem.getName()+"\" will be updated to \"" + updatedFields.getName() + "\"'s newly entered fields.\n\nDo you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                AggregatorCustImpl.updateAggregator(updatedFields, agg);
                agg = AggregatorCustImpl.getAllAggregators();
                customerTbl.setItems(agg);
            }
        }
    }
    
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
         agg = AggregatorCustImpl.getAllAggregators();
         customerTbl.setItems(agg);
         nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
         addressCol.setCellValueFactory(new PropertyValueFactory<>("combinedAddress"));
         phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Selecting tableview list item automatically populates update form's text fields. Uses addListener and a lambda expression.
        
        customerTbl.getSelectionModel().selectedItemProperty().addListener((Observable event) -> {
            selectedItem = customerTbl.getSelectionModel().getSelectedItem();
            
            if(selectedItem == null) {
                nameTxt.setText("");
                address1Txt.setText("");
                address2Txt.setText("");
                cityTxt.setText("");
                countryTxt.setText("");
                zipcodeTxt.setText("");
                phoneTxt.setText("");
                inactiveRBtn.setSelected(true);
            }else{
            nameTxt.setText(selectedItem.getName());
            address1Txt.setText(selectedItem.getAddress());
            address2Txt.setText(selectedItem.getAddress2());
            cityTxt.setText(selectedItem.getCity());
            countryTxt.setText(selectedItem.getCountry());
            zipcodeTxt.setText(selectedItem.getPostalCode());
            phoneTxt.setText(selectedItem.getPhone());
            if(selectedItem.isActive()){
                activeRBtn.setSelected(true);
            }else{
                inactiveRBtn.setSelected(true);
            }
            selectedCustomerId = selectedItem.getCustomerId();
            selectedAddressId = selectedItem.getAddressId();
            selectedCityId = selectedItem.getCityId();
            selectedCountryId = selectedItem.getCountryId();
            }});
    }
}

