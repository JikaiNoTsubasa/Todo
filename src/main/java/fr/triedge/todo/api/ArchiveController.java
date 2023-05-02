package fr.triedge.todo.api;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.utils.Vars;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@Controller
public class ArchiveController {

    @GetMapping(Vars.VIEW_ARCHIVE)
    public ModelAndView archive(){
        ModelAndView model = new ModelAndView("archive.html");
        try {
            model.addObject("entries",DB.getInstance().getArchivedEntries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
}
