package Chatapp;
import java.sql.*;
import java.time.LocalDateTime; //  Used for the fetchTime()
import java.time.format.DateTimeFormatter;//  Used for the fetchTime()
import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class App {
    public static User loggedUser;

    private Chatroom chatrooms;

    private static String date;

    private static String time;


    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        App.loggedUser = loggedUser;
    }

    public App(User logged_user, Chatroom chatrooms) {
        this.loggedUser = logged_user;
        this.chatrooms = chatrooms;
    }

    public App() {
    }

    // Getters And Setters
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

    public void loadGui() {

    }

    public void loadChatrooms() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet chatroom_query = statement.executeQuery("select * from cr_users ,usser,chatroom where user_id=" + loggedUser.getId() +
                    " and user_id = usser.id " +
                    " and cr_id = chatroom.id order by last_seen ");

            if (loggedUser.getChatrooms() != null && loggedUser.getChatrooms().size() != 0) {

                loggedUser.getChatrooms().clear();
            }


            while (chatroom_query.next()) {


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


                temporary_chatroom = null;


            }
            //loggedUser.show_chatrooms();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static ArrayList<User> getAllUsers() { //returns all users from database to verify user login
        ArrayList<User> users = new ArrayList<User>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from usser");

            User user;
            while (rs.next()) { //initializing user to put in array
                user = new User(Integer.parseInt(rs.getString("id"))
                        , Integer.parseInt(rs.getString("number"))
                        , rs.getString("f_name")
                        , rs.getString("password")
                        , rs.getString("prof_pic")
                        , rs.getString("prof_desc"));
                //adds the user to the array
                users.add(user);

            }

        } catch (Exception e) {
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
            if (user.getStories() != null && user.getStories().size() != 0) {

                user.getStories().clear();
            }

            //put Stories in story's Queue from database
            while (story_query.next()) {


                Story temporary_story = new Story();

                temporary_story.setId(Integer.parseInt(story_query.getString("id")));

                temporary_story.setTime(story_query.getString("time"));
                temporary_story.setText(story_query.getString("text"));
                User temporary_user = new User(Integer.parseInt(story_query.getString("user_id"))
                        , Integer.parseInt(story_query.getString("number"))
                        , story_query.getString("f_name")
                        , story_query.getString("password")
                        , story_query.getString("prof_pic")
                        , story_query.getString("prof_desc"));

                temporary_story.setUser(temporary_user);
                user.addStoryToStories(temporary_story);

                temporary_story = null;
                temporary_user = null;


            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void addContact(User user, int number, String name) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();

            // Query to get the Id of the added_id
            String getID = "SELECT id FROM usser WHERE number =" + number;
            ResultSet resultSet = statement.executeQuery(getID);

            if (resultSet.next())// i won the error of before start youshaaaaa!!!
            {

                String id = resultSet.getString("id");
                int addedId = Integer.parseInt(id);

                String adder_added = "SELECT * FROM contacts WHERE adder_id = " + user.getId() + " AND added_id = " + addedId;
                System.out.println(adder_added);
                ResultSet rstt = statement.executeQuery(adder_added);

                if (!rstt.next()) {

                    // query  to insert into contacts
                    String query2 = "INSERT INTO contacts ( `adder_id`, `added_id`, `name`) VALUES (" + user.getId() + ",'" + addedId + "','" + name + "');";
                    statement.executeUpdate(query2);
                    try {
                        String createChatroom = "INSERT INTO chatroom ( `is_group` , `last_seen` ) Values ( '0' , 'null' )";
                        System.out.println(createChatroom);
                        statement.executeUpdate(createChatroom, Statement.RETURN_GENERATED_KEYS);

                        ResultSet rs = statement.getGeneratedKeys();
                        rs.next();

                        int crID = rs.getInt(1);

                        //Puts newly created contact with the adder in the created chatroom
                        String first_user_insert = "INSERT INTO cr_users ( `cr_id` , `user_id` ) Values ( " + crID + " , " + user.getId() + ") ";
                        String second_user_insert = "INSERT INTO cr_users ( `cr_id` , `user_id` ) Values ( " + crID + " , " + addedId + ") ";

                        statement.executeUpdate(first_user_insert);
                        statement.executeUpdate(second_user_insert);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    System.out.println("You have already added this user ");

            } else {
                System.out.println("This is contact doesn't have this app installed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadContacts() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();

            ResultSet contacts_query = statement.executeQuery("select * from contacts,usser where adder_id=" + loggedUser.getId() +
                    " and adder_id = usser.id ");
            // delete array_list
            if (loggedUser.getContacts() != null && loggedUser.getContacts().size() != 0) {

                loggedUser.getContacts().clear();
            }

            //put query in object
            while (contacts_query.next()) {


                User temporary_contact = new User();

                temporary_contact.setId(Integer.parseInt(contacts_query.getString("id")));

                temporary_contact.setF_name(contacts_query.getString("name"));
                loggedUser.setId(Integer.parseInt(contacts_query.getString("id")));
                loggedUser.setNumber(Integer.parseInt(contacts_query.getString("number")));
                loggedUser.setF_name(contacts_query.getString("f_name"));
                loggedUser.setPassword(contacts_query.getString("password"));
                loggedUser.setProf_pic(contacts_query.getString("prof_pic"));
                loggedUser.setProf_desc(contacts_query.getString("prof_desc"));


                //put object in array_list
                loggedUser.addContactToContacts(temporary_contact);

                temporary_contact = null;


            }

            //loggedUser.show_contacts();


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public String showDesc(User user) {
        return "hello";
    }

    public static void fetchTime() {
        Timer timer = new Timer("Display Timer");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                   // This will be executed every second

                   DateTimeFormatter Date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                   LocalDateTime Datenow = LocalDateTime.now();
           
                   DateTimeFormatter Time = DateTimeFormatter.ofPattern("HH:mm:ss");
                   LocalDateTime Timenow = LocalDateTime.now();
           
                   setDate(Date.format(Datenow));
                   setTime(Time.format(Timenow));
                }
            };

           // This will invoke the timer every second
            timer.scheduleAtFixedRate(task, 1000, 1000);
    }

    public static void addChatroom(Stack<User> users,String name) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            String createChatroom = "INSERT INTO chatroom ( `is_group` , `last_seen` ,`cr_name`) Values ( '1' , 'null' ,'"+name+"')";

            statement.executeUpdate(createChatroom, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();

            int crID = rs.getInt(1);
            System.out.println("CR ID :  " + crID);

            String loggedinsert = "INSERT INTO cr_users ( `cr_id` , `user_id` ) Values ( " + crID + " , " + loggedUser.getId() + ") ";

            statement.executeUpdate(loggedinsert);
            while (!users.empty()) {
                User addedUser = new User();
                addedUser = users.pop();
                String insertUsers = "INSERT INTO cr_users ( `cr_id` , `user_id` ) Values ( " + crID + " , " + addedUser.getId() + ") ";
                System.out.println(insertUsers);
                statement.executeUpdate(insertUsers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static public Statement connect_to_database() {
        String connectionUrl = "jdbc:mysql://localhost:3306/chatapp";

        try {
            Connection connection = DriverManager.getConnection(connectionUrl, "root", "password");

            return connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }


    static public Stack<User> usersGetter(ArrayList<String> users, User loggedUser) { // takes an array lists of users names and returns Array of their users
        Stack<User> Users =new Stack<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();


            for (int i = 0; i < users.size(); i++) {
                //will get the id of the contact
                String get_contact_id_query = "Select added_id From contacts where adder_id= " + loggedUser.getId() + " AND name= '" + users.get(i)+"'";
                System.out.println(get_contact_id_query);
                ResultSet contact_id = statement.executeQuery(get_contact_id_query);
                contact_id.next();

               String cID=contact_id.getString("added_id");
                String get_user_query="Select * from usser where id=" + cID;

                ResultSet users_ = statement.executeQuery(get_user_query);

                users_.next();
                User user=new User(Integer.parseInt(users_.getString("id"))
                        , Integer.parseInt(users_.getString("number"))
                        , users_.getString("f_name")
                        , users_.getString("password")
                        , users_.getString("prof_pic")
                        , users_.getString("prof_desc"));


                Users.push(user);



            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        return Users;
}
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) // removing duplicates from an ArrayList
    {
        ArrayList<T> newList = new ArrayList<T>();
        // Traverse through the first list
        for (T element : list) {

            if (!newList.contains(element)) {

                newList.add(element);
            }
        }
        return newList;
    }

}

