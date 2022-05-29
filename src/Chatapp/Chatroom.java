package Chatapp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.mysql.cj.Query;

import java.sql.*;

public class Chatroom {
    private int id;
    private String name;
    private boolean isgroup;
    private static String last_seen;
    ArrayList<User> users;
   public ArrayList<Message> messages;
    private String cr_desc;

    public String getName() {
        return name;
    }

    public Chatroom() {

    }
    public Chatroom(int id,String name, boolean isgroup) {
        this.id = id;
        this.name=name;
        this.isgroup = isgroup;

    }

    public Chatroom(int id) {
        this.id=id;
    }


    //setters
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", isgroup=" + isgroup +
                ", last_seen='" + last_seen + '\'' +
                '}';
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }

    public void setLast_seen(String last_seen) {
        Chatroom.last_seen = last_seen;
    }
     
    public static String setLast_seen(int id)
    {   App.fetchTime();
        Chatroom.last_seen = App.getDate();
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            
            String updateLastSeen = " UPDATE chatroom SET last_seen = "+last_seen+" where id = "+ id +"";
            System.out.println(updateLastSeen);
            statement.executeUpdate(updateLastSeen);
            
        }catch(Exception e)
        {e.printStackTrace();}
        System.out.println(last_seen);
        return last_seen;
    }   

    public void setCr_desc(String cr_desc) {
        this.cr_desc = cr_desc;
    }
//getters

    public int getId() {
        return id;
    }

    public  boolean isGroup() {
        boolean is_true = false ;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            String query = "select is_group from chatroom where id = "+id;

            ResultSet r = statement.executeQuery("select is_group from chatroom where id = "+id);
            r.next();

            if(r.getString("is_group").equals("1") )

            {

                is_true=true ;
                System.out.println("This chat room is group");
                return is_true;
            }
            else if( r.getString("is_group").equals("0") )
            {
                is_true = false;
                System.out.println("This chat room is private");
                return is_true;

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }






        return is_true;
    }
    public String getLast_seen() {
        return last_seen;
    }

    public String getCr_desc() {
        return cr_desc;
    }


    //Functions
    public void showUsers(int RoomID) {

           if (findRoom(RoomID) != 0)
           {
               Queue <Integer> ids = new LinkedList<>();
               try {

                   Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
                   Statement statement = connection.createStatement();

                   // progressing if chatroom id exists or doesnt exist

                   String idQuery = "select * from cr_users where cr_id = " + RoomID;
                   ResultSet rs1 = statement.executeQuery(idQuery);
                   while (rs1.next()) {
                       String id = rs1.getString("user_id");
                       ids.add(Integer.parseInt(id));
                   }
                  
                   while (!ids.isEmpty())
                   {
                       int poppedId = ids.remove();
                       String query2 = "SELECT * From usser where id = " + poppedId;
                       ResultSet rs2 = statement.executeQuery(query2);

                       if (!rs2.next()) 
                       {
                           System.out.println("Results are not found !");
                       } else 
                       {
                           do {
                               String outputNames = rs2.getString("f_name");
                               System.out.println("Users : " + outputNames);
                           } while (rs2.next());
                       }
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
    }



    public String showLastSeen()
    {
        return "";
    }

    ;

    public void loadMessages() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();
            ResultSet query_for_messages = statement.executeQuery("select * from messages , message_to , usser where cr_id = "+ id +
                    " and  msg_id = messages.id " +
                    "and sender_id =usser.id  order by date , time");


            if(this.messages == null)
                this.messages=new ArrayList<>();

            else if (this.messages.size()!=0)
                this.messages.clear();
            while (query_for_messages.next()) {

                Message temporary = new Message();
                temporary.setDate(query_for_messages.getString("date"));
                temporary.setId(Integer.parseInt(query_for_messages.getString("id")));
                temporary.setSeen(Integer.parseInt(query_for_messages.getString("seen")));
                temporary.setTime(query_for_messages.getString("time"));
                temporary.setText(query_for_messages.getString("text"));
                User temp=new User(Integer.parseInt(query_for_messages.getString("sender_id"))
                        ,Integer.parseInt(query_for_messages.getString("number"))
                        , query_for_messages.getString("f_name")
                        , query_for_messages.getString("password")
                        , query_for_messages.getString("prof_pic")
                        , query_for_messages.getString("prof_desc"));
                temporary.setSender(temp);
                temporary.setType(query_for_messages.getString("type"));

                this.messages.add(temporary);
                temporary = null ;
                temp = null ;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        ;

    }

    ;

    public int findRoom (int RoomID) // Searches for a chatroom room and returns the id (If found)
    {
        int roomId = 0;
        boolean roomFound = false;
        int foundRoomId = -1;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();


            String Query = "select * from cr_users where cr_id = " + RoomID;
            ResultSet rsss = statement.executeQuery(Query);

            while (rsss.next()) {
                String rooms = rsss.getString("cr_id");
                roomId  = Integer.parseInt(rooms);
            }

                if (roomId == RoomID) {
                    roomFound = true;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!roomFound) {
            System.out.println("Room Not Found !");
        } else {
            System.out.println(" Room " + RoomID + " found ");
            foundRoomId = RoomID;
        }
        return foundRoomId;
    }
}
