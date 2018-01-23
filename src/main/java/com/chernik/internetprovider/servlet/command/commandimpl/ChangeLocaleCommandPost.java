package com.chernik.internetprovider.servlet.command.commandimpl;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@HttpRequestProcessor(uri = "/locale", method = RequestType.POST)
public class ChangeLocaleCommandPost implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String localeLanguage = request.getParameter("localeLanguage");
        String localeRegion = request.getParameter("localeRegion");
        Locale locale = new Locale(localeLanguage, localeRegion);

        HttpSession session = request.getSession();
        session.setAttribute("locale", locale);
    }
}
