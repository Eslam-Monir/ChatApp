package Chatapp;
import java.sql.*;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.Queue;

public class User {
    private int id;
    private int number;
    private  String f_name;
    private  String password;
    private  String prof_pic;
    private  String prof_desc;
    private ArrayList <Chatroom> chatrooms;
    private Queue<Story> Stories;
    private ArrayList <User>contacts;

    public User(int id, int number, String f_name, String password, String prof_pic, String prof_desc, ArrayList<Chatroom> chatrooms, Queue<Story> stories, ArrayList<User> contacts) {
        this.id = id;
        this.number = number;
        this.f_name = f_name;
        this.password = password;
        this.prof_pic = prof_pic;
        this.prof_desc = prof_desc;
        this.chatrooms = chatrooms;
        Stories = stories;
        this.contacts = contacts;
    }
    public User(){
        this.id = 0;
        this.number = 0;
        this.f_name = null;
        this.password = null;
        this.prof_pic = null;
        this.prof_desc = null;
        this.chatrooms = null;
        Stories = null;
        this.contacts = null;
    }




    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getF_name() {
        return f_name;
    }

    public String getPassword() {
        return password;
    }

    public String getProf_pic() {
        return prof_pic;
    }

    public String getProf_desc() {
        return prof_desc;
    }

    public ArrayList<Chatroom> getChatrooms() {
        return chatrooms;
    }

    public Queue<Story> getStories() {
        return Stories;
    }

    public ArrayList<User> getContacts() {
        return contacts;
    }


    //Functions
    public void deleteCr( Chatroom chatroom){
        String query= "DELETE FROM chatroom where id =" + chatroom.getId();
        Statement statement = App.connect_to_database();
        try {
            statement.executeUpdate(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendMessage( Chatroom chatroom, Message message,User sender){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String query = "INSERT INTO `chatapp`.`messages` (`id`, `sender_id`, `text`, `date`, `time`, `seen`, `type`) VALUES ('" + message.getId() + "', '" + sender.getId() + "', '" + message.getText() + "', '" + message.getDate() + "', '" + message.getTime() + "', '" + message.isSeen() + "', '" + message.getType() + "');\n";
            System.out.println(query);

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // STILL WORK IN PROGRESS (MONIR)
    }
    public void deleteMessage(Chatroom chatroom, String message){ /*STILL WORK IN PROGRESS (MONIR)*/}

    public void addStory(Story story)
    {

      try
      {
          Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
          String query = "INSERT INTO story ( `user_id`, `time`, `text`) VALUES ("+story.getUser().id+",'"+story.getTime()+"','"+story.getText()+"');";
          System.out.println(query);

               Statement statement = connection.createStatement();
                statement.executeUpdate(query);

      } catch (Exception e)
       {
          e.printStackTrace();

       }

    }


        public void deleteStory(Story story){
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
                String query = "DELETE FROM `chatapp`.`story` WHERE (`id` ="+story.getId()+" );";
                System.out.println(query);

                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

            }catch (Exception e) {
                e.printStackTrace();

            }
        }
    public void addContact(User user,int number,String name)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();

            // Query to get the Id of the added_id
            String query1 = "SELECT id FROM usser WHERE number =" + number;
            ResultSet resultSet = statement.executeQuery(query1);

            resultSet.next(); // i won the error of before start youshaaaaa!!!
            String temp ;
            temp = resultSet.getString("id");
            int addedId = Integer.parseInt(temp);

            // query  to insert into contacts
            String query2 = "INSERT INTO contacts ( `adder_id`, `added_id`, `name`) VALUES ("+user.getId()+",'"+ addedId +"','"+ name +"');";
            statement.executeUpdate(query2);

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void removeContact(User adder,User added){
        String query= "DELETE FROM contacts where adder_id =" + adder.getId()+"and added_id="+added.getId();
        Statement statement = App.connect_to_database();
        try {
            statement.executeUpdate(query);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addProfilePic(User user,String pic_dir){
        /* Note : i did "\"" + pic_dir + "\"" to skip error because he need string between " "  */

        String query= "UPDATE  chatapp.usser" + " SET prof_pic = " +   "\"" + pic_dir + "\"" + " WhERE id ="+user.getId();
        Statement statement = App.connect_to_database();
        try {

            statement.executeUpdate(query);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void editDesc(User user,String desc){

        String query= "UPDATE  chatapp.usser" + " SET prof_desc = " +   "\"" + desc + "\"" + " WhERE id ="+user.getId();
        Statement statement = App.connect_to_database();
        try {

            statement.executeUpdate(query);

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

}
