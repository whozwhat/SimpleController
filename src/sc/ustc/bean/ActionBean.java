package sc.ustc.bean;

import java.util.List;
import java.util.Map;

public class ActionBean {

    public String getActionClass() {
        return actionClass;
    }

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public Map<String, List<String>> getActionResults() {
        return actionResults;
    }

    public void setActionResults(Map<String, List<String>> actionResults) {
        this.actionResults = actionResults;
    }

    public Map<String, List<String>> getActionInterceptors() {
        return actionInterceptors;
    }

    public void setActionInterceptors(Map<String, List<String>> actionInterceptors) {
        this.actionInterceptors = actionInterceptors;
    }

    private String actionName;
    private String actionClass;
    private String actionMethod;
    private Map<String, List<String>> actionResults;
    private Map<String, List<String>> actionInterceptors;

    public ActionBean(String actionName, String actionClass, String actionMethod, Map<String, List<String>> actionResults,
                      Map<String, List<String>> actionInterceptors) {
        this.actionName = actionName;
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
        this.actionResults = actionResults;
        this.actionInterceptors = actionInterceptors;
    }



}