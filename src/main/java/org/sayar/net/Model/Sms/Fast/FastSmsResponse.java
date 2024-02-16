package org.sayar.net.Model.Sms.Fast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "VerificationCodeId",
        "Message",
        "IsSuccessful"

})
public class FastSmsResponse {

    @JsonProperty("VerificationCodeId")
    private String verificationCodeId ;
    @JsonProperty("Message")
    private String Message;
    @JsonProperty("IsSuccessful")
    private boolean isSuccessful;

    public FastSmsResponse() {
    }

    @JsonProperty("VerificationCodeId")
    public String getVerificationCodeId() {
        return verificationCodeId;
    }
    @JsonProperty("VerificationCodeId")
    public void setVerificationCodeId(String verificationCodeId) {
        this.verificationCodeId = verificationCodeId;
    }
    @JsonProperty("Message")
    public String getMessage() {
        return Message;
    }
    @JsonProperty("Message")
    public void setMessage(String message) {
        Message = message;
    }
    @JsonProperty("IsSuccessful")
    public boolean isSuccessful() {
        return isSuccessful;
    }
    @JsonProperty("IsSuccessful")
    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}

