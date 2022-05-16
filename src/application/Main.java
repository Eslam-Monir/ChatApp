package application;
import Chatapp.*;
import java.sql.*;
//imports for the voice notes
import gui.*;
import Voicenote.*;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

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
//        Chatroom cr=new Chatroom(1,false,"233","asddfasdfsadfas");

        User uu=new User(1);
       // Message msg=new Message(24,uu,"sadfasdfa","23","23",1,"asdfasdf");
      //  User u=new User();

      //  uu.deleteMessage(msg);

          //  u.deleteStory(stor);
//         App.addContact(App.loggedUser , 1687387229 , "levi");
      //  User user = new User(1);

        App.setLoggedUser(uu);
       // uu.showContactInfo();


        //loading form
        //LoginForm x = new LoginForm();
        //x.showLog();
        SimpleSoundCapture ssc = new SimpleSoundCapture();
        ssc.open();
        JFrame f = new JFrame("Capture/Playback");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add("Center", ssc);
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 360;
        int h = 170;
        f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
        f.setSize(w, h);
        f.show();

    }}