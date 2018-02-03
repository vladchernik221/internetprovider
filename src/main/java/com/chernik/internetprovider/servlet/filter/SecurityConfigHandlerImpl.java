package com.chernik.internetprovider.servlet.filter;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.persistence.entity.UserRole;
import com.chernik.internetprovider.servlet.command.RequestParameter;
import com.chernik.internetprovider.servlet.command.RequestType;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityConfigHandlerImpl implements SecurityConfiguration, SecurityConfigHandler {
    private Map<RequestParameter, List<UserRole>> securityConfig = new HashMap<>();
    private List<RequestParameter> bufferPatterns;
    private boolean enable;

    @Override
    public boolean isAvailable(RequestType method, String uri, UserRole userRole) {
        List<UserRole> userRoles = securityConfig.get(new RequestParameter(uri, method));
        return userRoles == null || userRoles.contains(userRole) || !enable;
    }


    public SecurityConfigHandlerImpl matcher(RequestType method, String... patterns) {
        if (bufferPatterns == null) {
            bufferPatterns = new LinkedList<>();
        }
        bufferPatterns.addAll(Arrays.stream(patterns)
                .map(s -> new RequestParameter(s, method)).collect(Collectors.toList()));
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
