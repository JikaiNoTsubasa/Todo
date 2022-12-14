package fr.triedge.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Project {
    private int id;
    private String name;
    private int priority;

    public Project(){}

    public Project(ResultSet res){
        try {
            setId(res.getInt("project_id"));
            setName(res.getString("project_name"));
            setPriority(res.getInt("project_priority"));
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
