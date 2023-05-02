package fr.triedge.todo.api;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.model.Project;
import fr.triedge.todo.model.User;
import fr.triedge.todo.tpl.Template;
import fr.triedge.todo.utils.Utils;
import fr.triedge.todo.utils.Vars;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

@Controller
public class HomeController {

    @GetMapping(Vars.VIEW_HOME)
    public ModelAndView home(){
        ModelAndView model = new ModelAndView("home.html");
        try {
            ArrayList<Project> projects = DB.getInstance().getProjects();
            model.addObject("projects", projects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }

    @GetMapping(Vars.AJAX_HOME_REFRESH)
    public ResponseEntity<?> ajaxRefresh(){
        try {
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

            return ResponseEntity.ok(tmp.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(Vars.AJAX_HOME_GET_ENTRY)
    public ResponseEntity<?> getEntry(@RequestParam(value = "entryId", required = true) int id){
        try {
            Entry e = DB.getInstance().getEntry(id);
            Template tpl = new Template("/html/showEntry.html");
            tpl
                    .setParameter("##id##", e.getId())
                    .setParameter("##name##", e.getName())
                    .setParameter("##prio##", e.getPriority())
                    .setParameter("##desc##", e.getDescription().replaceAll("\n","<br>"))
                    .setParameter("##project##", e.getProject().getName());
            return ResponseEntity.ok(tpl.generate());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @GetMapping(Vars.AJAX_HOME_EDIT_ENTRY)
    public ResponseEntity<?> editEntry(@RequestParam(value = "entryId", required = true) int id){
        try{
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
            return ResponseEntity.ok(tpl.generate());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping(Vars.AJAX_HOME_CHANGE_STATUS)
    public ResponseEntity<?> changeStatus(@RequestParam(value = "entryId", required = true) int id, @RequestParam(value = "statusId", required = true) int statusId){
        try {
            DB.getInstance().changeStatus(id, statusId);
            return ResponseEntity.ok("OK");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(Vars.AJAX_HOME_UPDATE_ENTRY)
    public ResponseEntity<?> updateEntry(
            @RequestParam(value = "entryId", required = true) int id,
            @RequestParam(value = "entryName", required = true) String name,
            @RequestParam(value = "entryDesc", required = true) String desc,
            @RequestParam(value = "priority", required = true) int prio,
            @RequestParam(value = "projectId", required = true) int projectId){

        try{
            Project p = new Project();
            p.setId(projectId);
            Entry e = new Entry();
            e.setId(id);
            e.setName(name);
            e.setDescription(desc);
            e.setPriority(prio);
            e.setProject(p);
            DB.getInstance().updateEntry(e, id);
            return ResponseEntity.ok("OK");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping(Vars.AJAX_HOME_NEW_ENTRY)
    public ResponseEntity<?> newEntry(
            @RequestParam(value = "entryName", required = true) String name,
            @RequestParam(value = "projectId", required = true) int projectId
    ){

        try{
            HttpSession session = getSession();
            User user = (User) session.getAttribute("user");
            if (name != null && projectId>0){
                int uid = 1;
                if (user != null){
                    uid = user.getUserId();
                }
                DB.getInstance().createNewEntry(name, projectId, uid);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("OK");
    }

    @GetMapping(Vars.AJAX_HOME_CONFIRM_ENTRY_DELETE)
    public ResponseEntity<?> confirmDelete(@RequestParam(value = "entryId", required = true) int id){
        try{
            Entry entry = DB.getInstance().getEntry(id);
            Template tpl = new Template("/html/askConfirmDeleteEntry.html");
            tpl
                    .setParameter("##id##", entry.getId())
                    .setParameter("##name##", entry.getName());
            return ResponseEntity.ok(tpl.generate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(Vars.AJAX_HOME_DELETE_ENTRY)
    public ResponseEntity<?> deleteEntry(@RequestParam(value = "entryId", required = true) int id){
        try{
            DB.getInstance().deleteEntry(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("OK");
    }

    public HttpSession getSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }
}
