package Chatapp;
import java.sql.*;
import java.time.LocalDateTime; //  Used for the fetchTime()
import java.time.format.DateTimeFormatter;//  Used for the fetchTime()

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

    }

    public void loadStories()
    {

    }

    public void loadContacts()
    {

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
