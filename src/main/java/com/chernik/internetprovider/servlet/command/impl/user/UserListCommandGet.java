package com.chernik.internetprovider.servlet.command.impl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/user", method = RequestType.GET)
public class UserListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(UserListCommandGet.class);

    private static final String USER_LIST_PAGE = "/WEB-INF/jsp/user/userList.jsp";

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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, BaseException, IOException {
        Integer pageNumber = baseMapper.getNotMandatoryInt(request, "page");
        pageNumber = (pageNumber != null) ? pageNumber - 1 : 0;

        String userRole = baseMapper.getNotMandatoryString(request, "role");
        userRole = (userRole != null) ? userRole : "";

        RequestDispatcher dispatcher = request.getRequestDispatcher(USER_LIST_PAGE);
        Page<User> usersPage = userService.getPage(new Pageable(pageNumber, 10), userRole);
        request.setAttribute("usersPage", usersPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", USER_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
