package fr.triedge.todo.controllers;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;

import java.sql.SQLException;
import java.util.ArrayList;

public class ArchiveAction {

    private ArrayList<Entry> archivedEntries;

    public String execute(){
        try {
            archivedEntries = DB.getInstance().getArchivedEntries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    public ArrayList<Entry> getArchivedEntries() {
        return archivedEntries;
    }

    public void setArchivedEntries(ArrayList<Entry> archivedEntries) {
        this.archivedEntries = archivedEntries;
    }
}
