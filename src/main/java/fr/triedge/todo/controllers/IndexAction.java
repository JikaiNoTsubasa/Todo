package fr.triedge.todo.controllers;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Project;

import java.sql.SQLException;
import java.util.ArrayList;

public class IndexAction {

    private ArrayList<Project> projects;

    public String execute(){
        try {
            projects = DB.getInstance().getProjects();
        } catch (SQLException e) {
            System.err.println("Error getting projects. "+e.getMessage());
            throw new RuntimeException(e);
        }

        return "success";
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
