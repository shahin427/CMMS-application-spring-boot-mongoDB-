package org.sayar.net.Controller;

import org.sayar.net.Dao.UserTypeDTO;
import org.sayar.net.Model.DTO.OrganizationTermDTO;
import org.sayar.net.Model.UserType;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.newService.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/userType")
public class UserTypeController {

    @Autowired
    private UserTypeService service;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganService organService;

    @PostMapping("save")
    public ResponseEntity<?> createUserType(@RequestBody UserType userType) {
        return ResponseEntity.ok().body(service.createUserType(userType));
    }

    @GetMapping("get-all-by-filter")
    public ResponseEntity<?> filter(@PathParam("name") String name, @PathParam("type") String type, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.filter(name, type, pageable, totalElement));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneUserType(@PathParam("userTypeId") String userTypeId) {
        return ResponseEntity.ok().body(service.getOneUserType(userTypeId));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteUserType(@PathParam("userTypeId") String userTypeId) {
        if (userService.ifUserTypeExistsInUser(userTypeId)) {
            return ResponseEntity.ok().body("\"برای حذف این نقش ابتدا آن را از قسمت نقش کاربرها پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(userTypeId, UserType.class));
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUserType(@RequestBody UserTypeDTO userType) {
        return ResponseEntity.ok().body(service.updateUserType(userType));
    }

    @GetMapping("get-all-type")
    public ResponseEntity<?> getAllType() {
        return ResponseEntity.ok().body(service.getAllType());
    }

    @GetMapping("get-all-userTypes-of-the-organization")
    public ResponseEntity<?> getAllUserTypesOfThOrganization(@PathParam("organizationId") String organizationId) {
        return ResponseEntity.ok().body(service.getAllUserTypesOfThOrganization(organizationId));
    }

    @GetMapping("get-all-userTypes-by-term")
    public ResponseEntity<?> getAllUserTypesByTerm(@PathParam("term") String term, @PathParam("organizationId") String organizationId) {
        return ResponseEntity.ok().body(service.getAllUserTypesByTerm(term, organizationId));
    }

    @GetMapping("get-userType-of-organization-by-term")
    public ResponseEntity<?> getUserTypeOfOrganizationByTerm(@RequestBody OrganizationTermDTO organizationTermDTO, @PathParam("term") String term) {
        return ResponseEntity.ok().body(service.getUserTypeOfOrganizationByTerm(organizationTermDTO, term));
    }

    @GetMapping("get-user-type-list-by-organization-id")
    public ResponseEntity<?> getUserTypeListByOrganizationId(@PathParam("orgId") String orgId) {
        return ResponseEntity.ok().body(service.getUserTypeListByOrganizationId(orgId));
    }
}
