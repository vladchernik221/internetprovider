package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

@Component
public class SecurityConfig {
    @AfterCreate
    public void initConfig(SecurityConfigHandlerImpl securityConfigHandlerImpl) {
        securityConfigHandlerImpl.enable()
                .antMatcher(HttpRequestType.GET, "/test").withRole(UserRole.ADMIN, UserRole.SELLER);
    }
}