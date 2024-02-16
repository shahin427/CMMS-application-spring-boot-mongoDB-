package org.sayar.net.Controller;

import org.sayar.net.Dao.UserTypeDao;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Filter.UserFilter;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Security.JwtTokenUtil;
import org.sayar.net.Security.JwtUserFactory;
import org.sayar.net.Security.Model.JwtAuthenticaationResponse;
import org.sayar.net.Security.Model.JwtUser;
import org.sayar.net.Security.Model.UserPass;
import org.sayar.net.Security.Password;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.*;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.sayar.net.exceptionHandling.ApiWrongUserNamePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private UserTypeDao userTypeDao;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetTemplateService assetTemplateService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private PartService partService;


    @RequestMapping(method = RequestMethod.POST, value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody UserPass request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.ok().body("\"اطلاعات وارد شده ناقص است\"");
        }
        User user = service.getUserForToken(request.getUsername());
        if (user != null) {
            UserType userType = userTypeDao.getOneUserType(user.getUserTypeId());
            UserAndUserTypDTO userAndUserTypDTO = new UserAndUserTypDTO(user, userType);
            if (Password.checkPassword(request.getPassword(), user.getPassword()) && user.getUsername().equals(request.getUsername())) {
                JwtUser jwtuser = JwtUserFactory.create(user, userType);   //creating token for considered user
                return ResponseEntity.ok().body(new JwtAuthenticaationResponse(JwtTokenUtil.generateToken(jwtuser), userAndUserTypDTO, false));
            } else {
                return ResponseEntity.ok().body("\"نام کاربری یا پسورد اشتباه است\"");
            }
        } else {
            return ResponseEntity.ok().body("\"کاربری با این مشخصات وجود ندارد\"");
        }
    }

    @PostMapping("by-id-list")
    public ResponseEntity<?> getByIdList(@RequestBody List<String> idList) {
        return new ResponseContent().sendOkResponseEntity("", service.getByIdList(idList));
    }

    @PostMapping("get-all-by-filter-and-pagination")
    public ResponseEntity<?> getAllByFilter(@RequestBody UserFilter userFilter, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByFilter(userFilter, pageable, totalElement));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllIdAndTitle() {
        return ResponseEntity.ok().body(service.getAllIdAndTitle());
    }

    @PostMapping("check-national-code")
    public ResponseEntity<?> checkNationalCode(@RequestBody User user) {
        return ResponseEntity.ok().body("{\"exist\":" + service.haveUserNationalCode(user) + "}");
    }

    @PostMapping("save-main-information")
    public ResponseEntity<?> createMainInformationOfUser(@RequestBody UserMainInformationStringDTO userMainInformationStringDTO) {
//        if (userMainInformationStringDTO.getNationalCode() != null && !userMainInformationStringDTO.getNationalCode().equals("")) {
//            if (service.checkIfNationalCodeExist(userMainInformationStringDTO.getNationalCode())) {
//                return ResponseEntity.ok().body("\"کد ملی وارد شده قبلا ثبت گردیده است\"");
//            }
//        }

//        if (service.checkIfUsernameIsRepetitive(userMainInformationStringDTO.getUsername())) {
//            return ResponseEntity.ok().body("\"نام کاربری انتخاب شده قبلا ثبت گردیده است\"");
//        }
        return ResponseEntity.ok().body(service.createMainInformationOfUser(userMainInformationStringDTO));
    }

    @GetMapping("get-main-information")
    public ResponseEntity<?> getMainInformationOfUserByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getMainInformationOfUser(userId));
    }

    @PutMapping("update-main-information")
    public ResponseEntity<?> updateMainInformation(@RequestBody UserMainInformationStringDTO userMainInformationDTO, @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.updateMainInformation(userMainInformationDTO, userId));
    }

    @GetMapping("get-secondary-information-of-user")
    public ResponseEntity<?> getSecondaryInformationOfUser(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getSecondaryInformationOfUser(userId));
    }

    @PutMapping("update-secondary-information-of-user")
    public ResponseEntity<?> updateSecondaryInformationOfUser(@RequestBody UserSecondaryInformationDTO userSecondaryInformationDTO) {
        if (userSecondaryInformationDTO.getUserId() == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.updateSecondaryInformationOfUser(userSecondaryInformationDTO));
        }
    }

    @PutMapping("update-child-users-for-report-to")
    public ResponseEntity<?> updateChildUsersForReportTo(@RequestBody UserChildUsersDTO userChildUsersDTO) {
        return ResponseEntity.ok().body(service.updateChildUsersForReport(userChildUsersDTO));
    }

    @GetMapping("get-all-children-users-id-by-user-id")
    public ResponseEntity<?> getAllChildUsersIdOfUserByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getAllChildUsersIdOfUserByUserId(userId));
    }

    @GetMapping("get-user-children-by-user-id")
    public ResponseEntity<?> getUserChildrenByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getUserChildrenByUserId(userId));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteUserByUserId(@PathParam("userId") String userId) {
        if (projectService.checkIfUserExistsInProject(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت پروژه های اتمام نیافته حذف نمایید.\"");
        } else if (workOrderService.checkIfUserExistWorkOrder(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت سفارش کارهای اتمام نیافته پحذف نمایید.\"");
        } else if (scheduleMaintananceService.checkIfUserExistsScheduleMaintenance(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت زمانبندی نت اتمام نیافته حذف نمایید.\"");
        } else if (assetService.checkIfUserExistsAsset(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت دارایی ها حذف نمایید.\"");
        } else if (assetTemplateService.checkIfUserExistsAssetTemplate(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت قالب دارایی ها حذف نمایید.\"");
        } else if (activityService.checkIfUserExistsActivity(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت فرایندها حذف نمایید.\"");
        } else if (partService.checkIfUserExistsInPart(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت قطعات حذف نمایید.\"");
        } else if (service.checkIfUserIsParentOfAnotherUser(userId)) {
            return ResponseEntity.ok().body("\"برای حذف این کاربر ابتدا کاربر را از قسمت گزارش به کاربرهای دیگر حذف نمایید.\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(userId, User.class));
        }
    }

    @GetMapping("get-all-users-by-term-and-pagination")
    public ResponseEntity<?> getAllUsersByTermAndPagination(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllUsersByTermAndPagination(term, pageable, totalElement));
    }

    @GetMapping("get-all-users-except-one")
    public ResponseEntity<?> getAllUsersExceptOne(@PathParam("userId") String userId) {
        if (userId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.getAllUsersExceptOne(userId));
        }
    }

    @GetMapping("check-if-user-name-is-repetitive")
    public ResponseEntity<?> checkIfUsernameIsRepetitive(@PathParam("username") String username) {
        return ResponseEntity.ok().body(service.checkIfUsernameIsRepetitive(username));
    }

    @GetMapping("get-all-organizations-by-user-type-id")
    public ResponseEntity<?> getAllOrganizationsByUserTypeId(@PathParam("userTypeId") String userTypeId) {
        return ResponseEntity.ok().body(service.getAllOrganizationsByUserTypeId(userTypeId));
    }

    @GetMapping("get-organizations-by-user-id")
    public ResponseEntity<?> getOrganizationsByAUserId(String userId) {
        return ResponseEntity.ok().body(service.getOrganizationsByAUserId(userId));
    }

    @GetMapping("check-if-national-code-exist-for-update")
    public ResponseEntity<?> checkIfNationalCodeExistForUpdate(@PathParam("code") String code, @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.checkIfNationalCodeExistForUpdate(code, userId));
    }

    @GetMapping("check-if-user-name-exist-for-update")
    public ResponseEntity<?> checkIfUsernameExistForUpdate(@PathParam("username") String username, @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.checkIfUsernameExistForUpdate(username, userId));
    }

    @GetMapping("get-all-sub-users-of-user-by-user-id")
    public ResponseEntity<?> getAllSubUsersOfUserByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getAllSubUsersOfUserByUserId(userId));
    }

    @GetMapping("check-if-user-name-is-unique")
    public ResponseEntity<?> checkIfUsernameIsUnique(@PathParam("username") String username) {
        if (username == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("یوزرنیم فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.checkIfUsernameIsUnique(username));
        }
    }

    @GetMapping("get-all-users-with-relevant-user-type")
    public ResponseEntity<?> getAllUsersWithRelevantUserType() {
        List<User> users = service.getAllUsersWithRelevantUserType();
        List<String> userTypeIdList = new ArrayList<>();
        users.forEach(user -> userTypeIdList.add(user.getUserTypeId()));
        List<UserType> userTypeList = userTypeDao.getRelevantUserType(userTypeIdList);
        return ResponseEntity.ok().body(UserDTOWithUserTypeDTO.map2(users, userTypeList));
        //TODO here should be changed after adding multi userType
    }

    @GetMapping("check-if-password-and-reset-password-are-same")
    public ResponseEntity<?> checkIfPasswordAndResetPasswordAreSame(@PathParam("password") String password,
                                                                    @PathParam("resetPassword") String resetPassword) {
        return ResponseEntity.ok().body(service.checkIfPasswordAndResetPasswordAreSame(password, resetPassword));
    }

    @GetMapping("get-sub-users-in-user-get-all")
    public ResponseEntity<?> getSubUsersInUserGetAll(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getSubUsersInUserGetAll(userId));
    }

    @GetMapping("get-org-and-user-type-list-by-user-id")
    public ResponseEntity<?> getOrgAndUserTypeListByUserId(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getOrgAndUserTypeListByUserId(userId));
    }

    @PostMapping("first-sign-in")
    public ResponseEntity<?> firstSignIn(@RequestBody SignInDTO signInDTO) {

        if (signInDTO.getUserName() == null || signInDTO.getPassword() == null) {
            throw new ApiWrongUserNamePassword("اطلاعات وارد شده ناقص است");
        }

        User user = service.getUserForToken(signInDTO.getUserName());
        if (user != null) {
            if (Password.checkPassword(signInDTO.getPassword(), user.getPassword()) && user.getUsername().equals(signInDTO.getUserName())) {
                return ResponseEntity.ok().body(service.getOrgAndUserTypeListByUserId(user.getId()));
            } else {
                throw new ApiWrongUserNamePassword("کاربری با ا ین مشخصات وجود ندارد");
            }
        } else {
            throw new ApiWrongUserNamePassword("کاربری با این مشخصات وجود ندارد");
        }
    }

    @GetMapping("get-all-users-of-user-type")
    public ResponseEntity<?> getAllUsersOfAUserType(@PathParam("userTypeId") String userTypeId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllUsersOfAUserType(userTypeId, pageable, totalElement));
    }

    @GetMapping("get-user-with-userType")
    public ResponseEntity<?> getUserWithUserType() {
        return ResponseEntity.ok().body(service.getUserWithUserType());
    }

    @GetMapping("get-user-report-to")
    public ResponseEntity<?> getUserReportTo(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(service.getUserReportTo(userId));
    }


//    @PostMapping("second-login")
//    public ResponseEntity<?> secondLogin(@RequestBody UserPass request) {
//
//        if (request.getUsername() == null || request.getUserTypeId() == null) {
//            return ResponseEntity.ok().body("\"اطلاعات وارد شده ناقص است\"");
//        }
//
//        User user = service.getUserForToken(request.getUsername());
//        UserType userType = userTypeDao.getOneUserType(request.getUserTypeId());
//        UserAndUserTypDTO userAndUserTypDTO = new UserAndUserTypDTO(user, userType);
//        JwtUser jwtuser = JwtUserFactory.create(user, userType);
//        return ResponseEntity.ok().body(new JwtAuthenticaationResponse(JwtTokenUtil.generateToken(jwtuser), userAndUserTypDTO, false));
//    }

}
