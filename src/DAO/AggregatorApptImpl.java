
package DAO;

import static DAO.DBConnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AggregatorAppt;
import view_controller.LoginController;

/**
 *
 * @author Dana K Lowe
 */
public class AggregatorApptImpl {
    public static ObservableList<AggregatorAppt> getAllAggregatorAppts() throws ClassNotFoundException, SQLException, ParseException{
        ObservableList<AggregatorAppt> allAggregatorAppts = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT c.customerId, c.customerName, u.userId, u.userName, a.appointmentId, a.type, a.start, a.end " +
"FROM customer AS c INNER JOIN appointment AS a ON c.customerId = a.customerId INNER JOIN user AS u ON a.userId = u.userId " +
"ORDER BY a.appointmentId";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        AggregatorAppt aggApptResult;
        ZoneId zid = ZoneId.systemDefault();
        while(result.next()){
            int appointmentId = result.getInt("appointmentId");
            int customerId = result.getInt("customerId");
            int userId = result.getInt("userId");
            String userName = result.getString("userName");
            String customerName = result.getString("customerName");
            String type = result.getString("type");
            LocalDateTime startLDT = result.getTimestamp("start").toLocalDateTime();
            LocalDateTime endLDT = result.getTimestamp("end").toLocalDateTime();
            
            ZonedDateTime utcZdtStart = startLDT.atZone(ZoneId.of("UTC"));
            ZonedDateTime localZdtStart = utcZdtStart.withZoneSameInstant(zid);
            ZonedDateTime utcZdtEnd = endLDT.atZone(ZoneId.of("UTC"));
            ZonedDateTime localZdtEnd = utcZdtEnd.withZoneSameInstant(zid);
                       
            aggApptResult = new AggregatorAppt(appointmentId, customerId, userId, customerName, userName, type, localZdtStart, localZdtEnd);
            allAggregatorAppts.add(aggApptResult);
        }
        DBConnection.closeConnection();
        return allAggregatorAppts;
    }
    
    
    public static boolean addAggregatorAppt (AggregatorAppt a) throws ClassNotFoundException, SQLException{
        DBConnection.startConnection();
        String sqlStatement = "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
        PreparedStatement sql = conn.prepareStatement(sqlStatement);
			sql.setInt(1, a.getCustomerId());
			sql.setInt(2, a.getUserId());
			sql.setString(3, "");
			sql.setString(4, "");
			sql.setString(5, "");
			sql.setString(6, "");
			sql.setString(7, a.getType());
			sql.setString(8, "");
			sql.setTimestamp(9, Timestamp.valueOf(a.getLocalZdtStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
			sql.setTimestamp(10, Timestamp.valueOf(a.getLocalZdtEnd().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
			sql.setString(11, LoginController.getVerifiedUser() );
			sql.setString(12, LoginController.getVerifiedUser());
			sql.execute();
        
        DBConnection.closeConnection();
        return true;
    }
    
    public static boolean updateAggregatorAppt (AggregatorAppt a) throws SQLException, ClassNotFoundException{
        DBConnection.startConnection();
        String sqlStatement = "UPDATE IGNORE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, url = ?, "
                + "start = ?, end = ?, lastUpdate = now(), lastUpdateBy = ? WHERE appointmentId = ?";
        PreparedStatement sql = conn.prepareStatement(sqlStatement);
            sql.setInt(1, a.getCustomerId());
            sql.setInt(2, a.getUserId());
            sql.setString(3, "");
            sql.setString(4, "");
            sql.setString(5, "");
            sql.setString(6, "");
            sql.setString(7, a.getType());
            sql.setString(8, "");
            sql.setTimestamp(9, Timestamp.valueOf(a.getLocalZdtStart().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            sql.setTimestamp(10, Timestamp.valueOf(a.getLocalZdtEnd().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            sql.setString(11, LoginController.getVerifiedUser() );
            sql.setInt(12, a.getAppointmentId());
            sql.execute();
        
        DBConnection.closeConnection();
        return true;            
    }
    
    public static boolean deleteAggregatorAppt (AggregatorAppt a) throws ClassNotFoundException, SQLException {
        DBConnection.startConnection();
        String sqlStatement = "DELETE FROM appointment WHERE appointmentId = " + a.getAppointmentId();
        Query.makeQuery(sqlStatement);
        DBConnection.closeConnection();
        return true;
    }
    
    public static AggregatorAppt isApptConflicting (ObservableList<AggregatorAppt> apptOL, AggregatorAppt aggAppt, int userId){
        for(AggregatorAppt a: apptOL){
            //Compares selected appointment with the list of appointments to see if 1. consultants are same, 2. the appointments are different, & 3. their times overlap.
            if(a.getUserId() == userId && (a.getAppointmentId() != aggAppt.getAppointmentId()) &&(aggAppt.getLocalZdtStart().isBefore( a.getLocalZdtEnd()))  &&  (aggAppt.getLocalZdtEnd().isAfter( a.getLocalZdtStart())))
                return a;
        }
        return null;        
    }
    
    public static boolean isApptAfterHours (String startingHour, String startingMinute, String duration) {
        if(startingHour.equals("16") && ((startingMinute.equals("15") && (duration.equals("1 hour"))) ||
                                         (startingMinute.equals("30") && (duration.equals("45 minutes") || duration.equals("1 hour"))) ||
                                         (startingMinute.equals("45") && (duration.equals("30 minutes") || duration.equals("45 minutes") || duration.equals("1 hour"))))) {
            return true;
        }
        return false;
    }
    
}
