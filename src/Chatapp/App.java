package Chatapp;
import java.sql.*;
import java.time.LocalDateTime; //  Used for the fetchTime()
import java.time.format.DateTimeFormatter;//  Used for the fetchTime()
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
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
        fetchTime();
        return time;
    }

    public static void setTime(String time) {
        App.time = time;
    }

    //Functions

    public void loadGui() {

    }

    public static void loadChatrooms() {
        int ID = -1;
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
                ID = temporary_chatroom.getId();
                temporary_chatroom.setIsgroup(chatroom_query.getBoolean("chatroom.is_group"));

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

            //dasbdjdb

        } catch (Exception e) {
            e.printStackTrace();

        }
        Chatroom.setLast_seen(ID);
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

    public static void sendMessage(Chatroom chatroom, Message message, User sender) {
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


    public static ArrayList<Story>  loadStories(User user) {
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
        return user.getStories();
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



                    try {
                        //check for existing contact chatroom




                        String qury2 = "SELECT * FROM contacts WHERE adder_id ="+user.getId()+ " AND added_id ="+ addedId ;
                        ResultSet resultSet1 = statement.executeQuery(qury2);

                       boolean f1= resultSet1.next();
                        String query3 = "SELECT * FROM contacts WHERE adder_id ="+addedId+ " AND added_id ="+ user.getId() ;
                        ResultSet resultSet2 = statement.executeQuery(query3);
                       boolean f2 =resultSet2.next();
                        System.out.println(f1+ " "+ f2);
                        if(!f1 & !f2) {


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
                        }
                        String query2 = "INSERT INTO contacts ( `adder_id`, `added_id`, `name`) VALUES (" + user.getId() + ",'" + addedId + "','" + name + "');";

                        statement.executeUpdate(query2);
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
                temporary_contact.setId(Integer.parseInt(contacts_query.getString("id")));
                temporary_contact.setNumber(Integer.parseInt(contacts_query.getString("number")));

                temporary_contact.setPassword(contacts_query.getString("password"));
                temporary_contact.setProf_pic(contacts_query.getString("prof_pic"));
                temporary_contact.setProf_desc(contacts_query.getString("prof_desc"));


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
            timer.scheduleAtFixedRate(task, 0, 1000);
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
    static public User userGetter(String user, User loggedUser) { // takes contact name and returns its user
        User aloooo = new User();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");

            Statement statement = connection.createStatement();


            //will get the id of the contact
            String get_contact_id_query = "Select added_id From contacts where adder_id= " + loggedUser.getId() + " AND name= '" + user + "'";
            System.out.println(get_contact_id_query);
            ResultSet contact_id = statement.executeQuery(get_contact_id_query);
            contact_id.next();

            String cID = contact_id.getString("added_id");
            String get_user_query = "Select * from usser where id=" + cID;

            ResultSet user_ = statement.executeQuery(get_user_query);

            user_.next();
            User new_user = new User(Integer.parseInt(user_.getString("id"))
                    , Integer.parseInt(user_.getString("number"))
                    , user_.getString("f_name")
                    , user_.getString("password")
                    , user_.getString("prof_pic")
                    , user_.getString("prof_desc"));
                    
                    aloooo = new_user;


                } catch (Exception e) {
            e.printStackTrace();
            
        }
        return aloooo;
    }
    
    public static int load1to1Chatroom(int id)
    {
        int foundRoomId = -1;
        LinkedList<Integer> chatrooms = new LinkedList<>();

        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();
            
            
            String getRoomIds = "SELECT cr_id FROM cr_users WHERE user_id = " + App.loggedUser.getId();
            ResultSet rst1 = statement.executeQuery(getRoomIds);

            while(rst1.next())
            {
                chatrooms.add(rst1.getInt("cr_id"));
            }

        
            int is_group; 
            int popped;
                for (int i = 0 ; i < chatrooms.size(); i++)
                {
                    popped = chatrooms.removeLast();
                    String validateChatroom = "SELECT is_group FROM chatroom where id = " + popped; //returns 1 row
                    ResultSet rst2 = statement.executeQuery(validateChatroom);
                    
                        if(rst2.next())
                        {
                            is_group = rst2.getInt("is_group");
                            if (is_group == 0) {
                                chatrooms.addFirst(popped);
                            }
                        }
          
                }
            System.out.println("--------------------");
                
            System.out.println("chatroomIds = " + chatrooms); // sout to check if the list is filtered

            for(int cr_Id : chatrooms)
            {
             String getRoom = "SELECT cr_id FROM cr_users  WHERE user_id =  " + id  + " AND cr_id =  " + cr_Id;
             ResultSet rst3 = statement.executeQuery(getRoom);

                 if(rst3.next())
                    {
                        foundRoomId = rst3.getInt("cr_id");
                        System.out.println("Room Found = " + foundRoomId);
                    }      
            } 
        
        }catch(Exception e)
        {
            e.printStackTrace();
        }    
    return foundRoomId;
      
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

    public static void deleteMessage(Message message) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM `chatapp`.`messages` WHERE (`id` = '" + message.getId() + "');";
            stmt.executeUpdate(query);
            System.out.println(query);

            String query1 = "DELETE FROM `chatapp`.`message_to` WHERE (`msg_id` = '" + message.getId() + "');";
            System.out.println(query1);
            Statement stmt1 = connection.createStatement();
            stmt.executeUpdate(query1);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public static String getContactName(int id){
        String name="";

        try{  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();

            String get_name = "SELECT name FROM contacts where added_id =  " +id +" and adder_id= " +loggedUser.getId();
//            System.out.println(get_name); //sout
            ResultSet rs = statement.executeQuery(get_name); rs.next();
          name =rs.getString("name");

//            System.out.println(name);


        }catch(Exception e)
        {e.printStackTrace();}

    return name;

    }

    public static ArrayList<Chatroom> loadGroup() {


      ArrayList<Chatroom> chatrooms=new ArrayList<>();



try {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
    Statement statement = connection.createStatement();


    String query = "SELECT * FROM chatapp.cr_users , chatroom where cr_id=chatroom.id and user_id=" + loggedUser.getId() + " and chatroom.is_group=1 ;";

    ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Chatroom inserted_cr=new Chatroom(rs.getInt("id"),rs.getString("cr_name"),rs.getBoolean("is_group"));
                chatrooms.add(inserted_cr);
            }



}    catch(Exception e)
{
    e.printStackTrace();
}
        return chatrooms;
    }

    public static int load1toMChatroom(int id)
    {
        int foundRoomId = -1;
        LinkedList<Integer> checkList = new LinkedList<>();
        LinkedList<Integer> chatroomIds = new LinkedList<>();

        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            Statement statement = connection.createStatement();


            String getRoomIds = "SELECT cr_id FROM cr_users WHERE user_id = " + App.loggedUser.getId();
            ResultSet rst1 = statement.executeQuery(getRoomIds);

            while(rst1.next())
            {
                checkList.add(rst1.getInt("cr_id"));
            }

            System.out.println("checked list = " + checkList);  // sout to check if the list is not empty

            int removedRoom; // haghayr esmo
            int popped;

            while (!checkList.isEmpty())
            {
                popped = checkList.remove();

                String validateChatroom = "SELECT is_group FROM chatroom where id = " + popped;
                ResultSet rst2 = statement.executeQuery(validateChatroom);

                while(rst2.next())
                {
                    removedRoom = rst2.getInt("is_group");
                    if (removedRoom == 1) {
                        chatroomIds.add(popped);
                    }
                }

            }
            System.out.println("--------------------");

            System.out.println("chatroomIds = " + chatroomIds); // sout to check if the list is filtered


            while(!chatroomIds.isEmpty())
            {
                String getRoom = "SELECT cr_id FROM cr_users  WHERE user_id =  " + id  + " AND cr_id =  " + chatroomIds.remove();
                ResultSet rst3 = statement.executeQuery(getRoom);

                if(rst3.next())
                {
                    foundRoomId = rst3.getInt("cr_id");
                    System.out.println("Room Found = " + foundRoomId);
                }
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return foundRoomId;

    }


}