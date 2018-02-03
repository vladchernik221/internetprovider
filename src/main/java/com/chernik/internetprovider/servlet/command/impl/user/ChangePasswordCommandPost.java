package com.chernik.internetprovider.servlet.command.impl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@HttpRequestProcessor(uri = "/user/{\\d+}/password", method = RequestType.POST)
public class ChangePasswordCommandPost implements Command {

    @Autowired
    private UserService userService;

    @Autowired
    private BaseMapper baseMapper;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Long userId = Long.valueOf(request.getRequestURI().split("/")[2]);
        String newPassword = baseMapper.getMandatoryString(request, "newPassword");

        userService.changePassword(userId, newPassword, user);
    }
}
