package fr.triedge.todo.controllers;

import com.opensymphony.xwork2.ActionContext;

public class LoginAction {

    private String strutsLoginName;
    private String strutsLoginPassword;

    private String strutsAction;

    public String execute(){

        System.out.println(" -> execute Login Action");
        if (getStrutsAction() != null && getStrutsAction().equalsIgnoreCase("login")){
            System.out.println("# "+getStrutsLoginName());
            System.out.println("# "+getStrutsLoginPassword());
        }


        return "success";
        //return "askLogin";
    }

    public String processForm(){
        ActionContext.getContext().getParameters().forEach((k,v)-> System.out.println("p: "+k+"="+v));
        System.out.println(" -> processForm Login Action");
        System.out.println(" Struts Action: "+getStrutsAction());
        System.out.println(" Struts Login Name: "+getStrutsLoginName());
        if (getStrutsAction() != null && getStrutsAction().equalsIgnoreCase("login")){
            System.out.println("# "+getStrutsLoginName());
            System.out.println("# "+getStrutsLoginPassword());
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
