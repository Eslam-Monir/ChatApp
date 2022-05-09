package Chatapp;
import java.sql.*;
import java.time.LocalDateTime; //  Used for the fetchTime()
import java.time.format.DateTimeFormatter;//  Used for the fetchTime()
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;


public class App
{
    private User loggedUser;

    private Chatroom chatrooms;

    private static String date;

    private static String time;


    public App(User logged_user, Chatroom chatrooms) {
        this.loggedUser = logged_user;
        this.chatrooms = chatrooms;
    }

    public App() {
    }

    // Getters And Setters
    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Chatroom getChatrooms() {
        return chatrooms;
    }

    public void setChatrooms(Chatroom chatrooms) {
        this.chatrooms = chatrooms;
    }

    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        App.date = date;
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        App.time = time;
    }

    //Functions

    public void loadGui()
    {

    }

    public void loadChatrooms()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet chatroom_query = statement.executeQuery("select * from cr_users ,usser,chatroom where user_id=" + loggedUser.getId() +
                    " and user_id = usser.id " +
                    " and cr_id = chatroom.id order by last_seen ");

            if(loggedUser.getChatrooms()!=null && loggedUser.getChatrooms().size()!=0 ){

                loggedUser.getChatrooms().clear();
            }


            while (chatroom_query.next()){


                Chatroom temporary_chatroom = new Chatroom();

                temporary_chatroom.setId(Integer.parseInt(chatroom_query.getString("chatroom.id")));
                temporary_chatroom.setIsgroup(chatroom_query.getBoolean("chatroom.id"));

                temporary_chatroom.setLast_seen(chatroom_query.getString("chatroom.last_seen"));
                loggedUser.setId(Integer.parseInt(chatroom_query.getString("usser.id")));
                loggedUser.setNumber(Integer.parseInt(chatroom_query.getString("number")));
                loggedUser.setF_name(chatroom_query.getString("f_name"));
                loggedUser.setPassword(chatroom_query.getString("password"));
                loggedUser.setProf_pic(chatroom_query.getString("prof_pic"));
                loggedUser.setProf_desc(chatroom_query.getString("prof_desc"));


                loggedUser.addChatroomToChatrooms(temporary_chatroom);


                temporary_chatroom = null ;





            }
            //loggedUser.show_chatrooms();




        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static ArrayList<User> getAllUsers()
    { //returns all users from database to verify user login
     ArrayList<User> users=new ArrayList<User>();
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            ResultSet rs=statement.executeQuery("SELECT * from usser");

            User user;
            while (rs.next()){ //initializing user to put in array
               user =new User(Integer.parseInt(rs.getString("id"))
                        ,Integer.parseInt(rs.getString("number"))
                        , rs.getString("f_name")
                        , rs.getString("password")
                        , rs.getString("prof_pic")
                        , rs.getString("prof_desc"));
                //adds the user to the array
               users.add(user);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return users;


    }
    public void loadStories(User user) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet story_query = statement.executeQuery("select * from story,usser where user_id=" + user.getId() +
                                                                                          " and user_id = usser.id order by  time");
            // remove stories from queue to not make redundancy in queue
            if(user.getStories()!=null && user.getStories().size()!=0 ){

                user.getStories().clear();
            }

            //put Stories in story's Queue from database
            while (story_query.next()){


                Story temporary_story = new Story();

                temporary_story.setId(Integer.parseInt(story_query.getString("id")));

                temporary_story.setTime(story_query.getString("time"));
                temporary_story.setText(story_query.getString("text"));
                User temporary_user=new User(Integer.parseInt(story_query.getString("user_id"))
                        ,Integer.parseInt(story_query.getString("number"))
                        , story_query.getString("f_name")
                        , story_query.getString("password")
                        , story_query.getString("prof_pic")
                        , story_query.getString("prof_desc"));

                temporary_story.setUser(temporary_user);
               user.addStoryToStories(temporary_story);

                temporary_story = null ;
                temporary_user = null ;




            }





        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void loadContacts()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet contacts_query = statement.executeQuery("select * from contacts,usser where adder_id=" + loggedUser.getId() +
                    " and adder_id = usser.id ");
            // delete array_list
            if(loggedUser.getContacts()!=null && loggedUser.getContacts().size()!=0 ){

                loggedUser.getContacts().clear();
            }

            //put query in object
            while (contacts_query.next()){


                User temporary_contact = new User();

                temporary_contact.setId(Integer.parseInt(contacts_query.getString("added_id")));

                temporary_contact.setF_name(contacts_query.getString("name"));
                loggedUser.setId(Integer.parseInt(contacts_query.getString("adder_id")));
                loggedUser.setNumber(Integer.parseInt(contacts_query.getString("number")));
                loggedUser.setF_name(contacts_query.getString("f_name"));
                loggedUser.setPassword(contacts_query.getString("password"));
                loggedUser.setProf_pic(contacts_query.getString("prof_pic"));
                loggedUser.setProf_desc(contacts_query.getString("prof_desc"));


                //put object in array_list
                loggedUser.addContactToContacts(temporary_contact);

                temporary_contact = null ;





            }

            //loggedUser.show_contacts();



        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public String showDesc(User user)
    {
        return "hello";
    }

    public void fetchTime()
    {
        DateTimeFormatter Date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime Datenow = LocalDateTime.now();

        DateTimeFormatter Time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime Timenow = LocalDateTime.now();

        setDate(Date.format(Datenow));
        setTime(Time.format(Timenow));
    }

    static  public Statement connect_to_database(){
        String connectionUrl = "jdbc:mysql://localhost:3306/chatapp";

        try{ Connection connection = DriverManager.getConnection(connectionUrl, "root", "password");

            return connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }
}
