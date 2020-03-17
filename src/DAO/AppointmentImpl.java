
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

/**
 *
 * @author Dana K Lowe
 */
public class AppointmentImpl {
    public static ObservableList<Appointment> getAllAppointments() throws ClassNotFoundException, SQLException, ParseException{
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        DBConnection.startConnection();
        String sqlStatement = "SELECT * FROM appointment";
        Query.makeQuery(sqlStatement);
        ResultSet result = Query.getResult();
        Appointment apptResult;
        while(result.next()){
            int appointmentId = result.getInt("appointmentId");
            int customerId = result.getInt("customerId");
            int userId = result.getInt("userId");
            String type = result.getString("type");
            LocalDateTime start = result.getTimestamp("start").toLocalDateTime();
            LocalDateTime end = result.getTimestamp("end").toLocalDateTime();
            

            LocalDateTime createDate = result.getTimestamp("createDate").toLocalDateTime();
            String createdBy = result.getString("createdBy");
            LocalDateTime lastUpdate = result.getTimestamp("lastUpdate").toLocalDateTime();
            String lastUpdateBy = result.getString("lastUpdateBy");

            apptResult = new Appointment(appointmentId, customerId, userId, type, start, end, createDate, createdBy, lastUpdate, lastUpdateBy);
            allAppointments.add(apptResult);
        }
        DBConnection.closeConnection();
        return allAppointments;
    }
}
