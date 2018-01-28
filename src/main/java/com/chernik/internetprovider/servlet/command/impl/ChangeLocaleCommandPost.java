package com.chernik.internetprovider.servlet.command.impl;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.HttpRequestProcessor;
import com.chernik.internetprovider.exception.BaseException;
import com.chernik.internetprovider.servlet.command.Command;
import com.chernik.internetprovider.servlet.command.RequestType;
import com.chernik.internetprovider.servlet.mapper.BaseMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@HttpRequestProcessor(uri = "/locale", method = RequestType.POST)
public class ChangeLocaleCommandPost implements Command {
    @Autowired
    private BaseMapper baseMapper;

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BaseException {
        String localeLanguage = baseMapper.getMandatoryString(request, "localeLanguage");
        String localeRegion = baseMapper.getMandatoryString(request, "localeRegion");
        Locale locale = new Locale(localeLanguage, localeRegion);

        HttpSession session = request.getSession();
        session.setAttribute("locale", locale);
    }
}
