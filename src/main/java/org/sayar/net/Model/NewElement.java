package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.NewElementType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewElement {
    private String id;
    private String questionTitle;
    private String guide;
    private boolean required;
    private String placeHolder;
    private long maxLength;
    private long minLength;
    private NewElementType newElementType;
    private int maxItemSelectable;
    private int minItemSelectable;
    List<NewOption> newOptionList;
}
