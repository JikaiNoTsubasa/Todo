package fr.triedge.todo.api;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.Entry;
import fr.triedge.todo.model.Event;
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
import java.util.ArrayList;
import java.util.Date;

@Controller
public class CalendarController {

    @GetMapping(Vars.VIEW_CALENDAR)
    public ModelAndView calendar(){
        ModelAndView model = new ModelAndView("calendar.html");
        return model;
    }

    @GetMapping(Vars.AJAX_CALENDAR_GET_EVENTS)
    public ResponseEntity<?> ajaxGetEvents(){

        try {
            ArrayList<Event> events = DB.getInstance().getEvents();
            StringBuilder tmp = new StringBuilder();
            int evCount = events.size();
            int evIdx = 1;
            tmp.append("[");
            for (Event e : events){
                tmp.append("{");
                tmp.append("\"id\": \"").append(e.getId()).append("\",");
                tmp.append("\"name\": \"").append(e.getTitle()).append("\",");
                tmp.append("\"type\": \"").append(e.getType()).append("\",");
                if (e.getBadge() != null)
                    tmp.append("\"badge\": \"").append(e.getBadge()).append("\",");
                if (e.getColor() != null)
                    tmp.append("\"color\": \"").append(e.getColor()).append("\",");
                tmp.append("\"everyYear\":\"").append(e.isEveryYear()).append("\",");
                SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
                String fdate = format.format(e.getDate());
                tmp.append("\"date\": \"").append(fdate).append("\"");
                tmp.append("}");
                if (evIdx < evCount)
                    tmp.append(","); // Prevent JSON parsing to fail
                evIdx++;
            }
            tmp.append("]");

            return ResponseEntity.ok(tmp.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping(Vars.AJAX_CALENDAR_CREATE_FORM)
    public ResponseEntity<?> ajaxCreateForm(){
        Template tpl = new Template("html/newEventForm.html");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tpl.setParameter("##DATE##",format.format(new Date()));
        return ResponseEntity.ok(tpl.generate());
    }

    @GetMapping(Vars.AJAX_CALENDAR_EDIT_FORM)
    public ResponseEntity<?> ajaxEditForm(@RequestParam(value = "id")int id){
        Template tpl = new Template("html/editEventForm.html");

        try {
            Event e = DB.getInstance().getEvent(id);

            String type = "";
            if (e.getType().equals("event")){
                type += "<option value=\"event\" selected>Event</option>\n";
            }else{
                type += "<option value=\"event\" selected>Event</option>\n";
            }
            if (e.getType().equals("holiday")){
                type += "<option value=\"holiday\" selected>Holiday</option>";
            }else{
                type += "<option value=\"holiday\">Holiday</option>";
            }
            if (e.getType().equals("birthday")){
                type += "<option value=\"birthday\" selected>Birthday</option>";
            }else{
                type += "<option value=\"birthday\">Birthday</option>";
            }

            tpl.setParameter("##TYPE##", type);
            tpl.setParameter("##TITLE##", e.getTitle());
            tpl.setParameter("##DESC##", e.getDescription());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            tpl.setParameter("##DATE##", format.format(e.getDate()));
            tpl.setParameter("##BADGE##", e.getBadge());
            tpl.setParameter("##COLOR##", e.getColor());
            tpl.setParameter("##YEAR##", e.isEveryYear()?"checked":"");
            tpl.setParameter("##NOTIFY##", e.isNotify()?"checked":"");
            tpl.setParameter("##ID##", e.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(tpl.generate());
    }

    @PostMapping(Vars.AJAX_CALENDAR_CREATE_EVENT)
    public ResponseEntity<?> ajaxCreateUpdateEvent(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "badge", required = false) String badge,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "date", required = true) String date,
            @RequestParam(value = "year", required = false) boolean year,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "notify", required = false) boolean notify
    ){
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(description);
        e.setColor(color);
        e.setBadge(badge);
        e.setType(type);
        e.setEveryYear(year);
        e.setNotify(notify);

        if (date != null && !date.equals("")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                e.setDate(format.parse(date));
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            if (id != null && id > 0){
                DB.getInstance().updateEvent(e, id);
            }else{
                DB.getInstance().insertEvent(e);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return ResponseEntity.ok("");
    }

    @GetMapping(Vars.AJAX_CALENDAR_SHOW_EVENT)
    public ResponseEntity<?> ajaxShowEvent(@RequestParam(value = "id")int id){
        Template tpl = new Template("html/showEvent.html");
        try {
            Event e = DB.getInstance().getEvent(id);
            tpl.setParameter("##COLOR##", e.getColor());
            tpl.setParameter("##TITLE##", e.getTitle());
            tpl.setParameter("##BADGE##", e.getBadge());
            tpl.setParameter("##ID##", id);
            tpl.setParameter("##DESC##", e.getDescription());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(tpl.generate());
    }

    @GetMapping(Vars.AJAX_CALENDAR_DELETE_EVENT)
    public ResponseEntity<?> ajaxDeleteEvent(@RequestParam(value = "id")int id){
        try {
            DB.getInstance().deleteEvent(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("");
    }
}
