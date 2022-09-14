package fr.triedge.todo.controllers.ajax;

import com.opensymphony.xwork2.ActionContext;
import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.model.User;
import fr.triedge.todo.tpl.Template;
import fr.triedge.todo.utils.Utils;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AjaxEntryAction {

    private String strutsAction;
    private String strutsEntryName;
    private String strutsEntryId;
    private String strutsStatusId;
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
            }
        }
        inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        return "success";
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
                .setParameter("##desc##", e.getDescription().replaceAll("\n","<br>"));
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
        HashMap<String, ArrayList<Entry>> map = Utils.filterByProject(entries);
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
}
