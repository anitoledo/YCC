package yucatancountryclub.com.ycc.Model;

/**
 * Created by Zazu on 15/02/18.
 */

public class Contact {
    private String title;
    private String description;
    private String location;
    private String schedule;
    private String phone;
    private String email;

    public Contact(String title, String description, String location, String schedule, String phone, String email){
        this.title = title;
        this.description = description;
        this.location = location;
        this.schedule = schedule;
        this.phone = phone;
        this.email = email;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
