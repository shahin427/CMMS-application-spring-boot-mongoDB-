package org.sayar.net.Model.Mongo.poll.model.elements;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Enumes.ImageStatus;
import org.sayar.net.Model.Mongo.poll.controller.form.ImageForm;
import org.sayar.net.Model.Mongo.poll.enums.ComboBoxType;
import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Tools.PatternList;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sina on 1/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class Element extends BasicElement implements Serializable {

    //global element
//    private Integer minItemSelectable = 1;
//    private Integer maxItemSelectable = 1;
    private List<String> comboOptionList;
    private Boolean multipleSelect;
    private ComboBoxType comboBoxType;
    @Pattern(regexp = PatternList.dateInModel)
    private Date startDate;
    @Pattern(regexp = PatternList.dateInModel)
    private Date endDate;
    private List<MatrixQuestion> matrixQuestionList;
    //    private ImageForm picture;
    private int step = 10;
    private String beginLabel;
    private String endLabel;
    private String starLabel;
    private int starCount;
    private String ColorStatus;
    private String Direction;
    private float InitialRate;
    private TimeModel startTime;
    private TimeModel endTime;
    private List<Object> subQuestionList = new ArrayList<>();
    //    private String middleLabel;
//    private List<String> whoAnswer = new ArrayList<>();
    private List<Image> optionList = new ArrayList<>();

}