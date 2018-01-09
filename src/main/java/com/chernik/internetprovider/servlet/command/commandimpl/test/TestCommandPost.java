package com.chernik.internetprovider.servlet.command.commandimpl.test;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/test", method = RequestType.POST)
public class TestCommandPost implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, BaseException {
        //PrintWriter out = response.getWriter();
        //out.write("/");
        response.getWriter().write("mur");
        response.setStatus(HttpServletResponse.SC_CONFLICT);
    }
}