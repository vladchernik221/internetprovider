package com.chernik.internetprovider.servlet.command.commandimpl.annex;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/{\\d+}/annex/new", method = RequestType.GET)
public class AnnexCreateCommandGet implements Command{
    private static final Logger LOGGER = LogManager.getLogger(AnnexCreateCommandGet.class);

    private static final String ANNEX_FORM_PAGE = "/WEB-INF/jsp/annex/annexForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(ANNEX_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", ANNEX_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
