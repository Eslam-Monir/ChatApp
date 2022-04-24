package Chatapp;

public class Chatroom {
    private int id;
    private boolean isgroup;
    private String last_seen;
    //vector<User> users;
    //vector<Message>messages;
    private String cr_desc;

    public Chatroom(int id, boolean isgroup, String last_seen, String cr_desc) {
        this.id = id;
        this.isgroup = isgroup;
        this.last_seen = last_seen;
        this.cr_desc = cr_desc;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public void setCr_desc(String cr_desc) {
        this.cr_desc = cr_desc;
    }
//getters

    public int getId() {
        return id;
    }

    public boolean isIsgroup() {
        return isgroup;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public String getCr_desc() {
        return cr_desc;
    }

    public void showUsers(){};

    public String showLastSeen(){
        return "";
    };

    public void loadMessages(){};


}
