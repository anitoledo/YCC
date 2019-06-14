package yucatancountryclub.com.ycc.Model;

import java.util.ArrayList;

/**
 * Created by Zazu on 21/02/18.
 */

public class Report {
    private String pk;
    private String user;
    private String title;
    private String description;
    private String status;
    private String date;
    private String time;
    private ArrayList<String> images;
    private ArrayList<String> posts;

    public ArrayList<String> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public Report(String pk, String user, String title, String description, String status, String date, String time, ArrayList<String> images, ArrayList<String> posts){
        this.pk = pk;
        this.user = user;
        this.title = title;
        this.description = description;
        this.status = status;
        this.date = date;
        this.time = time;
        this.images = images;
        this.posts = posts;

    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
