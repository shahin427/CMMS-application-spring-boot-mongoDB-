package org.sayar.net.Security;


import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Security.Model.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaqub on 2/28/17.
 */
@Component

public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user, UserType userType) {
        if (user == null) {
            return null;
        }

        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(userType),
                userType.getId()
        );

    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(UserType userType) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

//        if (userType != null) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(userType.getType().name().toUpperCase()));
//            GrantedAuthority grantedAuthority;
//            if (userType.getPrivilages() != null) {
//                for (Privilage p : userType.getPrivilages()) {
//
//                    grantedAuthorities.add(new SimpleGrantedAuthority(p.name().toUpperCase()));
//                }
//
//            }
//        }
//        Print.print("grantedAthourity",grantedAuthorities);
//        return grantedAuthorities;
        return null;
    }
}
