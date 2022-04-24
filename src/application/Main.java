package application;
import java.sql.*;

public class Main {


    public static void main(String[] args) {

        String sqlSelectAllPersons = "SELECT * FROM usser";
        String connectionUrl = "jdbc:mysql://localhost:3306/chatapp";

        try{ Connection connection = DriverManager.getConnection(connectionUrl, "root", "password");

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery( "select * from usser");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("f_name"));
        }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }}