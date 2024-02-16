
package org.sayar.net.Model.Sms;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Messages",
        "MobileNumbers",
        "LineNumber",
        "SendDateTime",
        "CanContinueInCaseOfError"
})
public class Sms {

    @JsonProperty("Messages")
    private List<String> messages = null;
    @JsonProperty("MobileNumbers")
    private List<String> mobileNumbers = null;
    @JsonProperty("LineNumber")
    private String lineNumber;
    @JsonProperty("SendDateTime")
    private String sendDateTime;
    @JsonProperty("CanContinueInCaseOfError")
    private String canContinueInCaseOfError;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Messages")
    public List<String> getMessages() {
        return messages;
    }

    @JsonProperty("Messages")
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @JsonProperty("MobileNumbers")
    public List<String> getMobileNumbers() {
        return mobileNumbers;
    }

    @JsonProperty("MobileNumbers")
    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }

    @JsonProperty("LineNumber")
    public String getLineNumber() {
        return lineNumber;
    }

    @JsonProperty("LineNumber")
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    @JsonProperty("SendDateTime")
    public String getSendDateTime() {
        return sendDateTime;
    }

    @JsonProperty("SendDateTime")
    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    @JsonProperty("CanContinueInCaseOfError")
    public String getCanContinueInCaseOfError() {
        return canContinueInCaseOfError;
    }

    @JsonProperty("CanContinueInCaseOfError")
    public void setCanContinueInCaseOfError(String canContinueInCaseOfError) {
        this.canContinueInCaseOfError = canContinueInCaseOfError;
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
