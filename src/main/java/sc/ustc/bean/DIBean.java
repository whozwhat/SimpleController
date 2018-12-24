package sc.ustc.bean;

import java.util.List;

public class DIBean {
    private String id;
    private  String className;

    public DIBean(String id, String className, List<List<String>> diList) {
        this.id = id;
        this.className = className;
        this.diList = diList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<List<String>> getDiList() {
        return diList;
    }

    public void setDiList(List<List<String>> diList) {
        this.diList = diList;
    }

    private List<List<String>> diList;
}
