package fr.triedge.todo.controllers;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Person;

import java.sql.SQLException;
import java.util.ArrayList;

public class PersonAction {

    private ArrayList<Person> persons;

    public String execute(){

        try {
            persons = DB.getInstance().getPersons();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "success";
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}
