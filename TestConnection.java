
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection connection =
            DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Glamify Database is Ready!");
        }
    }
}
