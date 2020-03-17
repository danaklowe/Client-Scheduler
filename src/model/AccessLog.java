
package model;

/**
 *
 * @author Dana K Lowe
 */
public class AccessLog {
    private String timeStamp;
    private String userName;
    private String password;
    private String success;

    public AccessLog(String timeStamp, String userName, String password, String success) {
        this.timeStamp = timeStamp;
        this.userName = userName;
        this.password = password;
        this.success = success;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    
    
}
