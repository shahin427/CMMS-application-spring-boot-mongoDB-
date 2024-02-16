package org.sayar.net.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class testController {

    @GetMapping("test")
    public ResponseEntity<?> getAllIdAndTitle() {
        return ResponseEntity.ok().body(new Date());
    }
}
