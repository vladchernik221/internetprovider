package com.chernik.internetprovider.servlet.command.commandimpl.contract;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
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

@HttpRequestProcessor(uri = "/contract", method = RequestType.GET)
public class ContractListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ContractListCommandGet.class);

    private static final String CONTRACT_LIST_PAGE = "/WEB-INF/jsp/contract/contractList.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, BaseException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(CONTRACT_LIST_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", CONTRACT_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
