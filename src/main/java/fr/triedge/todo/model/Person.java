package fr.triedge.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Person {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String location;
    private String lang;
    private Date lastVisited;
    private boolean isAdmin;
    private boolean canEditMol;
    private String info;

    public Person(){}

    public Person(ResultSet res){
        try {
            setId(res.getInt("person_id"));
            setUsername(res.getString("person_username"));
            setFirstname(res.getString("person_firstname"));
            setLastname(res.getString("person_lastname"));
            setLocation(res.getString("person_location"));
            setLastVisited(res.getTimestamp("person_last_visited"));
            setAdmin(res.getBoolean("person_is_admin"));
            setCanEditMol(res.getBoolean("person_mol_edit"));
            setInfo(res.getString("person_info"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Date lastVisited) {
        this.lastVisited = lastVisited;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isCanEditMol() {
        return canEditMol;
    }

    public void setCanEditMol(boolean canEditMol) {
        this.canEditMol = canEditMol;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
