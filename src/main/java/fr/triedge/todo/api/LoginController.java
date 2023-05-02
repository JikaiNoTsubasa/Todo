package fr.triedge.todo.api;

import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.User;
import fr.triedge.todo.utils.Vars;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class LoginController {

    @RequestMapping(value = Vars.VIEW_LOGIN, method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView login(@RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "password", required = false) String password){
        if (getSession().getAttribute("user") != null)
            return new ModelAndView("redirect:/home"); // redirects if user logged on

        ModelAndView model = new ModelAndView("login.html");

        if (username != null && password != null && !username.equals("") && !password.equals("")){
            try {
                User user = DB.getInstance().loginUser(username, password, true);
                if (user != null){
                    getSession().setAttribute("user", user);
                    return new ModelAndView("redirect:/home");
                }else{
                    model.addObject("error","Username or password is incorrect.");
                }
            } catch (SQLException e) {
                model.addObject("error","Username or password is incorrect, SQL exception.");
            }
        }

        return model;
    }

    public HttpSession getSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }
}
