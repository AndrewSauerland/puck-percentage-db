import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainTest {
  
  public static void main(String args[]) {

    Connection conn = null;

    try {

            // Define the database URL, username, and password
            String url = "jdbc:mysql://puckpercentage.cd2c6s2ykki4.us-east-2.rds.amazonaws.com:3306/stats";
            String username = "admin";
            String password = "datadriven871";

            // Establish a connection to the database
            conn = DriverManager.getConnection(url, username, password);

            // Your logic to interact with the database
            System.out.println("Connected to the database!");

        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        } finally {
            // Close the connection
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

  }

}
