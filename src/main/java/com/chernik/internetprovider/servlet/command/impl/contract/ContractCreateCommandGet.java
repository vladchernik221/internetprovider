package com.chernik.internetprovider.servlet.command.impl.contract;

import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@HttpRequestProcessor(uri = "/contract/new", method = RequestType.GET)
public class ContractCreateCommandGet implements Command{
    private static final Logger LOGGER = LogManager.getLogger(ContractCreateCommandGet.class);

    private static final String CONTRACT_FORM_PAGE = "/WEB-INF/jsp/contract/contractForm.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_FORM_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CONTRACT_FORM_PAGE);
        dispatcher.forward(request, response);
    }
}
