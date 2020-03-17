
package ClientApptDBMgmt;

import DAO.DBConnection;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dana K Lowe
 */
public class ClientApptDBMgmt extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/Login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        try {
            DBConnection.startConnection();
            launch(args);
            DBConnection.closeConnection();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }
    
}
