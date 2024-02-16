package org.sayar.net.Email;

import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.websocket.server.PathParam;
import java.io.File;

/**
 * created by shahinR
 */

@RestController
@RequestMapping("/mail")
public class EmailConfiguration {

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/simple-mail")
    public void sendSimpleEmail(@PathParam("mailTo") String mailTo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailTo);
        mailMessage.setFrom("shahinrajaei96@gmail.com");
        mailMessage.setSubject("به حامی خوش آمدید");
        mailMessage.setText("شما در نرم افزار تعمیرات و نگهداری حامی ثبت نام گردیدید");
        javaMailSender.send(mailMessage);
    }

    @PostMapping("/mime-mail")
    public void sendMimeEmail(@PathParam("mailTo") String mailTo, @PathParam("attachmentFilePath") String attachmentFilePath) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("shahinrajaei96@gmail.com");
        mimeMessageHelper.setTo(mailTo);
        mimeMessageHelper.setSubject("به حامی خوش آمدید");
        mimeMessageHelper.setText("شما در نرم افزار تعمیرات و نگهداری حامی ثبت نام گردیدید");
        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentFilePath));
        mimeMessageHelper.addAttachment("تقدیر نامه حامی", fileSystemResource);
        javaMailSender.send(mimeMessage);
    }

    public void sendResetPasswordCustom(String email, String link, String name, String family, String username) {

        String mail =
                "<!DOCTYPE html>\n" +
                        "<html lang=\\ \"en\\\" dir=\\ \"rtl\\\">\n" +
                        "\n" +
                        "<head>\n" +
                        "    <meta charset=\\ \"UTF-8\\\">\n" +
                        "    <title>Reset password</title>\n" +
                        "    <style>\n" +
                        "        .main {\n" +
                        "            display: flex;\n" +
                        "            width: 100%;\n" +
                        "            justify-content: center;\n" +
                        "            flex-direction: column;\n" +
                        "            align-items: center;\n" +
                        "            height: 90vh;\n" +
                        "        }\n" +
                        "        \n" +
                        "        a {\n" +
                        "            color: darkslateblue;\n" +
                        "        }\n" +
                        "        \n" +
                        "        a:hover {\n" +
                        "            text-decoration: underline;\n" +
                        "            cursor: pointer;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "    <div class=\\ \"main\\\" dir=\\ \"rtl\\\">\n" +
                        "        <p dir=\\ \"rtl\\\">gender نام کاربری شما (username) میباشد,جهت تغییر رمز عبور خود<a href=farzad>اینجا کلیک</a> .کنید</p>\n";

        String mailLink = mail.replace("shahin", link);
        String man = "جناب آقای fl";
        String woman = "سرکار خانم fl";
        String dearUser = "کاربر گرامی fl";
        String mailGender;
        dearUser = dearUser.replace("fl", name + " " + family);
//        if (gender.equals(User.Gender.MEN)) {
//            man = man.replace("fl", firstName + " " + lastName);
//            mailGender = mailLink.replace("gender", man);
//        } else {
//            woman = woman.replace("fl", firstName + " " + lastName);
//            mailGender = mailLink.replace("gender", woman);
//        }

        mailGender = mailLink.replace("gender", dearUser);
        String mailFull = mailGender.replace("username", username);
//        try {
//            System.out.println("try1");
//            email.sendEmail();
//            email.sendEmailWithAttachment(to,subject,"ریست پسوورد",mailFull);
//            emailComponent.sendMail(to, subject, "ریست پسوورد", mailFull);
//            System.out.println("try2");
//        } catch (Exception e) {
//            System.out.println("catch");
//            e.printStackTrace();
//        }
//		sendEmail(to, subject, mail);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setFrom("shahinrajaei96@gmail.com");
        mailMessage.setSubject("تغییر_گذرواژه");
        mailMessage.setText(mailFull);
        Print.print("mailMessage",mailMessage);
        javaMailSender.send(mailMessage);
    }
}
