package org.sayar.net.Model.Sms.Fast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastSms {
    @JsonProperty("ParameterArray")
    private List<Map<String,Object>> parameterArray;
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("TemplateId")
    private String templateId;

    public FastSms(String templateId,String phoneNumber,String templateCode,String code) {

        List<Map<String,Object>> paramrArray = new ArrayList<>();
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("Parameter",templateCode);
        parameter.put("ParameterValue",code);
        paramrArray.add(parameter);

        this.parameterArray = paramrArray;
        this.templateId = templateId;
        this.mobile = phoneNumber;

    }
}
