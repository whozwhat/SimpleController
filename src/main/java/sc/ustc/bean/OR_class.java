package sc.ustc.bean;

import java.util.List;

public class OR_class {

    private String name;
    private String table;
    private List<List<String>> propertyList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<List<String>> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<List<String>> propertyList) {
        this.propertyList = propertyList;
    }

}