package fr.triedge.todo.database;

import fr.triedge.todo.model.Project;
import fr.triedge.todo.model.User;
import fr.triedge.todo.utils.PWDManager;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    private static DB instance;
    private Connection connection;

    private DB(){}

    public static DB getInstance(){
        if (instance == null){
            instance = new DB();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            PWDManager manager = new PWDManager();
            String pwd = manager.decode("JGJpdXNlclMjODg=");
            connection = DriverManager.getConnection("jdbc:mysql://triedge.ovh/amadeus","amadeus",pwd);
        }
        return connection;
    }

    public ArrayList<Project> getProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "select * from td_project order by project_name";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            Project p = new Project();
            p.setId(res.getInt("project_id"));
            p.setName(res.getString("project_name"));
            p.setPriority(res.getInt("project_priority"));
            projects.add(p);
        }
        res.close();
        stmt.close();
        return projects;
    }

    public User loginUser(String name, String password, boolean isEncrypted) throws SQLException {
        if (name == null || password == null)
            return null;
        String pwd = password;
        if (isEncrypted)
            pwd = new PWDManager().encode(password);
        String sql = "select * from ama_user where user_name='?' and user_password='?'";
        User user = null;
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, name);
        stmt.setString((int)2, pwd);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            user = new User();
            user.setUserId(res.getInt("user_id"));
            user.setName(res.getString("user_name"));
            user.setLevel(res.getInt("user_level"));
            user.setDisplayName(res.getString("user_display_name"));
        }
        res.close();
        stmt.close();

        return user;
    }
}
