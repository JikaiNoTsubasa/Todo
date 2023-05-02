package fr.triedge.todo.api;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Person;
import fr.triedge.todo.tpl.Template;
import fr.triedge.todo.utils.Vars;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class PersonController {

    @GetMapping(Vars.VIEW_PERSONS)
    public ModelAndView persons(){
        ModelAndView model = new ModelAndView("person.html");
        try {
            model.addObject("persons", DB.getInstance().getPersonsByLastVisited());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }

    @GetMapping(Vars.AJAX_PERSON_NEW_PERSON)
    public ResponseEntity<?> newPerson(){
        Template tpl = new Template("/html/newPersonForm.html");
        return ResponseEntity.ok(tpl.generate());
    }

    @GetMapping(Vars.AJAX_PERSON_EDIT_PERSON)
    public ResponseEntity<?> editPerson(@RequestParam(value = "personId", required = true) int id){
        Template tpl = new Template("/html/editPersonForm.html");
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
                    default: tpl.setParameter("##lang##","<option value=\"EN\" selected>EN</option><option value=\"FR\">FR</option><option value=\"DE\">DE</option>");break;
                    case "FR": tpl.setParameter("##lang##","<option value=\"EN\">EN</option><option value=\"FR\" selected>FR</option><option value=\"DE\">DE</option>");break;
                    case "DE": tpl.setParameter("##lang##","<option value=\"EN\">EN</option><option value=\"FR\">FR</option><option value=\"DE\" selected>DE</option>");break;
                }
            }else{
                tpl.setParameter("##lang##","<option value=\"EN\" selected>EN</option><option value=\"FR\">FR</option><option value=\"DE\">DE</option>");
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

        return ResponseEntity.ok(tpl.generate());
    }

    @PostMapping(Vars.AJAX_PERSON_CREATE_PERSON)
    public ResponseEntity<?> createPerson(
            @RequestParam(value = "personIsAdmin", required = true) boolean isAdmin,
            @RequestParam(value = "personCanEditMol", required = true) boolean canEditMol,
            @RequestParam(value = "personUsername", required = true) String username,
            @RequestParam(value = "personFirstname", required = true) String firstname,
            @RequestParam(value = "personLastname", required = true) String lastname,
            @RequestParam(value = "personLocation", required = true) String location,
            @RequestParam(value = "personInfo", required = true) String info,
            @RequestParam(value = "personLang", required = true) String lang
    ){
        try {
            DB.getInstance().createNewPerson(username,
                    firstname,
                    lastname,
                    location,
                    isAdmin,
                    canEditMol,
                    info,
                    lang);
        } catch (SQLException e) {
            System.err.println("Failed to create person: "+e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping(Vars.AJAX_PERSON_UPDATE_PERSON)
    public ResponseEntity<?> createPerson(
            @RequestParam(value = "personId", required = true) int id,
            @RequestParam(value = "personIsAdmin", required = true) boolean isAdmin,
            @RequestParam(value = "personCanEditMol", required = true) boolean canEditMol,
            @RequestParam(value = "personUsername", required = true) String username,
            @RequestParam(value = "personFirstname", required = true) String firstname,
            @RequestParam(value = "personLastname", required = true) String lastname,
            @RequestParam(value = "personLocation", required = true) String location,
            @RequestParam(value = "personLastVisited", required = true) String lastVisited,
            @RequestParam(value = "personInfo", required = true) String info,
            @RequestParam(value = "personLang", required = true) String lang
    ){
        Person p = new Person();
        p.setUsername(username);
        p.setFirstname(firstname);
        p.setLastname(lastname);
        p.setLang(lang);
        p.setInfo(info);
        p.setId(id);
        p.setAdmin(isAdmin);
        p.setCanEditMol(canEditMol);
        p.setLocation(location);
        if (lastVisited != null && !lastVisited.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                p.setLastVisited(sdf.parse(lastVisited));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            DB.getInstance().updatePerson(p, id);
        } catch (SQLException e) {
            System.err.println("Failed to update person id "+id+": "+e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping(Vars.AJAX_PERSON_DELETE_PERSON)
    public ResponseEntity<?> createPerson(@RequestParam(value = "personId", required = true) int id){
        try {
            DB.getInstance().deletePerson(id);
        } catch (SQLException e) {
            System.err.println("Failed to delete person "+id+": "+e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("OK");
    }
}
