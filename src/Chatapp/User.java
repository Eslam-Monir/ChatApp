package Chatapp;
import java.sql.*;
import java.sql.DriverManager;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.*;


public class User {
    private int id;
    private int number;
    private String f_name;
    private String password;
    private String prof_pic;
    private String prof_desc;
    private ArrayList<Chatroom> chatrooms;
    private ArrayList<Story> Stories ;
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
            Stories =new ArrayList<>();


        }
        Stories.add(story);
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

    public void sendMessage(Chatroom chatroom, Message message, User sender) {
        try {

            //Create message
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String query = "INSERT INTO `chatapp`.`messages` (`id`, `sender_id`, `text`, `date`, `time`, `seen`, `type`) VALUES ( null , '" + sender.getId() + "', '" + message.getText() + "', '" + message.getDate() + "', '" + message.getTime() + "', '" + message.isSeen() + "', '" + message.getType() + "');\n";
            System.out.println(query);

            Statement statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int auto_msg_id = rs.getInt(1);

            boolean cr_exists = false;
            boolean user_Has_access = false;


            //Checks if Chatroom exists
            String verification_qry1 = "SELECT id from chatroom where id=" + chatroom.getId();
            Statement vrf_stmt = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(verification_qry1);

            if (resultSet.next()) {
                cr_exists = true;
            }

            //Checks if User has access to this chatroom
            String verification_qry2 = "SELECT cr_id from chatroom where cr_id=" + chatroom.getId() + "AND user_id=" + sender.getId();
            Statement vrf_stmt2 = connection.createStatement();
            ResultSet resultSet2 = statement.executeQuery(verification_qry1);
            if (resultSet2.next()) {
                user_Has_access = true;
            }


            //if there is no chatroom between user then:
            if (!cr_exists && !user_Has_access) {


                //First:
                // A chatroom will be created with default values:
                String query2 = "INSERT INTO `chatapp`.`chatroom` (`id`, `is_group`, `last_seen`) VALUES (null, '0', 'null');";
                System.out.println(query2);

                Statement statement2 = connection.createStatement();
                statement2.executeUpdate(query2, Statement.RETURN_GENERATED_KEYS);
                System.out.println("passedddd");
                ResultSet rs2 = statement2.getGeneratedKeys();

                rs2.next();
                int auto_cr_id = rs2.getInt(1);
                //Second:
                // will add the user to the users of the chatroom in cr_users

                String query4 = "INSERT INTO `chatapp`.`cr_users` (`cr_id`, `user_id`) VALUES ('" + auto_cr_id + "', '" + sender.getId() + "')";
                System.out.println(query4);

                Statement stmt = connection.createStatement();
                stmt.executeUpdate(query4, Statement.RETURN_GENERATED_KEYS);


                // Third:
                // Will create a message in message_to relation table with the newly created Chatroom
                String query3 = "INSERT INTO `chatapp`.`message_to` (`msg_id`, `user_id`, `cr_id`) VALUES ('" + auto_msg_id + "', '" + sender.getId() + "', '" + auto_cr_id + "')";

                System.out.println(query3);

                Statement statement3 = connection.createStatement();
                statement3.executeUpdate(query3);

            } else if (cr_exists && user_Has_access) {
                // Will create a message in message_to relation table with the existing Chatroom values
                String query4 = "INSERT INTO `chatapp`.`message_to` (`msg_id`, `user_id`, `cr_id`) VALUES ('" + auto_msg_id + "', '" + sender.getId() + "', '" + chatroom.getId() + "')";

                System.out.println(query4);

                Statement statement4 = connection.createStatement();
                statement4.executeUpdate(query4);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void deleteMessage(Message message) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM `chatapp`.`messages` WHERE (`id` = '" + message.getId() + "');";
            stmt.executeUpdate(query);


            String query1 = "DELETE FROM `chatapp`.`message_to` WHERE (`msg_id` = '" + message.getId() + "');";

            Statement stmt1 = connection.createStatement();
            stmt.executeUpdate(query1);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public static void addStory(String textt) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String query = "INSERT INTO story ( `user_id`, `time`, `text`) VALUES (" + App.loggedUser.getId() + ",'" + App.getTime() + "','" + textt + "');";
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

            }

            if (story.getUser().getId() != this.getId())
            {
                System.out.println("inside the if");
                for ( int id : user_ids)
                {
                    System.out.println("inside the for");
                    if (id != this.getId())
                    {
                        String fetchSeeCount = "Select seen from story where  id = " + story.getId();
                        statement.executeQuery(fetchSeeCount);
                        System.out.println(fetchSeeCount); //sout
                        seenCount = Integer.parseInt(fetchSeeCount);
                        seenCount += 1;
                        String updateQuery = "UPDATE `chatapp`.`story` SET seen = " + seenCount + " where id = " + story.getId();
                        statement.executeQuery(updateQuery);
                        System.out.println("seen count = " + seenCount);

                        String insertQuery = "INSERT INTO `chatapp`.`seen_story` ( `str_id` , `user_id`) VALUES ( `" + story.getId() + "` , `" + this.getId() + "`) ";
                        statement.executeQuery(insertQuery);
                        story.setSeen_count(seenCount);
                    }
                    else {
                        System.out.println("this user has already seen story");
                    }
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
}

