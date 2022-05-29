package Chatapp;
import java.sql.*;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.mysql.cj.xdevapi.Result;

import java.util.*;


public class User {
    private int id;
    private int number;
    private String f_name;
    private String password;
    private String prof_pic;
    private String prof_desc;
    private ArrayList<Chatroom> chatrooms;
    private ArrayList<Story> Stories =new ArrayList<>();
    private ArrayList<User> contacts;

    public User(int id, int number, String f_name, String password, String prof_pic, String prof_desc, ArrayList<Chatroom> chatrooms, ArrayList<Story> stories, ArrayList<User> contacts) {
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

    public User() {
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

    public User(int id, int number, String f_name, String password, String prof_pic, String prof_desc) {
        this.id = id;
        this.number = number;
        this.f_name = f_name;
        this.password = password;
        this.prof_pic = prof_pic;
        this.prof_desc = prof_desc;
    }

    public User(int id) {
        this.id = id;
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

    public ArrayList<Story> getStories() {
        return Stories;
    }

    public ArrayList<User> getContacts() {
        return contacts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProf_pic(String prof_pic) {
        this.prof_pic = prof_pic;
    }

    public void setProf_desc(String prof_desc) {
        this.prof_desc = prof_desc;
    }

    public void setChatrooms(ArrayList<Chatroom> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public void setStories(ArrayList<Story> stories) {

        Stories = stories;
    }
    public void addStoryToStories(Story story) {
        if(this.Stories == null) {
            ArrayList<Story> Stories =new ArrayList<>();


        }
        this.Stories.add(story);

    }
    public void addContactToContacts(User user) {
        if(this.contacts == null) {
            contacts =new ArrayList<>();


        }
        this.contacts.add(user);
    }
    public void addChatroomToChatrooms(Chatroom chatroom) {
        if(this.chatrooms == null) {
            chatrooms =new ArrayList<>();


        }
        this.chatrooms.add(chatroom);
    }


    public void setContacts(ArrayList<User> contacts) {
        this.contacts = contacts;
    }

    //Functions
    public void deleteCr(Chatroom chatroom) {
        String query = "DELETE FROM chatroom where id =" + chatroom.getId();
        Statement statement = App.connect_to_database();
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public void addStory(String textt) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String query = "INSERT INTO story ( user_id, time, text ) VALUES (" + App.loggedUser.getId() + ",'" + App.getTime() + "','" + textt + "');";
            System.out.println(query);

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }




    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", number=" + number +
                ", f_name='" + f_name + '\'' +
                ", password='" + password + '\'' +
                ", prof_pic='" + prof_pic + '\'' +
                ", prof_desc='" + prof_desc + '\'' +
                '}';
    }

    public void deleteStory(Story story) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String query = "DELETE FROM `chatapp`.`story` WHERE (`id` =" + story.getId() + " );";
            System.out.println(query);

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void removeContact(User adder, User added) {
        String query = "DELETE FROM contacts where adder_id =" + adder.getId() + "and added_id=" + added.getId();
        Statement statement = App.connect_to_database();
        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProfilePic(User user, String pic_dir) {
        /* Note : i did "\"" + pic_dir + "\"" to skip error because he need string between " "  */

        String query = "UPDATE  chatapp.usser" + " SET prof_pic = " + "\"" + pic_dir + "\"" + " WhERE id =" + user.getId();
        Statement statement = App.connect_to_database();
        try {

            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editDesc(User user, String desc) {

        String query = "UPDATE  chatapp.usser" + " SET prof_desc = " + "\"" + desc + "\"" + " WhERE id =" + user.getId();
        Statement statement = App.connect_to_database();
        try {

            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public Story openStory(Story story ) // the parameter is the clicked story ❤?
    {
        try {
            int seenCount = 0;
            ArrayList<Integer> user_ids = new ArrayList<>();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();

            String getIDsQuery = "SELECT * FROM seen_story where str_id =  " +story.getId(); // get user ids who seen the story
            System.out.println(getIDsQuery); //sout
            ResultSet rs1 = statement.executeQuery(getIDsQuery);

            while (rs1.next()) {
                String id = rs1.getString("user_id");
                user_ids.add(Integer.parseInt(id));

            } // this array(user_ids) now carries the ids of users who saw the story before
            System.out.println("done");
            if (!App.loadStories(App.loggedUser).contains(story.getUser().getId())) //check if the story not opened by its publisher
            {


                if (!user_ids.contains(App.loggedUser.getId()))
                {
                    String fetchSeeCount = "Select * from story where  id = " + story.getId();
                    ResultSet w = statement.executeQuery(fetchSeeCount);

                    w.next();
                    seenCount = Integer.parseInt(w.getString("seen"));
                    seenCount += 1;



                    String updateQuery = "UPDATE `chatapp`.`story` SET seen = " + seenCount + " where id = " + story.getId();
                    statement.executeUpdate(updateQuery);


                    String insertQuery = " INSERT INTO `chatapp`.`seen_story`  VALUES ( " + story.getId() + " , " + App.loggedUser.getId() + ") ";

                    statement.executeUpdate(insertQuery);

                    story.setSeen_count(seenCount);

                }
                else {
                    System.out.println("this user has already seen story");
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return story;
    };

    public void show_stories() {
        for (Story elem : Stories) {
            System.out.println(elem.toString());
        }

    }


    public void show_contacts() {
        System.out.println(toString() +"\nContacts :");
        for (User elem : contacts) {

            System.out.println("\t" + elem.toString());
        }

    }
    public void show_chatrooms() {
        System.out.println(toString() +"\nChatrooms :");
        for (Chatroom elem : chatrooms) {

            System.out.println( elem.toString());
        }



    }
    public void showContactInfo()
    { 
        int addedId = 0;
        int addedIdss = 0;
        LinkedList<Integer> contacts = new LinkedList<>();
        LinkedList<Integer> validatedContacts = new LinkedList<>(); //hghdfgddbh
        try
        {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
        Statement statement = connection.createStatement();

        String getAddedId = "SELECT added_id FROM contacts where adder_id  = " + App.loggedUser.getId(); //get  ids of contacts of logged user
        ResultSet rst = statement.executeQuery(getAddedId);
        
        while(rst.next())
        {
            contacts.add(rst.getInt("added_id"));
        }
        while(!contacts.isEmpty())
        {
            addedId = contacts.remove();
            String getAdderId = "SELECT adder_id FROM  contacts WHERE adder_id  = " + addedId + " AND added_id = " + App.loggedUser.getId();
            ResultSet rstt = statement.executeQuery(getAdderId);
            if(rstt.next())
            {
                validatedContacts.add(rstt.getInt("adder_id"));
            }
        }
            while(!validatedContacts.isEmpty())
            {
                addedIdss = validatedContacts.remove(); 
             //   String getName = "SELECT name FROM contacts WHERE added_id = " + x ;
                String getName = "SELECT * FROM contacts,usser WHERE added_id = " + addedIdss  + " AND added_id = usser.id ";

                ResultSet rst_name = statement.executeQuery(getName);
                if(!rst_name.next())
                {
                System.out.println("no names"); // will be deleted
                break;
                }
                System.out.println(rst_name.getString("contacts.name"));
                System.out.println(rst_name.getString("prof_pic"));

            }
        
        // String name = rstt.getString("name");
        // System.out.println("name := " + name);
        
        }catch(Exception e)
        {e.printStackTrace();}
    }


    public void editProfilePic(User user, String prof_pic) {

        String query = "UPDATE  chatapp.usser" + " SET prof_pic = " + "'" + prof_pic + "'" + " WhERE id =" + user.getId();
        Statement statement = App.connect_to_database();
        try {

            statement.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void undoMessage(int msgId)
    {
        try
        {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
        Statement statement = connection.createStatement();

        String deleteMessage = "DELETE FROM message_to where where msg_id = " + msgId;
        statement.executeQuery(deleteMessage);    
        
        }catch(Exception e)
        {e.printStackTrace();}

    }


    public void UserData() {


        try {
            Statement statement = App.connect_to_database();
            ResultSet contact_query = statement.executeQuery("select * from contacts,usser where usser.id=" + id +
                    " and contacts.added_id = usser.id ");
            contact_query.next();
            setF_name(contact_query.getString("name"));
            setId(Integer.parseInt(contact_query.getString("id")));
            setNumber(Integer.parseInt(contact_query.getString("number")));
            setPassword(contact_query.getString("password"));
            setProf_pic(contact_query.getString("prof_pic"));
            setProf_desc(contact_query.getString("prof_desc"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

// if (story.getUser().getId() != this.getId()) //check if the story not opened by its publisher
// {
//     for ( int id : user_ids)
//     {
//         if (id != App.loggedUser.getId())// check if the story not opened by same user before
//         {
//             String fetchSeeCount = "Select seen from story where  id = " + story.getId();
//             statement.executeQuery(fetchSeeCount);

//             seenCount = Integer.parseInt(fetchSeeCount);
//             seenCount += 1;

//             String updateQuery = "UPDATE `chatapp`.`story` SET seen = " + seenCount + " where id = " + story.getId();
//             statement.executeQuery(updateQuery);

//             String insertQuery = "INSERT INTO `chatapp`.`seen_story` ( `str_id` , `user_id`) VALUES ( `" + story.getId() + "` , `" + this.getId() + "`) ";
//             statement.executeQuery(insertQuery);
            
//             story.setSeen_count(seenCount);
//         }
//         else {
//             System.out.println("this user has already seen story");
//         }
//     }
// }