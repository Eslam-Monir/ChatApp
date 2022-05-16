package application;

import Chatapp.*;
import gui.*;

import java.sql.*;

public class Main {


    public static void main(String[] args) {

        try{ Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery( "select * from usser");
        while (resultSet.next()) {
       //     System.out.println(resultSet.getString("f_name"));
        }
        } catch (Exception e) {
            e.printStackTrace();

        }
        Chatroom cr=new Chatroom(16);

        User uu=new User(14);
        Message msg=new Message(5,uu,"sasdfa","23","23",1,"asdfasdf");
      //  User u=new User();

        App.sendMessage(cr,msg,uu);
//        uu.deleteMessage(msg);

          //  u.deleteStory(stor);
//         App.addContact(App.loggedUser , 1687387229 , "levi");
      //  User user = new User(1);

        App.setLoggedUser(uu);
       // uu.showContactInfo();

        //loading form
        LoginForm x = new LoginForm();
        x.showLog();

    }}