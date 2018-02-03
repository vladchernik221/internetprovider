package com.chernik.internetprovider.servlet.command.impl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.exception.UnableSaveEntityException;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@HttpRequestProcessor(uri = "/user/{\\d+}/block", method = RequestType.POST)
public class UserBlockCommandPost implements Command {

    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws BaseException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Long id = Long.valueOf(request.getRequestURI().split("/")[2]);

        if (!id.equals(user.getUserId())) {
            userService.block(id);
        } else {
            throw new UnableSaveEntityException("Can't block yourself");
        }
    }
}
