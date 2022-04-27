package Chatapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class Story{
    private int id;

    private User user;

    private String time;

    private   String text;

    private int seen_count;

    public Story(int id) {
        this.id = id;
    }

    public Story(int id, User user, String time, String text, int seen_count) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.text = text;
        this.seen_count = seen_count;
    }
    public Story(){
        this.id=0;
        this.user = null;
        this.time = null;
        this.text = null;
        this.seen_count = 0;
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


    //getters

    public int getId() {
        return id;
    }

    public User getUser() {
        return this.user;
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
    public Story openStory(Story story) // the parameter is the clicked story ❤?
    {
        try {
            int seenCount = 0;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp", "root", "password");
            String fetchSeeCount = "Select seen from story where  id = "+ story.getId();
            seenCount = Integer.parseInt(fetchSeeCount);
            seenCount += 1 ;
            String query = "UPDATE `chatapp`.`story` SET seen = " + seenCount +" where id = " +  story.getId();
            System.out.println("seen count = " + seenCount);
 // ahsouf men ele 3aml seen w mayeb2aash as a duplicate (maye3mlsh seen tane)
            //Just once yabn elkkelab




        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return story;
    };

   public boolean seen(){
       return true ;
   }

   };
