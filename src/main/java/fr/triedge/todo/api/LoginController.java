package fr.triedge.todo.api;

import com.idorsia.research.sbilib.utils.SPassword;
import fr.triedge.todo.database.DB;
import fr.triedge.todo.model.User;
import fr.triedge.todo.utils.Vars;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                    createLoginCookie("TodoUser", username);
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

    @GetMapping(Vars.DISCONNECT)
    public ModelAndView disconnect(){
        getSession().setAttribute("user", null);
        deleteLoginCookie("TodoUser");
        return new ModelAndView("redirect:/login");
    }

    public HttpSession getSession(){
        return getHttpReq().getSession(true); // true == allow create
    }

    public HttpServletRequest getHttpReq(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    public HttpServletResponse getHttpRep(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getResponse();
    }

    public void createLoginCookie(String name, String value){
        SPassword pwd = new SPassword(value);
        Cookie cookie = new Cookie(name, pwd.getEncrypted());
        cookie.setMaxAge(86400);
        getHttpRep().addCookie(cookie);
    }

    public void deleteLoginCookie(String name){
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        getHttpRep().addCookie(cookie);
    }
}
