package com.chernik.internetprovider.servlet.command.commandimpl.tariffplan;

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

@HttpRequestProcessor(uri = "/tariff-plan/new", method = RequestType.GET)
public class TariffPlanCreateCommandGet implements Command{
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanCreateCommandGet.class);

    private static final String TARIFF_FORM_PAGE = "/WEB-INF/jsp/tariffForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TARIFF_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
