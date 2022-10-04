package fr.triedge.todo.controllers.ajax;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Project;
import fr.triedge.todo.tpl.Template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class AjaxProjectAction {

    private String strutsAction;
    private String strutsProjectName;
    private String strutsProjectId;
    private String strutsProjectPriority;
    private InputStream inputStream;

    public String execute() throws UnsupportedEncodingException {
        String result = "";

        if (getStrutsAction() != null){
            if (getStrutsAction().equalsIgnoreCase("newProjectForm")){
                result = processNewProjectForm();
            }else if (getStrutsAction().equalsIgnoreCase("createProject")){
                result = processCreateProject();
            }else if (getStrutsAction().equalsIgnoreCase("askConfirmDelete")){
                result = processAskConfirmDelete();
            }else if (getStrutsAction().equalsIgnoreCase("deleteProject")){
                result = processDeleteProject();
            }
        }

        inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        return "success";
    }

    private String processDeleteProject() {
        if (getStrutsProjectId() == null)
            return "ID is null";
        int id = Integer.parseInt(getStrutsProjectId());
        try {
            DB.getInstance().deleteProject(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private String processAskConfirmDelete() {
        if (getStrutsProjectId() == null) {
            return "ID is null";
        }
        Template tpl = new Template("/html/askConfirmDeleteProject.html");
        int id = Integer.parseInt(getStrutsProjectId());
        try {
            Project p = DB.getInstance().getProject(id);
            tpl.setParameter("##id##", p.getId());
            tpl.setParameter("##name##", p.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tpl.generate();
    }

    private String processCreateProject() {
        if (getStrutsProjectName() == null || getStrutsProjectPriority() == null)
            return "";
        int prio = Integer.parseInt(getStrutsProjectPriority());
        try {
            DB.getInstance().createNewProject(getStrutsProjectName(), prio);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private String processNewProjectForm(){
        Template tpl = new Template("/html/newProjectForm.html");
        return tpl.generate();
    }

    public String getStrutsProjectId() {
        return strutsProjectId;
    }

    public void setStrutsProjectId(String strutsProjectId) {
        this.strutsProjectId = strutsProjectId;
    }

    public String getStrutsProjectName() {
        return strutsProjectName;
    }

    public void setStrutsProjectName(String strutsProjectName) {
        this.strutsProjectName = strutsProjectName;
    }

    public String getStrutsProjectPriority() {
        return strutsProjectPriority;
    }

    public void setStrutsProjectPriority(String strutsProjectPriority) {
        this.strutsProjectPriority = strutsProjectPriority;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(String strutsAction) {
        this.strutsAction = strutsAction;
    }
}
