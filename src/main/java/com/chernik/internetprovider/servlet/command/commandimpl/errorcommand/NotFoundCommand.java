package com.chernik.internetprovider.servlet.command.commandimpl.errorcommand;

import com.chernik.internetprovider.servlet.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NotFoundCommand implements Command {
    private final static String NOT_FOUND_JSP = "/WEB-INF/jsp/404.jsp";

    @Override
    public void execute(ServletContext context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = context.getRequestDispatcher(NOT_FOUND_JSP);
        dispatcher.forward(request, response);
    }
}
