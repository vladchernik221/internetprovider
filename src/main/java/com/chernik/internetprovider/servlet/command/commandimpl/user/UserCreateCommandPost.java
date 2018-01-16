package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.UserMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@HttpRequestProcessor(uri = "/user/new", method = RequestType.POST)
public class UserCreateCommandPost implements Command {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException, IOException {
        User user = userMapper.create(request);
        Long generatedId = userService.create(user);
        response.getWriter().write(generatedId.toString());
    }
}