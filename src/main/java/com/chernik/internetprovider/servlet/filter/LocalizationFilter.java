package com.chernik.internetprovider.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class LocalizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        Locale locale = (Locale) session.getAttribute("locale");

        if (locale == null) {
            locale = request.getLocale();
            session.setAttribute("locale", locale);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
