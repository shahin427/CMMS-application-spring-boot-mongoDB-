package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.MessageDTO;
import org.sayar.net.Model.newModel.Messaging.Message;
import org.sayar.net.Service.newService.MessageService;
import org.sayar.net.Tools.Print;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("messaging")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PutMapping("update")
    public ResponseEntity<?> updateMessage(@RequestBody MessageDTO message) {
        if (message == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("بادی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(messageService.updateMessage(message));
        }
    }

    @PostMapping("save")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("بادی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(messageService.createMessage(message));
        }
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneMessage(@PathParam("userId") String userId) {
        if (userId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(messageService.getOneMessage(userId));
        }
    }

}
