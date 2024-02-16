package org.sayar.net.Model.Sms;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"ID",
"MobileNo",
"SendDateTime",
"DeliveryStatus",
"SMSMessageBody",
"SendIsErronous",
"DeliveryStatusFetchError",
"NeedsReCheck",
"DeliveryStateID"
})
public class Messages {

@JsonProperty("ID")
private Integer iD;
@JsonProperty("MobileNo")
private String mobileNo;
@JsonProperty("SendDateTime")
private String sendDateTime;
@JsonProperty("DeliveryStatus")
private String deliveryStatus;
@JsonProperty("SMSMessageBody")
private String sMSMessageBody;
@JsonProperty("SendIsErronous")
private Boolean sendIsErronous;
@JsonProperty("DeliveryStatusFetchError")
private String deliveryStatusFetchError;
@JsonProperty("NeedsReCheck")
private Boolean needsReCheck;
@JsonProperty("DeliveryStateID")
private Integer deliveryStateID;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("ID")
public Integer getID() {
return iD;
}

@JsonProperty("ID")
public void setID(Integer iD) {
this.iD = iD;
}

@JsonProperty("MobileNo")
public String getMobileNo() {
return mobileNo;
}

@JsonProperty("MobileNo")
public void setMobileNo(String mobileNo) {
this.mobileNo = mobileNo;
}

@JsonProperty("SendDateTime")
public String getSendDateTime() {
return sendDateTime;
}

@JsonProperty("SendDateTime")
public void setSendDateTime(String sendDateTime) {
this.sendDateTime = sendDateTime;
}

@JsonProperty("DeliveryStatus")
public String getDeliveryStatus() {
return deliveryStatus;
}

@JsonProperty("DeliveryStatus")
public void setDeliveryStatus(String deliveryStatus) {
this.deliveryStatus = deliveryStatus;
}

@JsonProperty("SMSMessageBody")
public String getSMSMessageBody() {
return sMSMessageBody;
}

@JsonProperty("SMSMessageBody")
public void setSMSMessageBody(String sMSMessageBody) {
this.sMSMessageBody = sMSMessageBody;
}

@JsonProperty("SendIsErronous")
public Boolean getSendIsErronous() {
return sendIsErronous;
}

@JsonProperty("SendIsErronous")
public void setSendIsErronous(Boolean sendIsErronous) {
this.sendIsErronous = sendIsErronous;
}

@JsonProperty("DeliveryStatusFetchError")
public String getDeliveryStatusFetchError() {
return deliveryStatusFetchError;
}

@JsonProperty("DeliveryStatusFetchError")
public void setDeliveryStatusFetchError(String deliveryStatusFetchError) {
this.deliveryStatusFetchError = deliveryStatusFetchError;
}

@JsonProperty("NeedsReCheck")
public Boolean getNeedsReCheck() {
return needsReCheck;
}

@JsonProperty("NeedsReCheck")
public void setNeedsReCheck(Boolean needsReCheck) {
this.needsReCheck = needsReCheck;
}

@JsonProperty("DeliveryStateID")
public Integer getDeliveryStateID() {
return deliveryStateID;
}

@JsonProperty("DeliveryStateID")
public void setDeliveryStateID(Integer deliveryStateID) {
this.deliveryStateID = deliveryStateID;
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