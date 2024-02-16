package org.sayar.net.Controller.newController;


import org.sayar.net.Model.Filter.OrganizationFilter;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganService service;
    private final UserService userService;

    @Autowired
    public OrganizationController(OrganService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
    public ResponseEntity<?> getOrganUseres(@PathVariable String id) {
        return ResponseEntity.ok().body(service.getOrganUseres(id));

    }

    @GetMapping("check-organization-is-unique")
    public ResponseEntity<?> isUniqueOrganCode(@PathParam("organCode") String organCode) {
        return ResponseEntity.ok().body(service.isUniqueOrganCode(organCode));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/children/{parentOrganId}")
    public ResponseEntity<?> getParentOrganChildren(@PathVariable String parentOrganId) {
        return ResponseEntity.ok().body(service.getParentChilderen(parentOrganId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "organbyuser/{id}")
    public ResponseEntity<?> getOrganByUserName(@PathVariable String id) {
        return ResponseEntity.ok().body(service.getOrganByUserName(id));

    }

    @RequestMapping(method = RequestMethod.GET, value = "all/compress")
    public ResponseEntity<?> getOrganNameAndId() {
        return ResponseEntity.ok().body((service.getOrganNameAndId()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "nameid/{cityId}")
    public ResponseEntity<?> getOrganNameAndId(@PathVariable String cityId) {
        return ResponseEntity.ok().body((service.getOrganNameAndIdByCityId(cityId)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{organId}")
    public ResponseEntity<?> findOne(@PathVariable String organId) {
        return ResponseEntity.ok().body(service.findOneById(organId, Organization.class));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all")
    public ResponseEntity<?> getAll() {
        List<Organization> list = service.findAllNotLogicDeleted(Organization.class);
        return ResponseEntity.ok().body(list);
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> save(@RequestBody Organization organization) {
        if (service.isUniqueOrganCode(organization.getOrganCode())) {
            return ResponseEntity.ok().body("\"کد سازمانی وارد شده موجود است\"");
        } else {
            return ResponseEntity.ok().body(service.save(organization));
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody Organization organization) {
        if (service.isOrganCodeUniqueInUpdate(organization.getOrganCode(),organization.getId())) {
            return ResponseEntity.ok().body("\"کد سازمانی وارد شده موجود است\"");
        } else {
            return ResponseEntity.ok().body(service.updateOrganization(organization));
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(@PathParam("id") String id) {
        if (service.checkIfOrganHasChild(id)) {
            return ResponseEntity.ok().body("\"برای حذف این سازمان ابتدا سازمان های زیرمجموعه آن را پاک کنید\"");
        } else if (userService.ifOrganisationExistsInUser(id)) {
            return ResponseEntity.ok().body("\"برای حذف این سازمان ابتدا کاربرهایی که در این سازمان قرار دارند را پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(id, Organization.class));
        }
    }

    @GetMapping("get-all-parent-organizations")
    public ResponseEntity<?> getAllParentOrganization() {
        return ResponseEntity.ok().body(service.getAllParentOrgan());
    }

    @PostMapping("get-all-by-filter-and-pagination")
    public ResponseEntity<?> getAllByFilterAndPagination(@RequestBody OrganizationFilter organizationFilter, Pageable pageable, Integer totalElements) {
        return ResponseEntity.ok().body(service.getAllByFilterAndPagination(organizationFilter, pageable, totalElements));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneOrganization(@PathParam("orgId") String orgId) {
        return ResponseEntity.ok().body(service.getOneOrganisation(orgId));
    }

    @GetMapping("check-if-user-type-of-organization-used-in-user")
    public ResponseEntity<?> checkIfUserTypeOfOrganizationUsedInUser(@PathParam("organizationId") String organizationId,
                                                                     @PathParam("userTypeId") String userTypeId) {
        return ResponseEntity.ok().body(service.checkIfUserTypeOfOrganizationUsedInUser(organizationId, userTypeId));
    }

    @GetMapping("get-organization-name")
    public ResponseEntity<?> getOrganizationName() {
       return ResponseEntity.ok().body(service.getOrganizationName());
    }


}
