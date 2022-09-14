package fr.triedge.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Entry {
    private int id;
    private String name;
    private String description;
    private User user;
    private Status status;
    private Project project;
    private int priority;

    public Entry(){}

    public Entry(ResultSet res){
        try {
            setId(res.getInt("entry_id"));
            setName(res.getString("entry_name"));
            setDescription(res.getString("entry_desc"));
            setPriority(res.getInt("entry_priority"));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
