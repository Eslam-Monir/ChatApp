package Chatapp;

public class App
{
    private User logged_user;

    private Chatroom chatrooms;

    private static String date;

    private static String time;


    public App(User logged_user, Chatroom chatrooms) {
        this.logged_user = logged_user;
        this.chatrooms = chatrooms;
    }
            // Getters And Setters
    public User getLogged_user() {
        return logged_user;
    }

    public void setLogged_user(User logged_user) {
        this.logged_user = logged_user;
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

    }

    public void fetchTime()
    {

    }
}
