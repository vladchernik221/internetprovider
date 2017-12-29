package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.HttpRequestParameter;
import com.chernik.internetprovider.servlet.command.HttpRequestType;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityConfigHandlerImpl implements SecurityConfigHandler {//TODO log
    private Map<HttpRequestParameter, List<UserRole>> securityConfig = new HashMap<>();
    private List<HttpRequestParameter> bufferPatterns;
    private boolean enable;

    @Override
    public boolean isAvailable(HttpRequestType method, String uri, UserRole userRole) {
        List<UserRole> userRoles = securityConfig.get(new HttpRequestParameter(uri, method));
        return userRoles == null || userRoles.contains(userRole) || !enable;
    }


    public SecurityConfigHandlerImpl antMatcher(HttpRequestType method, String... patterns) {
        if (bufferPatterns == null) {
            bufferPatterns = new LinkedList<>();
        }
        bufferPatterns.addAll(Arrays.stream(patterns)
                .map(s -> new HttpRequestParameter(s, method)).collect(Collectors.toList()));
        return this;
    }

    public SecurityConfigHandlerImpl withRole(UserRole... roles) {
        bufferPatterns.forEach(s -> securityConfig.put(s, Arrays.asList(roles)));
        bufferPatterns = null;
        return this;
    }

    public SecurityConfigHandlerImpl enable() {
        enable = true;
        return this;
    }
}
