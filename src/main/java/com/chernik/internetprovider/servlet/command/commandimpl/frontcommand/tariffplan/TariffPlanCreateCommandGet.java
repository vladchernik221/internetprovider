package com.chernik.internetprovider.servlet.command.commandimpl.frontcommand.tariffplan;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.HttpRequestType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/tariff_plan/new", method = HttpRequestType.GET)
public class TariffPlanCreateCommandGet implements Command{
    private final static Logger LOGGER = LogManager.getLogger(TariffPlanCreateCommandGet.class);

    private final static String TARIFF_FORM_PAGE = "/WEB-INF/jsp/tariffForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TARIFF_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
