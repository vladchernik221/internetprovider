package com.chernik.internetprovider.servlet.command.commandimpl.user;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.User;
import com.chernik.internetprovider.service.UserService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@HttpRequestProcessor(uri = "/user", method = RequestType.GET)
public class UserListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(UserListCommandGet.class);

    private static final String USER_LIST_PAGE = "/WEB-INF/jsp/user/userList.jsp";

    @Autowired
    private UserService userService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, BaseException, IOException {
        int pageNumber = 0;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page")) - 1;
        }
        String userRole = "";
        if (request.getParameter("role") != null) {
            userRole = request.getParameter("role");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(USER_LIST_PAGE);
        Page<User> usersPage = userService.getPage(new Pageable(pageNumber, 10), userRole);//TODO to property or constant or somewhere
        request.setAttribute("usersPage", usersPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", USER_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
