
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Address;
import java.time.LocalDateTime;

/**
 *
 * @author Dana K Lowe
 */
public class AddressImpl {
    static boolean act;
    
    public static Address getAddress(int addressId) throws SQLException, Exception {
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM address WHERE addressId = '" + addressId + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Address addressResult;
        if (result.next()){
            int addressIdG = result.getInt("addressId");
            String address = result.getString("address");
            String address2 = result.getString("address2");
            int cityId = result.getInt("cityId");
            String postalCode = result.getString("postalCode");
            String phone = result.getString("phone");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            addressResult = new Address(addressIdG, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy);
            return addressResult;
        }
        DBConnection.closeConnection();
        return null;
    }
    
    public static ObservableList<Address> getAllAddresses() throws SQLException, Exception {
        ObservableList<Address> allAddresses = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM address";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Address addressResult;
        while (result.next()){
            int addressId = result.getInt("addressId");
            String addressG = result.getString("address");
            String address2 = result.getString("address2");
            int cityId = result.getInt("cityId");
            String postalCode = result.getString("postalCode");
            String phone = result.getString("phone");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            addressResult = new Address(addressId, addressG, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy);
            allAddresses.add(addressResult);
                    }
        DBConnection.closeConnection();
        return allAddresses;
    }
}
