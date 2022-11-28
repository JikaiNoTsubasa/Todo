package fr.triedge.todo.controllers.ajax;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public abstract class AjaxAbstractAction {

    private String strutsAction;
    private InputStream inputStream;

    public String execute(){
        String result = executeAction(strutsAction);
        try {
            inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    protected abstract String executeAction(String strutsAction);

    public String getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(String strutsAction) {
        this.strutsAction = strutsAction;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
