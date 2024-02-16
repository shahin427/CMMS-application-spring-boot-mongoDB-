package org.sayar.net.Model.Sms;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "TokenKey",
        "IsSuccessful",
        "Message"
})
public class TokenResponse {

    @JsonProperty("TokenKey")
    private String tokenKey;
    @JsonProperty("IsSuccessful")
    private Boolean isSuccessful;
    @JsonProperty("Message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TokenKey")
    public String getTokenKey() {
        return tokenKey;
    }

    @JsonProperty("TokenKey")
    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
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