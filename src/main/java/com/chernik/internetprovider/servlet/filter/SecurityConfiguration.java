package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.RequestType;

public interface SecurityConfiguration {
    SecurityConfigHandlerImpl matcher(RequestType method, String... patterns);

    SecurityConfigHandlerImpl withRole(UserRole... roles);

    SecurityConfigHandlerImpl enable();
}
