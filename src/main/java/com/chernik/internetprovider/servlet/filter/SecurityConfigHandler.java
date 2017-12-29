package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

public interface SecurityConfigHandler {
    boolean isAvailable(HttpRequestType method, String uri, UserRole userRole);
}
