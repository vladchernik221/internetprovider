package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/user/{\\d+}/password", method = RequestType.POST)
public class ChangePasswordCommandPost implements Command {

    @Autowired
    private UserService userService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        Long userId = Long.valueOf(request.getRequestURI().split("/")[2]);
        String newPassword = request.getParameter("newPassword");

        userService.changePassword(userId, newPassword);
    }
}
