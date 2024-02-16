package org.sayar.net.Security.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {

    private String userId;
    private String userName;
    private String password;
//    private String organId;
    private String userTypeId;
    private List<? extends GrantedAuthority> authorities;


    public JwtUser(String userId, String userName, String password,
                   List<? extends GrantedAuthority> authorities,
                   String userTypeId) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
//        this.organId = organId;
        this.userTypeId = userTypeId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public String getOrganId() {
//        return organId;
//    }

//    public void setOrganId(String organId) {
//        this.organId = organId;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
