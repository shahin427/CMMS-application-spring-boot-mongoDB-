//package org.sayar.net.Service;
//
//import org.sayar.net.Model.Email.Email;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.File;
//
//@Component
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    public JavaMailSender emailSender;
//
//
//    @Async
//    @Override
//    public boolean sendEmail(Email email) {
////        try {
//
//            if (email.getAddress()==null){
//                return false;
//            }else {
//
//                MimeMessage message = emailSender.createMimeMessage();
//                try {
//                    message.setContent(email.getText(),"text/html");
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//                MimeMessageHelper helper = new MimeMessageHelper(message);
////            helper.setText(email.getText());
////            helper.setText(email.getText());
//
//                try {
//                    helper.setSubject(email.getSubject());
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    helper.setTo(email.getAddress());
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                }
//
//                emailSender.send(message);
//                return true;
//
//            }


//        }catch (Exception e){

//        //    System.out.println(e);
////            return false;
////        }
//    }
//
//    @Override
//    public boolean sendMessageWithAttachment(Email email, File file) {
//        try {
//
//
//            MimeMessage message = emailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(email.getAddress());
//            helper.setSubject(email.getSubject());
//            helper.setText(email.getText());
//
//            FileSystemResource fileSystemResource
//                    = new FileSystemResource(file);
//            helper.addAttachment(file.getName(), fileSystemResource);
//            emailSender.send(message);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }
//
//
//
//}
//