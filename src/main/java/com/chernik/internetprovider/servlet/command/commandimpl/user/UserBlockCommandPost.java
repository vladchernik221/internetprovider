package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@HttpRequestProcessor(uri = "/user/{\\d+}/block", method = RequestType.POST)
public class UserBlockCommandPost implements Command {

    @Autowired
    private UserService userService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);
        userService.block(id);
    }
}
