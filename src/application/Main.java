package application;

import Chatapp.*;

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
        Chatroom cr=new Chatroom(1,false,"233","asddfasdfsadfas");

        User uu=new User();
        Message msg=new Message(24,uu,"sadfasdfa","23","23",1,"asdfasdf");
        User u=new User();

        uu.deleteMessage(msg);

          //  u.deleteStory(stor);
            u.addContact(uu , 2002 , "dazaii");




    }}