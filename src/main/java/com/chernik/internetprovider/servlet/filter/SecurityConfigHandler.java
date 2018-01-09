package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.RequestType;

public interface SecurityConfigHandler {
    boolean isAvailable(RequestType method, String uri, UserRole userRole);
}
