import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnect {
  
    public String varOne;
    public String varTwo;
    public String varThree;

    public dbConnect() {
        System.out.println("Mapping variables");
        varOne = System.getenv("DATABASE_URL");
        varTwo = System.getenv("DATABASE_USERNAME");
        varThree = System.getenv("DATABASE_PASSWORD");
    }

  public static void main(String args[]) {

    dbConnect testClass = new dbConnect();
    System.out.println("Printing variables pulled from env");
    System.out.println(testClass.varOne);
    System.out.println(testClass.varTwo);
    System.out.println(testClass.varThree);

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
