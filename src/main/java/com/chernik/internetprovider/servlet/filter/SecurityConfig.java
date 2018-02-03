package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.context.AfterCreate;
import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.RequestType;

@Component
public class SecurityConfig {
    @AfterCreate
    public void initConfig(SecurityConfiguration securityConfiguration) {
        securityConfiguration
                .enable()
                .matcher(RequestType.GET, "/contract/{\\d+}", "/contract/{\\d+}/annex", "/contract/annex/{\\d+}", "/contract/annex/{\\d+}/account").withRole(UserRole.CUSTOMER, UserRole.SELLER)
                .matcher(RequestType.GET, "/contract/new", "/contract", "/contract/{\\d+}/annex/new").withRole(UserRole.SELLER)
                .matcher(RequestType.POST, "/contract/new", "/contract/{\\d+}/dissolve", "/contract/{\\d+}/annex/new", "/contract/annex/{\\d+}/cancel", "/client/individual", "client/legal").withRole(UserRole.SELLER)
                .matcher(RequestType.GET, "/discount/new", "/discount/{\\d+}/edit", "/service/new", "/service/{\\d+}/edit", "/tariff-plan/new", "/tariff-plan/{\\d+}/edit", "/user/new", "/user").withRole(UserRole.ADMIN)
                .matcher(RequestType.POST, "/discount/new", "/discount/{\\d+}", "/discount/{\\d+}/remove", "/service/new", "/service/{\\d+}", "/service/{\\d+}/archive", "/tariff-plan/new", "/tariff-plan/{\\d+}", "/tariff-plan/{\\d+}/archive", "/user/new", "/user/{\\d+}/block").withRole(UserRole.ADMIN)
                .matcher(RequestType.GET, "/employee").withRole(UserRole.SELLER, UserRole.ADMIN)
                .matcher(RequestType.GET, "/user/{\\d+}/password").withRole(UserRole.CUSTOMER, UserRole.SELLER, UserRole.ADMIN)
                .matcher(RequestType.POST, "/user/{\\d+}/password").withRole(UserRole.CUSTOMER, UserRole.SELLER, UserRole.ADMIN);
    }
}
