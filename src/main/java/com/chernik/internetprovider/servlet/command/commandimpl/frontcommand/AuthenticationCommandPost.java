package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand;

import com.chernik.internetprovider.servicelayer.service.AuthenticationService;
import com.chernik.internetprovider.servlet.command.Command;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationCommandPost implements Command {
    AuthenticationService authenticationService;

    public AuthenticationCommandPost(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void execute(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
