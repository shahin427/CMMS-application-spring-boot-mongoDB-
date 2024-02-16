package org.sayar.net.Security.Service;

import org.sayar.net.Dao.UserDao;
import org.sayar.net.Dao.UserTypeDao;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserService implements UserDetailsService {

    @Autowired
    private UserDao dao;
    @Autowired
    private UserTypeDao userTypeDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = dao.getUserForToken(s);
        UserType userType = userTypeDao.getOneUserType(user.getUserTypeId());
        return JwtUserFactory.create(user, userType);
    }
}
