package org.sayar.net.PrimaryUser;

import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.newService.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("primary-user")
public class PrimaryUser {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganService organService;

    @Autowired
    private UserTypeService userTypeService;

    @GetMapping()
    public ResponseEntity<?> primaryUser() {
        UserType userType = userTypeService.createFirstUserType();
        Organization organization = organService.createFirstOrganization(userType);
        return ResponseEntity.ok().body(userService.createPrimaryUser(organization, userType));
    }
}