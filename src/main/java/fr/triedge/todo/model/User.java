package fr.triedge.todo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private int userId;
    private String name;
    private String displayName;
    private String password;
    private int level;

    public User(){}

    public User(ResultSet res){
        try {
            setUserId(res.getInt("user_id"));
            setName(res.getString("user_name"));
            setDisplayName(res.getString("user_display_name"));
            setLevel(res.getInt("user_level"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
