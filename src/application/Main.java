package application;
import Chatapp.Story;
import Chatapp.User;

import java.sql.*;

public class Main {


    public static void main(String[] args) {

        try{ Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery( "select * from usser");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("f_name"));
        }
        } catch (Exception e) {
            e.printStackTrace();

        }
        User uu=new User();
        Story stor =new Story(2,uu,"aqssadsdas","sdfasdfaasdasdsdf",3);
        User u=new User();
            u.deleteStory(stor);

    }}