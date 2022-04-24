package application;
import java.sql.*;

public class Main {


    public static void main(String[] args) {

        String sqlSelectAllPersons = "SELECT * FROM usser";
        String connectionUrl = "jdbc:mysql://localhost:3306/chatapp";

        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "password");

             PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("f_name");
                String lastName = rs.getString("l_name");
                System.out.println(id);
                // do something with the extracted data...
            }
        } catch (SQLException e) { e.printStackTrace();
            // handle the exception
        }
    }}