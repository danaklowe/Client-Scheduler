
package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 *
 * @author Dana K Lowe
 */

// aggregating class to hold appointment fields, customer fields, and user fields
public class AggregatorAppt {
    private int appointmentId;
    private int customerId;
    private int userId;
    private String customerName;
    private String userName;
    private String type;
    private ZonedDateTime localZdtStart;
    private ZonedDateTime localZdtEnd;
    private LocalDate apptDate;
    private LocalTime apptStartTime;
    private LocalTime apptEndTime;
    private long apptDuration;

    public AggregatorAppt(int appointmentId, int customerId, int userId, String customerName, String userName, String type, ZonedDateTime localZdtStart, ZonedDateTime localZdtEnd) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.customerName = customerName;
        this.userName = userName;
        this.type = type;
        this.localZdtStart = localZdtStart;
        this.localZdtEnd = localZdtEnd;
        
        apptDate = localZdtStart.toLocalDate();
        apptStartTime = localZdtStart.toLocalTime();
        apptEndTime = localZdtEnd.toLocalTime();
        apptDuration = Duration.between(apptStartTime, apptEndTime).toMinutes();
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getLocalZdtStart() {
        return localZdtStart;
    }

    public void setLocalZdtStart(ZonedDateTime localZdtStart) {
        this.localZdtStart = localZdtStart;
    }

    public ZonedDateTime getLocalZdtEnd() {
        return localZdtEnd;
    }

    public void setLocalZdtEnd(ZonedDateTime localZdtEnd) {
        this.localZdtEnd = localZdtEnd;
    }

    public LocalDate getApptDate() {
        return apptDate;
    }

    public void setApptDate(LocalDate apptDate) {
        this.apptDate = apptDate;
    }

    public LocalTime getApptStartTime() {
        return apptStartTime;
    }

    public void setApptStartTime(LocalTime apptStartTime) {
        this.apptStartTime = apptStartTime;
    }

    public LocalTime getApptEndTime() {
        return apptEndTime;
    }

    public void setApptEndTime(LocalTime apptEndTime) {
        this.apptEndTime = apptEndTime;
    }

    public long getApptDuration() {
        return apptDuration;
    }

    public void setApptDuration(long apptDuration) {
        this.apptDuration = apptDuration;
    }



    
}
