package com.chernik.internetprovider.servlet.command.commandimpl.errorcommand;

import com.chernik.internetprovider.exception.BaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ErrorCommand {
    void execute(HttpServletRequest request, HttpServletResponse response, BaseException e)
            throws ServletException, IOException, BaseException;
}
