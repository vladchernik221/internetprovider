package com.chernik.internetprovider.servlet.mapper;

import com.chernik.internetprovider.context.Component;
import com.chernik.internetprovider.exception.BadRequestException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.persistence.entity.UserRole;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserMapper extends Mapper<User> {

    @Override
    public User create(HttpServletRequest request) throws BadRequestException {
        User user = new User();
        user.setLogin(getMandatoryString(request.getParameter("login")));
        user.setPassword(getMandatoryString(request.getParameter("password")));
        user.setUserRole(UserRole.valueOf(getMandatoryString(request.getParameter("userRole"))));
        return user;
    }
}
