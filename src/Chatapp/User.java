package Chatapp;
import java.util.ArrayList;
import java.util.Queue;

public class User {
    private int id;
    private int number;
    private  String f_name;
    private  String password;
    private  String prof_pic;
    private  String prof_desc;
    private ArrayList <Chatroom> chatrooms;
    private Queue<Story> Stories;
    private ArrayList <User>contacts;

    public User(int id, int number, String f_name, String password, String prof_pic, String prof_desc, ArrayList<Chatroom> chatrooms, Queue<Story> stories, ArrayList<User> contacts) {
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

    public Queue<Story> getStories() {
        return Stories;
    }

    public ArrayList<User> getContacts() {
        return contacts;
    }
        //Functions
    public void deleteCr( Chatroom chatroom){}
    public void sendMessage( Chatroom chatroom, String message){}
    public void deleteMessage(Chatroom chatroom, String message){}
    public void addStory(Story story){}
    public void deleteStory(Story story){}
    public void addContact(User user){}
    public void removeContact(User user){}
    public void addProfilePic(String pic_dir){}
    public void editDesc(){}

}
