package org.sayar.net.Controller;

import org.sayar.net.Email.EmailConfiguration;
import org.sayar.net.Model.Captcha;
import org.sayar.net.Model.User;
import org.sayar.net.Service.UserService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailConfiguration emailConfiguration;

    @GetMapping("create")
    public ResponseEntity<?> createCaptcha() {
        String value = generateForCaptcha(5);
        Captcha captcha = new Captcha();
        captcha.setImage(value.getBytes());
        captcha.setCode(value.toLowerCase());

        //adding one minute to current date
        long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingOneMinutes = new Date(t + ONE_MINUTE_IN_MILLIS);
        captcha.setExpireDate(afterAddingOneMinutes);
        mongoOperations.save(captcha);
        return ResponseEntity.ok().body(captcha);
    }

    public static String generateForCaptcha(int length) {
        return generate("ZGAWSXCDERFVBTYHNMUIKLP23456789abcdefghkmnpqrstuvwxyz", length);
    }

    public static String generate(String candidateChars, int length) {
        StringBuilder sb;
        Random random;
        String result;
        sb = new StringBuilder();
        random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        result = sb.toString();
        return result;
    }

    @GetMapping("check-captcha-and-username-or-email")
    public ResponseEntity<?> checkCaptchaAndUsername(@PathParam("captcha") String captcha,
                                                     @PathParam("usernameOrEmail") String usernameOrEmail) {
        User user = userService.getUserForResetPassword(usernameOrEmail);
        Print.print("user12", user);
        if (user == null) {
            return ResponseEntity.ok().body("\"کاربری با این مشخصات وجود ندارد\"");
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("code").is(captcha));
            Captcha foundCaptcha = mongoOperations.findAndRemove(query, Captcha.class);
            if (foundCaptcha == null || foundCaptcha.getExpireDate().before(new Date())) {
                return ResponseEntity.ok().body("\"کپچا تغییر کند\"");
            } else {
                String token = generateForCaptcha(20);
                Print.print("token", token);
                userService.addTokenToResetPassword(user, token);
                String link = "یو ار ال صفحه فرستاده شود" + token;
                Print.print("link", link);
                emailConfiguration.sendResetPasswordCustom(user.getUserContact().getEmail(), link, user.getName(), user.getFamily(), user.getUsername());
                return ResponseEntity.ok().body("\" ایمیل فرستاده شد\"");
            }
        }
    }

}
