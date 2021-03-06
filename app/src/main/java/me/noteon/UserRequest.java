package me.noteon;

import java.util.Date;

public class UserRequest {
private String username;
private String first_name;
private String last_name;
private String email;
private String date_joined =  new Date(System.currentTimeMillis()).toString();
private String formID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\": \"" + username + "\"," +
                "\"first_name\": \"" + first_name + "\"," +
                "\"last_name\": \"" + last_name + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"date_joined\": \"" + date_joined + "\"," +
                "\"formID\": \"" + formID + "\"," +
                '}';
    }
}
