package org.sayar.net.Model.Sms.Fast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PositionSms {

    @JsonProperty("ParameterArray")
    private List<Map<String, Object>> parameterArray;
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("TemplateId")
    private String templateId;

    public PositionSms(String templateId, String phoneNumber, int bus,int chair) {

        List<Map<String, Object>> paramrArray = new ArrayList<>();
        Map<String, Object> parameterBus = new HashMap<>();
        parameterBus.put("Parameter", "bus");
        parameterBus.put("ParameterValue", bus);
        paramrArray.add(parameterBus);

        Map<String, Object> parameterChair = new HashMap<>();
        parameterChair.put("Parameter", "chair");
        parameterChair.put("ParameterValue", chair);
        paramrArray.add(parameterChair);

        this.parameterArray = paramrArray;
        this.templateId = templateId;
        this.mobile = phoneNumber;

    }
}
