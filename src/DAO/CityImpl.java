
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;

/**
 *
 * @author Dana K Lowe
 */
public class CityImpl {
    static boolean act;
    
    public static City getCity(int cityId) throws SQLException, Exception {
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM city WHERE cityId = '" + cityId + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        City cityResult;
        if (result.next()){
            int cityIdG = result.getInt("cityId");
            String city = result.getString("city");
            int countryId = result.getInt("countryId");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            cityResult = new City(cityIdG, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
            return cityResult;
        }
        DBConnection.closeConnection();
        return null;
    }
    
    public static ObservableList<City> getAllCities() throws SQLException, Exception {
        ObservableList<City> allCities = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM city";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        City cityResult;
        while (result.next()){
            int cityId = result.getInt("cityId");
            String cityG = result.getString("city");
            int countryId = result.getInt("countryId");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            cityResult = new City(cityId, cityG, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
            allCities.add(cityResult);
        }
        DBConnection.closeConnection();
        return allCities;
    }
}
