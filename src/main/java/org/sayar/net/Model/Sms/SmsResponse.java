package org.sayar.net.Model.Sms;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Ids",
        "BatchKey",
        "IsSuccessful",
        "Message"
})
public class SmsResponse {

    @JsonProperty("Ids")
    private List<SmsResponseId> ids = null;
    @JsonProperty("BatchKey")
    private String batchKey;
    @JsonProperty("IsSuccessful")
    private Boolean isSuccessful;
    @JsonProperty("Message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Ids")
    public List<SmsResponseId> getIds() {
        return ids;
    }

    @JsonProperty("Ids")
    public void setIds(List<SmsResponseId> ids) {
        this.ids = ids;
    }

    @JsonProperty("BatchKey")
    public String getBatchKey() {
        return batchKey;
    }

    @JsonProperty("BatchKey")
    public void setBatchKey(String batchKey) {
        this.batchKey = batchKey;
    }

    @JsonProperty("IsSuccessful")
    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    @JsonProperty("IsSuccessful")
    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}