package com.chernik.internetprovider.servlet.command.commandimpl.tariffplan;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.entity.TariffPlan;
import com.chernik.internetprovider.service.TariffPlanService;
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

@HttpRequestProcessor(uri = "/tariff-plan", method = RequestType.GET)
public class TariffPlanListCommandGet implements Command {
    private static final Logger LOGGER = LogManager.getLogger(TariffPlanListCommandGet.class);

    private static final String TARIFF_LIST_PAGE = "/WEB-INF/jsp/tariff/tariffList.jsp";

    @Autowired
    private TariffPlanService tariffPlanService;

    @Autowired
    private BaseMapper baseMapper;

    public void setTariffPlanService(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, BaseException, IOException {
        Integer pageNumber = baseMapper.getNotMandatoryInt(request, "page");
        pageNumber = (pageNumber != null) ? pageNumber - 1 : 0;

        Boolean archived = baseMapper.getNotMandatoryBoolean(request, "archived");
        archived = (archived != null) ? archived : false;

        RequestDispatcher dispatcher = request.getRequestDispatcher(TARIFF_LIST_PAGE);
        Page<TariffPlan> tariffPlansPage = tariffPlanService.getPage(new Pageable(pageNumber, 10), archived);
        request.setAttribute("tariffPlansPage", tariffPlansPage);
        LOGGER.log(Level.TRACE, "Forward to page: {}", TARIFF_LIST_PAGE);
        dispatcher.forward(request, response);
    }
}
