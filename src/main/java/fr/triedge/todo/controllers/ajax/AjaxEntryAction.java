package fr.triedge.todo.controllers.ajax;

import com.opensymphony.xwork2.ActionContext;
import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.model.User;
import fr.triedge.todo.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class AjaxEntryAction {

    private String strutsAction;
    private String strutsEntryName;
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
            }
        }
        inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        return "success";
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

        map.forEach((prj,list)->{
            tmp.append("<div class=\"sb-card sb-margin-top\"><div class=\"sb-card-header\">");
            tmp.append(prj);
            tmp.append("</div><div class=\"sb-card-body\">");
            list.forEach(e -> {
                tmp.append("<div class=\"sb-entry\"><div class=\"sb-entry-title\">");
                tmp.append("#").append(e.getId()).append(" [<span style=\"color:").append(e.getStatus().getColor()).append("\">").append(e.getStatus().getName()).append("</span>] <a class=\"sb-link\" href=\"#\" onclick=\"openModalShowEntry(").append(e.getId()).append(");\">").append(e.getName()).append("</a><br/>");
                tmp.append(Utils.shrinkText(e.getDescription(), 100));
                tmp.append("</div><div class=\"sb-entry-priority\">");
                tmp.append(e.getPriority());
                tmp.append("</div><div class=\"sb-entry-action\">");
                tmp.append("<select class=\"sb-dropdown\" onchange=\"changeStatus(").append(e.getId()).append(",this.value);\">");
                tmp.append("<option value=\"-1\">Status</option><option value=\"2\">InProgress</option><option value=\"3\">Done</option></select>");
                tmp.append("<span class=\"sb-button\" onclick=\"openModal(").append(e.getId()).append(");\">Edit</span>");
                tmp.append("<span class=\"sb-button\" onclick=\"openModalDelete(").append(e.getId()).append(");\">Delete</span>");
                tmp.append("</div></div>");
            });
            tmp.append("</div></div>");
        });

        return tmp.toString();
        //inputStream = new ByteArrayInputStream(tmp.toString().getBytes("UTF-8"));
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
}
