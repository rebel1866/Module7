package com.epam.esm.security.userdetail;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDao.findUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exists");
        }
        return new SecurityUser(user.getEmail(), user.getPassword(), new ArrayList<>(user.getRole().getAuthorities()),
                user.getStatus().equals(Status.ACTIVE));
    }
}
