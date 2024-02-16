package org.sayar.net.Model.Mongo.poll.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Mongo.poll.controller.form.ImageForm;
import org.sayar.net.Model.Mongo.poll.enums.FormStatus;
import org.sayar.net.Model.Mongo.poll.model.elements.Element;
import org.sayar.net.Model.newModel.Image;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by sina on 1/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Form implements Serializable {
    @Id
    private String id;
    private String title;
    private String description;
    private FormStatus formStatus;
    private String creatorId;
    private List<Element> elementList = new ArrayList<>();
    private boolean canBeTemplate = true;
    @JsonIgnore
    private List<String> whoAnswers = new ArrayList<>();
    private Integer totalResponder;
    private boolean Template = false;
    private int formScore;
    private Date systemCreationDate;
    private String parkId;
    private String formCategoryId;
    private boolean usedInActivity;
    private String companyId;
}