package org.sayar.net.Model.Sms;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"Messages",
"IsSuccessful",
"Message"
})
public class CheckSmsSendedResponse {

@JsonProperty("Messages")
private Messages messages;
@JsonProperty("IsSuccessful")
private Boolean isSuccessful;
@JsonProperty("Message")
private String message;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("Messages")
public Messages getMessages() {
return messages;
}

@JsonProperty("Messages")
public void setMessages(Messages messages) {
this.messages = messages;
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