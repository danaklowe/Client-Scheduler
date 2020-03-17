
package DAO;

import DAO.DBConnection;
import DAO.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.AggregatorCust;

/**
 *
 * @author Dana K Lowe
 */
public class CustomerImpl {
    static boolean act;
    
    public static Customer getCustomer(String customerName) throws SQLException, Exception {
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM customer WHERE customerName = '" + customerName + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Customer customerResult;
        if (result.next()){
            int customerId = result.getInt("customerId");
            String customerNameB = result.getString("customerName");
            int addressId = result.getInt("addressId");
            int active = result.getInt("active");
            if(active == 1) act = true;
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            customerResult = new Customer(customerId, customerNameB, addressId, act, createDate, createdBy, lastUpdate, lastUpdateBy);
            return customerResult;
        }
        DBConnection.closeConnection();
        return null;
    }
    
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM customer";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Customer customerResult;
        while (result.next()){
            int customerId = result.getInt("customerId");
            String customerNameB = result.getString("customerName");
            int addressId = result.getInt("addressId");
            int active = result.getInt("active");
            if(active == 1) act = true;
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            customerResult = new Customer(customerId, customerNameB, addressId, act, createDate, createdBy, lastUpdate, lastUpdateBy);
            allCustomers.add(customerResult);
        }
        DBConnection.closeConnection();
        return allCustomers;
    }
    
    public static boolean isCustomerNameTaken(ObservableList<AggregatorCust> custList, AggregatorCust c){
        for(AggregatorCust cust : custList){
            if (cust.getName().equals(c.getName())){
                return true;                
            }
        }
        return false;
    }
}
