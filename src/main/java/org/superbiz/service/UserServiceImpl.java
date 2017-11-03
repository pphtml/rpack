package org.superbiz.service;

import org.superbiz.config.ApplicationModule;
import org.superbiz.dao.UserDAO;

import javax.inject.Inject;

public class UserServiceImpl implements UserService {
    private final ApplicationModule.Config config;

    @Inject
    UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO, ApplicationModule.Config config) {
        this.userDAO = userDAO;
        this.config = config;
    }

    @Override
    public String getUsername() {
        return config.getNodeName();
    }
//
//    @Override
//    public String getUsername() {
//        return userDAO.getUsername();
//    }
}
