package org.sayar.net.Model.ResponseModel;

import org.sayar.net.Model.newModel.Task.model.TaskGroup;

import java.util.List;

public class TaskGroupRes {

    private TaskGroup taskGroup;
    private List<Long> wo;
    private List<Long> sh;

    public TaskGroupRes() {
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public List<Long> getWo() {
        return wo;
    }

    public void setWo(List<Long> wo) {
        this.wo = wo;
    }

    public List<Long> getSh() {
        return sh;
    }

    public void setSh(List<Long> sh) {
        this.sh = sh;
    }
}
