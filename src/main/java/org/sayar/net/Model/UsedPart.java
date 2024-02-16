package org.sayar.net.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsedPart {
    private String partId;
    private Long usedNumber;
}
