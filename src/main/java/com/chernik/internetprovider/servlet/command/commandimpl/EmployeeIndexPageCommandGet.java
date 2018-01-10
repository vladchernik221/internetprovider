package com.chernik.internetprovider.servlet.command.commandimpl;

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

@HttpRequestProcessor(uri = "/employee", method = RequestType.GET)
public class EmployeeIndexPageCommandGet implements Command {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeIndexPageCommandGet.class);

    private static final String EMPLOYEE_INDEX_PAGE = "/WEB-INF/jsp/employee-index.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(EMPLOYEE_INDEX_PAGE);
        LOGGER.log(Level.TRACE, "Forward to page: {}", EMPLOYEE_INDEX_PAGE);
        dispatcher.forward(request, response);
    }
}
