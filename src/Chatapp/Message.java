package Chatapp;

public class Message {
    private int id;
    private User sender;
    private String text;
    private String date;
    private String time;
    private int seen;
    private String type;

    public Message(int id, User sender, String text, String date, String time, int seen,String type) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.time = time;
        this.seen = seen;
        this.type=type;
    }
    public Message() {

    }
    public Message(int id) {
        this.id =id;
    }

    //setter

    public void setType(String type) {
        this.type = type;
    }

    public Message(User sender, String text, String date, String time, int seen, String type) {
        this.id= 0;
        this.sender = sender;
        this.text = text;
        this.date = date;
        this.time = time;
        this.seen = seen;
        this.type = type;
    }

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

    public void setSeen(int seen) {
        this.seen = seen;
    }
    //getters

    public String getType() {
        return type;
    }

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

    public int isSeen() {
        return seen;
    }
}
