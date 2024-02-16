package org.sayar.net.Model.Mongo.poll.DTO;

import org.sayar.net.Model.Mongo.poll.enums.FormStatus;
import org.sayar.net.Model.Mongo.poll.model.elements.Element;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormDto {
    private String id;
    private String title;
    private FormCategory formCategory;
    private String description;
    private FormStatus formStatus;
    private String creatorId;
    private List<Element> elementList = new ArrayList<>();
    private boolean canBeTemplate = true;
    private Integer totalResponder;
    private boolean isTemplate = false;
    private int formScore;
    private Date systemCreationDate;


    public FormDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FormCategory getFormCategory() {
        return formCategory;
    }

    public void setFormCategory(FormCategory formCategory) {
        this.formCategory = formCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FormStatus getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(FormStatus formStatus) {
        this.formStatus = formStatus;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<Element> getElementList() {
        return elementList;
    }

    public void setElementList(List<Element> elementList) {
        this.elementList = elementList;
    }

    public boolean isCanBeTemplate() {
        return canBeTemplate;
    }

    public void setCanBeTemplate(boolean canBeTemplate) {
        this.canBeTemplate = canBeTemplate;
    }

    public Integer getTotalResponder() {
        return totalResponder;
    }

    public void setTotalResponder(Integer totalResponder) {
        this.totalResponder = totalResponder;
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    public int getFormScore() {
        return formScore;
    }

    public void setFormScore(int formScore) {
        this.formScore = formScore;
    }

    public Date getSystemCreationDate() {
        return systemCreationDate;
    }

    public void setSystemCreationDate(Date systemCreationDate) {
        this.systemCreationDate = systemCreationDate;
    }
}
