package com.astoppello.incomebalanceapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by @author stopp on 19/12/2021
 */
@Component
public class RestFilter implements Filter {
   private final static Logger logger = LoggerFactory.getLogger(RestFilter.class);


    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        final String uri = req.getRequestURI();
        String qs = req.getQueryString();
        logger.info("{} {}{}", req.getMethod(), uri, qs == null || qs.length() ==0 ? "" : "?"+qs);

        chain.doFilter(request, response);

        HttpServletResponse resp = (HttpServletResponse) response;
        if (HttpStatus
                .valueOf(resp.getStatus()).isError()) {
            logger.error("{}:  {} {}", resp.getStatus(), req.getMethod(), uri);
        }
    }

    @Override
    public void destroy() {}
}
