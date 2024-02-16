package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Certification;
import org.sayar.net.Service.CertificationService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/certification")
public class CertificationController {
    @Autowired
    private CertificationService certificationService;

    @PostMapping("save")
    public ResponseEntity<?> createUserCertification(@RequestBody Certification certification) {
        return ResponseEntity.ok().body(certificationService.createUserCertification(certification));
    }

    @GetMapping("get-all-certification")
    public ResponseEntity<?> getAllUserCertification() {
        return ResponseEntity.ok().body(certificationService.getAllUserCertification());
    }

    @GetMapping("get-user-certifications")
    public ResponseEntity<?> getOneUserCertification(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(certificationService.getOneCertification(userId));
    }

    @GetMapping("get-one-certification")
    public ResponseEntity<?> getAnyCertification(@PathParam("certificationId") String certificationId) {
        if (certificationId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(certificationService.getAnyCertification(certificationId));
        }
    }

    @PutMapping("update-certification")
    public ResponseEntity<?> updateCertification(@RequestBody Certification certification) {
        return ResponseEntity.ok().body(certificationService.updateCertification(certification));
    }

    @DeleteMapping("delete-certification")
    public ResponseEntity<?> deleteCertification(String id) {
        if (id == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(certificationService.logicDeleteById(id, Certification.class));
        }
    }

    @GetMapping("get-all-user-certification")
    public ResponseEntity<?> getAllUserCertification(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(certificationService.getUsersCertification(userId));
    }

}
