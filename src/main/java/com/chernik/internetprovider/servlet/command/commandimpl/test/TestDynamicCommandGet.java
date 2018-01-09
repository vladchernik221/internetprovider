package com.chernik.internetprovider.servlet.command.commandimpl.test;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.service.RegularExpressionService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/test/{\\d+}", method = RequestType.GET)
public class TestDynamicCommandGet implements Command {

    @Autowired
    private RegularExpressionService regularExpressionService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.getWriter().write(request.getRequestURI().split("/")[2]);
    }
}