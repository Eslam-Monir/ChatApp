package Chatapp;

import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Chatroom {
    private int id;
    private boolean isgroup;
    private String last_seen;
    ArrayList<User> users;
    ArrayList<Message>messages;
    private String cr_desc;

    public Chatroom(int id, boolean isgroup, String last_seen, String cr_desc) {
        this.id = id;
        this.isgroup = isgroup;
        this.last_seen = last_seen;
        this.cr_desc = cr_desc;
    }



    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public void setCr_desc(String cr_desc) {
        this.cr_desc = cr_desc;
    }
//getters

    public int getId() {
        return id;
    }

    public  boolean isIsgroup() {
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
    public void showUsers(){};

    public String showLastSeen(){
        return "";
    };

    public void loadMessages(){};


}
