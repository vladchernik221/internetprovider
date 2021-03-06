package com.chernik.internetprovider.servlet.command.impl.service;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.Service;
import com.chernik.internetprovider.service.ServiceService;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@HttpRequestProcessor(uri = "/contract/annex/{\\d+}/service/order", method = RequestType.GET)
public class ServiceOrderCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ServiceOrderCommandGet.class);

    private static final String SERVICE_ANNEX_LIST_PAGE = "/WEB-INF/jsp/service/serviceAnnexList.jsp";

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BaseMapper baseMapper;

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, BaseException, IOException {
        Integer pageNumber = baseMapper.getNotMandatoryInt(request, "page");
        pageNumber = (pageNumber != null) ? pageNumber - 1 : 0;

        RequestDispatcher dispatcher = request.getRequestDispatcher(SERVICE_ANNEX_LIST_PAGE);
        Page<Service> servicesPage = serviceService.getPage(new Pageable(pageNumber, 10), false);
        request.setAttribute("servicesPage", servicesPage);
        String annexId = request.getRequestURI().split("/")[3];
        request.setAttribute("annexId", annexId);

        LOGGER.log(Level.TRACE, "Forward to page: {}", SERVICE_ANNEX_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
