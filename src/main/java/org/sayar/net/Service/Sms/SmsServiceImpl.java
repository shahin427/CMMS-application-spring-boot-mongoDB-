package org.sayar.net.Service.Sms;

import org.sayar.net.Model.Sms.*;
import org.sayar.net.Model.Sms.Fast.FastSms;
import org.sayar.net.Model.Sms.Fast.FastSmsResponse;
import org.sayar.net.Model.Sms.Fast.PositionSms;
import org.sayar.net.NetApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SmsServiceImpl implements SmsService,CommandLineRunner {
    private static final String UserApiKey = "d164659dc39dd22ca766659d";
    private static final String SecretKey = "L1derL@nd#2018Arash";
    private static final String LineNumber = "30004747475144";

    private static final String FastTemplateCode = "VerificationCode";
    private static final String TemplateId = "5124";


//    private static final String UserApiKey = "1ea636648613276e2f9a3aa3";
////    private static final String SecretKey = "S@yarOrg96";
////    private static final String LineNumber = "30004505000648";


//    public static String token;
    @Override
    public boolean sendSms(String message,String phoneNumber) {

        SmsResponse smsResponse = send(message,phoneNumber);

        if (isTokenExpire(smsResponse)){
            getSecurityToken();
            smsResponse = send(message,phoneNumber);
        }


        return checkUserGetSms(smsResponse);
    }

    @Override
    public boolean sendFastSms(String phoneNumber,String code) {

        FastSmsResponse smsResponse = sendFast(phoneNumber,code);
        return smsResponse.isSuccessful();
    }

    @Override
    public boolean sendPositionSms(String phoneNumber, int chair, int bus) {

        String url = "http://RestfulSms.com/api/UltraFastSend";

        PositionSms fastSms = new PositionSms(TemplateId,phoneNumber,bus,chair);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sms-ir-secure-token", NetApplication.token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<PositionSms> entity = new HttpEntity<>(fastSms,headers);


        ResponseEntity<FastSmsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                FastSmsResponse.class);

        return response.getBody().isSuccessful();

    }


    @Override
    public String getSecurityToken() {
        String url = "http://RestfulSms.com/api/Token";
        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> body = new HashMap<>();
        body.put("UserApiKey",UserApiKey);
        body.put("SecretKey",SecretKey);



        try {

            ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(url,body,TokenResponse.class);
            TokenResponse response=responseEntity.getBody();

            if (response.getIsSuccessful()){
                return response.getTokenKey();
            }else {
                System.out.println("Can't get sms token !! response success is : "+response.getIsSuccessful());
                throw new RuntimeException("Can't get sms token !!");
            }

        }catch (Exception e){

            System.out.println("Can't get sms token !!");
            throw new RuntimeException("Can't get sms token !!");

        }


    }

    public boolean isTokenExpire(SmsResponse response){

        if (!response.getIsSuccessful() && response.getMessage().equals("Token منقضی شده است . Token جدیدی درخواست کنید")){
            return true;
        }else {
            return false;
        }
    }

    public SmsResponse send(String message,String phoneNumber){

        String url = "http://RestfulSms.com/api/MessageSend";

        List<String> temp = new ArrayList<>();
        temp.add(message);
        Sms sms = new Sms();
        sms.setMessages(temp);
        temp = new ArrayList<>();
        temp.add(phoneNumber);
        sms.setMobileNumbers(temp);
        sms.setLineNumber(LineNumber);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sms-ir-secure-token", NetApplication.token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Sms> entity = new HttpEntity<>(sms,headers);


        ResponseEntity<SmsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                SmsResponse.class);

        return response.getBody();
    }


    private FastSmsResponse sendFast(String phone,String code){
        String url = "http://RestfulSms.com/api/UltraFastSend";

        FastSms fastSms = new FastSms(TemplateId,phone,FastTemplateCode,code);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sms-ir-secure-token", NetApplication.token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<FastSms> entity = new HttpEntity<>(fastSms,headers);


        ResponseEntity<FastSmsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                FastSmsResponse.class);

        return response.getBody();
    }

    public boolean checkUserGetSms(SmsResponse smsResponse){


        if (smsResponse.getIds().isEmpty() || !smsResponse.getIsSuccessful()){
            return false;
        }

        int smsId = smsResponse.getIds().get(0).getID();

        Messages messages = new Messages();
        messages.setID(smsId);

        String url = "http://RestfulSms.com/api/MessageSend?id="+smsId;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("x-sms-ir-secure-token",NetApplication.token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Sms> entity = new HttpEntity<>(headers);


        ResponseEntity<CheckSmsSendedResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CheckSmsSendedResponse.class);
        CheckSmsSendedResponse response = responseEntity.getBody();

        return response.getIsSuccessful();
    }


    @Override
    public void run(String... strings) throws Exception {

//        NetApplication.token = getSecurityToken();
//
    }
}
