
package DAO;

import static DAO.DBConnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AggregatorCust;
import view_controller.LoginController;


// aggregating class to hold customer name, address, and phone number
public class AggregatorCustImpl {
    
    public static ObservableList<AggregatorCust> getAllAggregators() throws ClassNotFoundException, SQLException {
        ObservableList<AggregatorCust> allAggregators = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT u.customerName, u.active, a.address, a.address2, a.postalCode, a.phone, i.city, o.country, " +
"u.customerId, a.addressId, i.cityId, o.countryId " +
"FROM customer AS u INNER JOIN address AS a ON u.addressId = a.addressId INNER JOIN city AS i ON a.cityId = i.cityId " +
"INNER JOIN country AS o ON i.countryId = o.countryId " +
"ORDER BY u.customerId";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        AggregatorCust aggResult;
        while(result.next()){
            String name = result.getString("customerName");
            boolean active = (result.getInt("active") == 1);
            String address = result.getString("address");
            String address2 = result.getString("address2");
            String postalCode = result.getString("postalCode");
            String phone = result.getString("phone");
            String city = result.getString("city");
            String country = result.getString("country");
            int customerId = result.getInt("customerId");
            int addressId = result.getInt("addressId");
            int cityId = result.getInt("cityId");
            int countryId = result.getInt("countryId");
            aggResult = new AggregatorCust(name, active, address, address2, city, country, postalCode, phone, customerId, addressId, cityId, countryId);
            allAggregators.add(aggResult);
        }
        DBConnection.closeConnection();
        return allAggregators;
    }

    public static boolean addAggregator(AggregatorCust a) throws ClassNotFoundException, SQLException{
        PreparedStatement preparedStatement;
        DBConnection.startConnection();
        
        String countryInsert = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) \n" +
"VALUES (?, now(), ?, now(), ?) ON DUPLICATE KEY UPDATE countryId = LAST_INSERT_ID(countryId) \n ";
        preparedStatement = conn.prepareStatement(countryInsert);
        preparedStatement.setString(1, a.getCountry());
        preparedStatement.setString(2, LoginController.getVerifiedUser());
        preparedStatement.setString(3, LoginController.getVerifiedUser());
        preparedStatement.executeUpdate();
        
        String cityInsert = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) \n" +
"VALUES (?, LAST_INSERT_ID(), now(), ?, now(), ?) ON DUPLICATE KEY UPDATE cityId = LAST_INSERT_ID(cityId) \n ";
        preparedStatement = conn.prepareStatement(cityInsert);
        preparedStatement.setString(1, a.getCity());
        preparedStatement.setString(2, LoginController.getVerifiedUser());
        preparedStatement.setString(3, LoginController.getVerifiedUser());
        preparedStatement.executeUpdate();

        String addressInsert = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) \n" +
"VALUES (?, ?, LAST_INSERT_ID(), ?, ?, now(), ?, now(), ?) ON DUPLICATE KEY UPDATE addressId = LAST_INSERT_ID(addressId) \n ";
        preparedStatement = conn.prepareStatement(addressInsert);
        preparedStatement.setString(1, a.getAddress());
        preparedStatement.setString(2, a.getAddress2());
        preparedStatement.setString(3, a.getPostalCode());
        preparedStatement.setString(4, a.getPhone());
        preparedStatement.setString(5, LoginController.getVerifiedUser());
        preparedStatement.setString(6, LoginController.getVerifiedUser());
        preparedStatement.executeUpdate();
        
        String customerInsert = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) \n" +
"VALUES (?, LAST_INSERT_ID(), ?, now(), ?, now(), ?) \n ";
        preparedStatement = conn.prepareStatement(customerInsert);
        preparedStatement.setString(1, a.getName());
        preparedStatement.setString(2, a.isActive()?"1":"0");
        preparedStatement.setString(3, LoginController.getVerifiedUser());
        preparedStatement.setString(4, LoginController.getVerifiedUser());
        preparedStatement.executeUpdate();
        
        DBConnection.closeConnection();
        return true;
        
    }
    
    public static boolean updateAggregator(AggregatorCust a, ObservableList<AggregatorCust> aggList) throws ClassNotFoundException, SQLException {

        PreparedStatement preparedStatement;
        DBConnection.startConnection();

        String countryUpdate = "UPDATE IGNORE country SET country = ?, lastUpdate = now(), lastUpdateBy = ? WHERE countryId = ?";
        preparedStatement = conn.prepareStatement(countryUpdate);
        preparedStatement.setString(1, a.getCountry());
        preparedStatement.setString(2, LoginController.getVerifiedUser());
        preparedStatement.setInt(3, a.getCountryId());
        preparedStatement.executeUpdate();      

        String cityUpdate = "UPDATE IGNORE city SET city = ?, lastUpdate = now(), lastUpdateBy = ? WHERE cityId = ?";
        preparedStatement = conn.prepareStatement(cityUpdate);
        preparedStatement.setString(1, a.getCity());
        preparedStatement.setString(2, LoginController.getVerifiedUser());
        preparedStatement.setInt(3, a.getCityId());
        preparedStatement.executeUpdate();
        
        String addressUpdate = "UPDATE IGNORE address SET address = ?, address2 = ?, postalCode = ?, phone = ?, lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?";
        preparedStatement = conn.prepareStatement(addressUpdate);
        preparedStatement.setString(1, a.getAddress());
        preparedStatement.setString(2, a.getAddress2());
        preparedStatement.setString(3, a.getPostalCode());
        preparedStatement.setString(4, a.getPhone());
        preparedStatement.setString(5, LoginController.getVerifiedUser());
        preparedStatement.setInt(6, a.getAddressId());
        preparedStatement.executeUpdate();
        
        String customerUpdate = "UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdate = now(), lastUpdateBy =? WHERE customerId = ?";
        preparedStatement = conn.prepareStatement(customerUpdate);
        preparedStatement.setString(1, a.getName());
        preparedStatement.setInt(2, a.getAddressId());
        preparedStatement.setString(3, a.isActive()?"1":"0");
        preparedStatement.setString(4, LoginController.getVerifiedUser());
        preparedStatement.setInt(5, a.getCustomerId());
        preparedStatement.executeUpdate();
        
        DBConnection.closeConnection();
        return true;
    }

    public static boolean deleteAggregator(AggregatorCust a) throws SQLException, ClassNotFoundException{
        DBConnection.startConnection();
        
        String appointmentDelete = "DELETE FROM appointment WHERE customerId = " +a.getCustomerId();
        Query.makeQuery(appointmentDelete);
        String customerDelete = "DELETE FROM customer WHERE customerId = " +a.getCustomerId();
        Query.makeQuery(customerDelete);
        String addressDelete = "DELETE address FROM address LEFT JOIN customer ON address.addressId = customer.addressId WHERE customer.addressId IS NULL";
        Query.makeQuery(addressDelete);
        String cityDelete = "DELETE city FROM city LEFT JOIN address ON city.cityId = address.cityId WHERE address.cityId IS NULL";
        Query.makeQuery(cityDelete);
        String countryDelete = "DELETE country FROM country LEFT JOIN city ON country.countryId = city.countryId WHERE city.countryId IS NULL";
        Query.makeQuery(countryDelete);
        
        DBConnection.closeConnection();
        return true;
    }
}
