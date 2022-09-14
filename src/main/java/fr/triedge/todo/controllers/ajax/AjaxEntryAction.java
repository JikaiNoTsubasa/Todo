package fr.triedge.todo.controllers.ajax;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AjaxEntryAction {

    private String strutsAction;
    private InputStream inputStream;
    public InputStream getInputStream() {
        return inputStream;
    }

    public String execute() throws Exception {
        if (getStrutsAction() != null){
            if (getStrutsAction().equalsIgnoreCase("refreshEntries")){
                ArrayList<Entry> entries = DB.getInstance().getEntries();
                HashMap<String, ArrayList<Entry>> map = Utils.filterByProject(entries);
                StringBuilder tmp = new StringBuilder();

                map.forEach((prj,list)->{
                    tmp.append("<div class=\"sb-card sb-margin-top\"><div class=\"sb-card-header\">");
                    tmp.append(prj);
                    tmp.append("</div><div class=\"sb-card-body\">");
                    list.forEach(e -> {
                        tmp.append("<div class=\"sb-entry\"><div class=\"sb-entry-title\">");
                        tmp.append("#").append(e.getId()).append("[<span style=\"color:").append(e.getStatus().getColor()).append("\">").append(e.getStatus().getName()).append("</span>] <a class=\"sb-link\" href=\"#\" onclick=\"openModalShowEntry(").append(e.getId()).append(");\">").append(e.getName()).append("</a><br/>");
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

                inputStream = new ByteArrayInputStream(tmp.toString().getBytes("UTF-8"));
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

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
