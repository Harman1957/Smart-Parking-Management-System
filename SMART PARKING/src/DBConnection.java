import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/parking_db",
                "root",
                "******abc124"//your password
            );

            return con;

        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed!");
            System.out.println(e.getMessage()); // important
            return null;
        }
    }
}
