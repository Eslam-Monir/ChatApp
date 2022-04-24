package Chatapp;

public class Story{
    private int id;

    private User user;

    private String time;

    private   String text;

    private int seen_count;



    public Story(int id, User user, String time, String text, int seen_count) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.text = text;
        this.seen_count = seen_count;
    }


    //setters

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSeen_count(int seen_count) {
        this.seen_count = seen_count;
    }

    //getters

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public int getSeen_count() {
        return seen_count;
    }

    //Functions
    public void openStory(){};

   public boolean seen(){
       return true ;
   };
}
