package com.chernik.internetprovider.servlet.command;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {
    void execute(ServletContext context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
