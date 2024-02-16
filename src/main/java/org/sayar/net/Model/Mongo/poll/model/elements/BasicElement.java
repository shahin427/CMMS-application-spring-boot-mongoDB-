package org.sayar.net.Model.Mongo.poll.model.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.Mongo.poll.enums.ElementType;
import org.sayar.net.Model.Mongo.poll.enums.TextFieldType;
import org.sayar.net.Model.newModel.Image;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * @author Meghdad Hajilo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
@NoArgsConstructor
public class BasicElement {
    @Id
    private String id;
    private String label;
    private String helpText;
    private Boolean required;
    private Image picture;
    private ElementType elementType;
    private Map<String, String> parentElement = null;
    private int index;
    private int minLength = 1;
    private int maxLength = 10;
    private String placeHolder;
    private TextFieldType textFieldType;
    private Integer minItemSelectable = 1;
    private Integer maxItemSelectable = 1;
}
