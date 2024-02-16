package org.sayar.net.Security;

import org.sayar.net.Security.Model.JwtUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TokenPrinciple {

//    public String getOrganCode() {
//        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return jwtUser.getOrganId();
//    }

    public String getUserId() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser.getUserId();
    }

    public String getUserName() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser.getUsername();
    }

    public String getUserTypeId() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser.getUserTypeId();
    }


//    public long getOrganCodePrincipal(){
//
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//       JwtUser jwtUser = (JwtUser) principal.getPrincipal();
//        return jwtUser.getOrganId();
////        return principal.getPrincipal();
//    }
//
//    public long getUserIdPrincipal(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        JwtUser jwtUser = (JwtUser) principal.getPrincipal();
//        return jwtUser.getUserId();
//    }
//    public String getUserNamePrincipal(){
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        JwtUser jwtUser = (JwtUser) principal.getPrincipal();
//        return jwtUser.getUsername();
//    }


}
