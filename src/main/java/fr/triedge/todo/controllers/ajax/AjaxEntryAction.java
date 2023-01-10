package fr.triedge.todo.controllers.ajax;

import com.opensymphony.xwork2.ActionContext;
import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.model.Project;
import fr.triedge.todo.model.User;
import fr.triedge.todo.tpl.Template;
import fr.triedge.todo.utils.Utils;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AjaxEntryAction {

    private String strutsAction;
    private String strutsEntryName;
    private String strutsEntryDesc;
    private String strutsEntryId;
    private String strutsStatusId;
    private String strutsPriority;
    private String strutsProjectId;
    private InputStream inputStream;

    public String execute() throws Exception {
        System.out.println("Ajax action: "+getStrutsAction());
        String result = "";
        if (getStrutsAction() != null){
            if (getStrutsAction().equalsIgnoreCase("refreshEntries")){
                result = processRefreshEntries();
            }else if (getStrutsAction().equalsIgnoreCase("newEntry")){
                result = processNewEntry();
            }else if (getStrutsAction().equalsIgnoreCase("showEntry")){
                result = processShowEntry();
            }else if (getStrutsAction().equalsIgnoreCase("askConfirmDelete")){
                result = processAskConfirmDelete();
            }else if (getStrutsAction().equalsIgnoreCase("deleteEntry")){
                result = processDeleteEntry();
            }else if (getStrutsAction().equalsIgnoreCase("changeStatus")){
                result = processChangeStatus();
            }else if (getStrutsAction().equalsIgnoreCase("editEntry")){
                result = processEditEntry();
            }else if (getStrutsAction().equalsIgnoreCase("updateEntry")){
                result = processUpdateEntry();
            }
        }
        inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        return "success";
    }

    private String processUpdateEntry() throws SQLException {
        if (getStrutsEntryId() == null)
            return "Id is null";
        int id = Integer.parseInt(getStrutsEntryId());
        String name = getStrutsEntryName();
        String desc = getStrutsEntryDesc();
        int prio = Integer.parseInt(getStrutsPriority());
        int projectId = Integer.parseInt(getStrutsProjectId());
        Project p = new Project();
        p.setId(projectId);
        Entry e = new Entry();
        e.setId(id);
        e.setName(name);
        e.setDescription(desc);
        e.setPriority(prio);
        e.setProject(p);
        DB.getInstance().updateEntry(e, id);
        return "";
    }

    private String processEditEntry() throws SQLException {
        if (getStrutsEntryId() == null)
            return "Id is null";
        int id = Integer.parseInt(getStrutsEntryId());
        Entry entry = DB.getInstance().getEntry(id);
        ArrayList<Project> projects = DB.getInstance().getProjects();

        StringBuilder selectPrio = new StringBuilder();
        for(int i = 1; i <=10; ++i){
            String sel = "";
            if (entry.getPriority() == i)
                sel = "selected";
            selectPrio.append("<option value=\"").append(i).append("\" ").append(sel).append(">").append(i).append("</option>");
        }

        StringBuilder selectProject = new StringBuilder();
        for (Project p : projects){
            String sel = "";
            if (entry.getProject().getId() == p.getId())
                sel = "selected";
            selectProject
                    .append("<option value=\"")
                    .append(p.getId())
                    .append("\" ")
                    .append(sel)
                    .append(">")
                    .append(p.getName())
                    .append("</option>");
        }

        Template tpl = new Template("/html/editEntryForm.html");
        tpl
                .setParameter("##id##", entry.getId())
                .setParameter("##name##", entry.getName())
                .setParameter("##desc##", entry.getDescription())
                .setParameter("##prio##", selectPrio.toString())
                .setParameter("##project##", selectProject.toString());
        return tpl.generate();
    }

    private String processChangeStatus() throws SQLException {
        if (getStrutsEntryId() == null || getStrutsStatusId() == null)
            return "Ids are null";
        int entryId = Integer.parseInt(getStrutsEntryId());
        int statusId = Integer.parseInt(getStrutsStatusId());
        DB.getInstance().changeStatus(entryId, statusId);
        return "";
    }

    private String processDeleteEntry() throws SQLException {
        if (getStrutsEntryId() == null)
            return "Entry id is NULL";
        int id = Integer.parseInt(getStrutsEntryId());
        DB.getInstance().deleteEntry(id);
        return "";
    }

    private String processAskConfirmDelete() throws SQLException {
        if (getStrutsEntryId() == null)
            return "Entry id is NULL";
        int id = Integer.parseInt(getStrutsEntryId());
        Entry entry = DB.getInstance().getEntry(id);
        Template tpl = new Template("/html/askConfirmDeleteEntry.html");
        tpl
                .setParameter("##id##", entry.getId())
                .setParameter("##name##", entry.getName());
        return tpl.generate();
    }

    private String processShowEntry() throws SQLException {
        if (getStrutsEntryId() == null)
            return "Entry id is NULL";
        int id = Integer.parseInt(getStrutsEntryId());
        Entry e = DB.getInstance().getEntry(id);
        Template tpl = new Template("/html/showEntry.html");
        tpl
                .setParameter("##id##", e.getId())
                .setParameter("##name##", e.getName())
                .setParameter("##prio##", e.getPriority())
                .setParameter("##desc##", e.getDescription().replaceAll("\n","<br>"))
                .setParameter("##project##", e.getProject().getName());
        String read = tpl.generate();
        return read;
    }

    private String processNewEntry() throws SQLException {
        System.out.println("Processing new entry");
        System.out.println("Entry name: "+getStrutsEntryName());
        User user = (User) ActionContext.getContext().getSession().get("user");
        if (getStrutsEntryName() != null && getStrutsProjectId() != null && user != null){
            DB.getInstance().createNewEntry(getStrutsEntryName(), Integer.parseInt(getStrutsProjectId()), user.getUserId());
        }else{
            System.err.println("Error: entry name, session user or project id is null");
        }
        return "";
    }

    private String processRefreshEntries() throws SQLException, UnsupportedEncodingException {
        ArrayList<Entry> entries = DB.getInstance().getEntries();
        TreeMap<String, ArrayList<Entry>> map = Utils.filterByProject(entries);
        StringBuilder tmp = new StringBuilder();

        Template tplPrj = new Template("/html/refreshDashboardProjects.html");
        Template tplEntry = new Template("/html/refreshDashboardEntries.html");
        map.forEach((prj,list)->{
            tplPrj.setParameter("##project##", prj);
            StringBuilder tmp2 = new StringBuilder();
            list.forEach(e -> {
                tplEntry.setParameter("##id##", e.getId())
                        .setParameter("##status##", e.getStatus().getName())
                        .setParameter("##name##", e.getName())
                        .setParameter("##color##", e.getStatus().getColor())
                        .setParameter("##prio##", e.getPriority())
                        .setParameter("##desc##", Utils.shrinkText(e.getDescription(), 100));

                String s = tplEntry.generate();
                //System.out.println(s);
                tmp2.append(s);
            });
            tplPrj.setParameter("##entries##", tmp2.toString());
            tmp.append(tplPrj.generate());
        });

        return tmp.toString();
    }

    public String getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(String strutsAction) {
        this.strutsAction = strutsAction;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getStrutsEntryName() {
        return strutsEntryName;
    }

    public void setStrutsEntryName(String strutsEntryName) {
        this.strutsEntryName = strutsEntryName;
    }

    public String getStrutsProjectId() {
        return strutsProjectId;
    }

    public void setStrutsProjectId(String strutsProjectId) {
        this.strutsProjectId = strutsProjectId;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getStrutsEntryId() {
        return strutsEntryId;
    }

    public void setStrutsEntryId(String strutsEntryId) {
        this.strutsEntryId = strutsEntryId;
    }

    public String getStrutsStatusId() {
        return strutsStatusId;
    }

    public void setStrutsStatusId(String strutsStatusId) {
        this.strutsStatusId = strutsStatusId;
    }

    public String getStrutsEntryDesc() {
        return strutsEntryDesc;
    }

    public void setStrutsEntryDesc(String strutsEntryDesc) {
        this.strutsEntryDesc = strutsEntryDesc;
    }

    public String getStrutsPriority() {
        return strutsPriority;
    }

    public void setStrutsPriority(String strutsPriority) {
        this.strutsPriority = strutsPriority;
    }
}
