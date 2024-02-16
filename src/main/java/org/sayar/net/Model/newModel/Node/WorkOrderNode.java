package org.sayar.net.Model.newModel.Node;



import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.ArrayList;
import java.util.List;

public class WorkOrderNode {

    private long id;
    private String name;
    private int laborHour;
    private Priority priorityTitle;
    private String statusTitle;
    private String chargeDepartmentTitle;
    private String accountTitle;
    private String completionDate;
    private List<WorkOrderNode> children=new ArrayList<>();

    public WorkOrderNode() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLaborHour() {
        return laborHour;
    }

    public void setLaborHour(int laborHour) {
        this.laborHour = laborHour;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getChargeDepartmentTitle() {
        return chargeDepartmentTitle;
    }

    public void setChargeDepartmentTitle(String chargeDepartmentTitle) {
        this.chargeDepartmentTitle = chargeDepartmentTitle;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public List<WorkOrderNode> getChildren() {
        return children;
    }

    public void setChildren(List<WorkOrderNode> children) {
        this.children = children;
    }

    public Priority getPriorityTitle() {
        return priorityTitle;
    }

    public void setPriorityTitle(Priority priorityTitle) {
        this.priorityTitle = priorityTitle;
    }
}
