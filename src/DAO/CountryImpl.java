
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

/**
 *
 * @author Dana K Lowe
 */
public class CountryImpl {
    static boolean act;
    
    public static Country getCountry(int countryId) throws SQLException, Exception {
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM country WHERE countryId = '" + countryId + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Country countryResult;
        if (result.next()){
            int countryIdG = result.getInt("countryId");
            String country = result.getString("country");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            countryResult = new Country(countryIdG, country, createDate, createdBy, lastUpdate, lastUpdateBy);
            return countryResult;
        }
        DBConnection.closeConnection();
        return null;
    }
    
    public static ObservableList<Country> getAllCountries() throws SQLException, Exception {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM country";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Country countryResult;
        while (result.next()){
            int countryId = result.getInt("countryId");
            String countryG = result.getString("country");
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            countryResult = new Country(countryId, countryG, createDate, createdBy, lastUpdate, lastUpdateBy);
            allCountries.add(countryResult);
        }
        DBConnection.closeConnection();
        return allCountries;
    }
}
