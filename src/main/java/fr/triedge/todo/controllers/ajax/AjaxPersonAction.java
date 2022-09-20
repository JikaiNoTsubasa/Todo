package fr.triedge.todo.controllers.ajax;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Person;
import fr.triedge.todo.tpl.Template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AjaxPersonAction {

    private String strutsAction;
    private String strutsPersonUsername;
    private String strutsPersonId;
    private String strutsPersonFirstname;
    private String strutsPersonLastname;
    private String strutsPersonLastVisited;
    private String strutsPersonLocation;
    private String strutsPersonInfo;
    private String strutsPersonLang;
    private String strutsPersonIsAdmin;
    private String strutsPersonCanEditMol;

    private InputStream inputStream;

    public String execute() throws UnsupportedEncodingException {
        String result = "";

        if (getStrutsAction() != null){
            if (getStrutsAction().equalsIgnoreCase("newPersonForm")){
                result = processNewPersonForm();
            } else if (getStrutsAction().equalsIgnoreCase("createPerson")){
                result = processCreatePerson();
            } else if (getStrutsAction().equalsIgnoreCase("editPersonForm")){
                result = processEditPersonForm();
            } else if (getStrutsAction().equalsIgnoreCase("updatePerson")){
                result = processUpdatePerson();
            }
        }

        inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        return "success";
    }

    private String processUpdatePerson() {
        if (getStrutsPersonId() == null)
            return "Id is null";
        int id = Integer.parseInt(getStrutsPersonId());
        Person p = new Person();
        p.setUsername(getStrutsPersonUsername());
        p.setFirstname(getStrutsPersonFirstname());
        p.setLastname(getStrutsPersonLastname());
        p.setLang(getStrutsPersonLang());
        p.setInfo(getStrutsPersonInfo());
        p.setId(id);
        p.setAdmin(Boolean.valueOf(getStrutsPersonIsAdmin()));
        p.setCanEditMol(Boolean.valueOf(getStrutsPersonCanEditMol()));
        p.setLocation(getStrutsPersonLocation());

        try {
            Date dd = getStrutsLastVisitedAsDate();
            if (dd != null)
                p.setLastVisited(dd);
        } catch (ParseException e) {
            System.err.println("Failed to parse date"+e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            DB.getInstance().updatePerson(p, id);
        } catch (SQLException e) {
            System.err.println("Failed to update person id "+id+": "+e.getMessage());
            throw new RuntimeException(e);
        }
        return "";
    }

    private Date getStrutsLastVisitedAsDate() throws ParseException {
        if (getStrutsPersonLastVisited() == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(getStrutsPersonLastVisited());
    }

    private String processEditPersonForm(){
        if (getStrutsPersonId() == null)
            return "Id is null";
        Template tpl = new Template("/html/editPersonForm.html");
        int id = Integer.parseInt(getStrutsPersonId());
        try {
            Person p = DB.getInstance().getPerson(id);
            tpl
                    .setParameter("##id##", p.getId())
                    .setParameter("##username##", p.getUsername())
                    .setParameter("##firstname##", p.getFirstname())
                    .setParameter("##lastname##", p.getLastname())
                    .setParameter("##location##", p.getLocation())
                    .setParameter("##info##", p.getInfo());
            tpl.setParameter("##isadmin##", p.isAdmin()?"checked":"");
            tpl.setParameter("##caneditmol##", p.isCanEditMol()?"checked":"");
            if (p.getLang() != null){
                switch(p.getLang()){
                    case "EN": tpl.setParameter("##lang##","<option value=\"EN\" selected>EN</option><option value=\"FR\">FR</option><option value=\"DE\">DE</option>");break;
                    case "FR": tpl.setParameter("##lang##","<option value=\"EN\">EN</option><option value=\"FR\" selected>FR</option><option value=\"DE\">DE</option>");break;
                    case "DE": tpl.setParameter("##lang##","<option value=\"EN\">EN</option><option value=\"FR\">FR</option><option value=\"DE\" selected>DE</option>");break;
                }
            }
            if (p.getLastVisited() != null){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dd = format.format(p.getLastVisited());
                tpl.setParameter("##lastvisited##", dd);
            }else{
                tpl.setParameter("##lastvisited##", "");
            }
        } catch (SQLException e) {
            System.err.println("Could not get person id "+id);
            throw new RuntimeException(e);
        }

        return tpl.generate();
    }

    private String processCreatePerson(){
        boolean isAdmin = false;
        boolean canEditMol = false;
        if (getStrutsPersonIsAdmin() != null)
            isAdmin = Boolean.valueOf(getStrutsPersonIsAdmin());
        if (getStrutsPersonCanEditMol() != null)
            canEditMol = Boolean.valueOf(getStrutsPersonCanEditMol());

        try {
            DB.getInstance().createNewPerson(getStrutsPersonUsername(),
                    getStrutsPersonFirstname(),
                    getStrutsPersonLastname(),
                    getStrutsPersonLocation(),
                    isAdmin,
                    canEditMol,
                    getStrutsPersonInfo(),
                    getStrutsPersonLang());
        } catch (SQLException e) {
            System.err.println("Failed to create person: "+e.getMessage());
            throw new RuntimeException(e);
        }

        return "";
    }

    private String processNewPersonForm(){
        Template tpl = new Template("/html/newPersonForm.html");
        return tpl.generate();
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

    public String getStrutsPersonUsername() {
        return strutsPersonUsername;
    }

    public void setStrutsPersonUsername(String strutsPersonUsername) {
        this.strutsPersonUsername = strutsPersonUsername;
    }

    public String getStrutsPersonFirstname() {
        return strutsPersonFirstname;
    }

    public void setStrutsPersonFirstname(String strutsPersonFirstname) {
        this.strutsPersonFirstname = strutsPersonFirstname;
    }

    public String getStrutsPersonLastname() {
        return strutsPersonLastname;
    }

    public void setStrutsPersonLastname(String strutsPersonLastname) {
        this.strutsPersonLastname = strutsPersonLastname;
    }

    public String getStrutsPersonLocation() {
        return strutsPersonLocation;
    }

    public void setStrutsPersonLocation(String strutsPersonLocation) {
        this.strutsPersonLocation = strutsPersonLocation;
    }

    public String getStrutsPersonIsAdmin() {
        return strutsPersonIsAdmin;
    }

    public void setStrutsPersonIsAdmin(String strutsPersonIsAdmin) {
        this.strutsPersonIsAdmin = strutsPersonIsAdmin;
    }

    public String getStrutsPersonCanEditMol() {
        return strutsPersonCanEditMol;
    }

    public void setStrutsPersonCanEditMol(String strutsPersonCanEditMol) {
        this.strutsPersonCanEditMol = strutsPersonCanEditMol;
    }

    public String getStrutsPersonInfo() {
        return strutsPersonInfo;
    }

    public void setStrutsPersonInfo(String strutsPersonInfo) {
        this.strutsPersonInfo = strutsPersonInfo;
    }

    public String getStrutsPersonLang() {
        return strutsPersonLang;
    }

    public void setStrutsPersonLang(String strutsPersonLang) {
        this.strutsPersonLang = strutsPersonLang;
    }

    public String getStrutsPersonId() {
        return strutsPersonId;
    }

    public void setStrutsPersonId(String strutsPersonId) {
        this.strutsPersonId = strutsPersonId;
    }

    public String getStrutsPersonLastVisited() {
        return strutsPersonLastVisited;
    }

    public void setStrutsPersonLastVisited(String strutsPersonLastVisited) {
        this.strutsPersonLastVisited = strutsPersonLastVisited;
    }
}
