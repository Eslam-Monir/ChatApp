package Chatapp;

public class Message {
    private int id;
    private User sender;
    private String text;
    private String date;
    private String time;
    private boolean seen;

    public Message(int id, User sender, String text, String date, String time, boolean seen) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.time = time;
        this.seen = seen;
    }
    //setter

    public void setId(int id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    //getters

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isSeen() {
        return seen;
    }
}
