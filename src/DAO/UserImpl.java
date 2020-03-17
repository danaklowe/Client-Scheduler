
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

/**
 *
 * @author Dana K Lowe
 */

// Create, Read, Update, & Delete implementation
public class UserImpl {
    static boolean act;
    
    public static User getUser(String userName) throws SQLException, Exception {
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM user WHERE userName = '" + userName + "'";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        User userResult;
        if (result.next()){
            int userId = result.getInt("userId");
            String userNameB = result.getString("userName");
            String password = result.getString("password");
            int active = result.getInt("active");
            if(active == 1) act = true;
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            userResult = new User(userId, userNameB, password, act, createDate, createdBy, lastUpdate, lastUpdateBy);
            return userResult;
        }
        DBConnection.closeConnection();
        return null;
    }
    
    public static ObservableList<User> getAllUsers() throws SQLException, ParseException, ClassNotFoundException{
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM user";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        User userResult;
        while(result.next()){
            int userId = result.getInt("userId");
            String userNameB = result.getString("userName");
            String password = result.getString("password");
            int active = result.getInt("active");
            if(active == 1) act = true;
            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");
            userResult = new User(userId, userNameB, password, act, createDate, createdBy, lastUpdate, lastUpdateBy);
            allUsers.add(userResult);
        }
        DBConnection.closeConnection();
        return allUsers;        
    }
    
    public static boolean verifyCredentials(String user, String pass) throws Exception{
        User user1 = getUser(user);
        if (user1 != null)
        {return pass.equals(user1.getPassword());}
        return false;
    }
    
}
