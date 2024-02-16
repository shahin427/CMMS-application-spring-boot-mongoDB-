//package org.sayar.net.Controller;
//
//import org.sayar.net.Enumes.ResponseKeyWord;
//import org.sayar.net.Model.Email.Email;
//import org.sayar.net.Model.ResponseModel.ResponseContent;
//import org.sayar.net.Service.EmailService;
//import org.sayar.net.Service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//
//@RestController
//@RequestMapping("/contact")
//public class ContactController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private UserService userService;
//
//
//
//    @RequestMapping(method = RequestMethod.GET,value = "/email")
//    public ResponseEntity<?> sendEmail(@RequestBody Email email){
//
//
//
//        return new ResponseContent().sendOkResponseEntity(
//                "Email Send to "+email.getAddress(),
//                emailService.sendEmail(email)
//        );
//
//
//    }
//    @RequestMapping(method = RequestMethod.GET,value = "/emailfile")
//    public ResponseEntity<?> sendEmail(@RequestBody Email email, @RequestParam("file") MultipartFile multipartFile){
//
//
//        try {
//            File convFile = new File(multipartFile.getOriginalFilename());
//            convFile.createNewFile();
//            FileOutputStream fos = new FileOutputStream(convFile);
//            fos.write(multipartFile.getBytes());
//            fos.close();
//
//            return new ResponseContent().sendOkResponseEntity(
//                    "Email Send to "+email.getAddress(),
//                    emailService.sendMessageWithAttachment(email,convFile)
//            );
//        } catch (FileNotFoundException e) {
//            return new ResponseContent().sendErrorResponseEntity("Error in send email : file not found", ResponseKeyWord.FILE_NOT_FOUND,404);
//        } catch (IOException e) {
//            return new ResponseContent().sendErrorResponseEntity("Error in send email : file not found", ResponseKeyWord.FILE_NOT_FOUND,404);
//        }
//    }

//    @RequestMapping(method = RequestMethod.GET,value = "/telegram")
//    public ResponseEntity<?> sendTelegramMessage(@RequestBody TelegramMessage message){
//
//
//        User user = userService.findOne(message.getUser().getId());
//        message.setUser(user);
//
//        return new ResponseContent().sendOkResponseEntity(
//                "Send telegram message to :"+message.getUser().getUsername(),
//                telegramService.senMessage(message)
//        );
//    }
//}
