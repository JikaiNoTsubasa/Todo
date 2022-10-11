package fr.triedge.todo.controllers;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.opensymphony.xwork2.ActionContext;
import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.User;

import java.sql.SQLException;

public class LoginAction {

    private String strutsLoginName;
    private String strutsLoginPassword;

    private String strutsAction;

    public String execute(){


        return "success";
        //return "askLogin";
    }

    public String processForm(){
        if (getStrutsAction() != null && getStrutsAction().equalsIgnoreCase("login")){
            try {
                User user = DB.getInstance().loginUser(getStrutsLoginName(), getStrutsLoginPassword(), true);
                if (user != null) {
                    ActionContext.getContext().getSession().put("user", user);
                    //System.out.println("Login success");
                    return "success";
                } else {
                    System.out.println("Login failed");
                    return "askLogin";
                }
            }catch (CommunicationsException e){
                try {
                    DB.getInstance().resetConnection();
                    processForm();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        return "success";
    }

    public String getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(String strutsAction) {
        this.strutsAction = strutsAction;
    }

    public String getStrutsLoginName() {
        return strutsLoginName;
    }

    public void setStrutsLoginName(String strutsLoginName) {
        this.strutsLoginName = strutsLoginName;
    }

    public String getStrutsLoginPassword() {
        return strutsLoginPassword;
    }

    public void setStrutsLoginPassword(String strutsLoginPassword) {
        this.strutsLoginPassword = strutsLoginPassword;
    }
}
