package fr.triedge.todo.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import fr.triedge.todo.model.User;

public class SecurityInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation inv) throws Exception {
        User user = (User)inv.getInvocationContext().getSession().get("user");

        if (user == null && !inv.getInvocationContext().getActionName().equalsIgnoreCase("register")){
            return "login";
        }

        return inv.invoke();
    }
}
