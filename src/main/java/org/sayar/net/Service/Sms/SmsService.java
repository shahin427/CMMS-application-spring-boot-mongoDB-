package org.sayar.net.Service.Sms;

public interface SmsService  {

    public boolean sendSms(String message, String phoneNumber);
    public boolean sendFastSms(String phoneNumber, String code);
    public boolean sendPositionSms(String phoneNumber, int chair, int bus);
    public String getSecurityToken();


}
