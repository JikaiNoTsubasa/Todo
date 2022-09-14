package fr.triedge.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Status {

    private int id;
    private String name;
    private String color;

    public Status(){}

    public Status(ResultSet res){
        try {
            setId(res.getInt("status_id"));
            setName(res.getString("status_name"));
            setColor(res.getString("status_color"));
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
