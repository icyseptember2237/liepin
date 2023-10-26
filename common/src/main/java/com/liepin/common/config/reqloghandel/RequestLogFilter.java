package com.liepin.common.config.reqloghandel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;


public class RequestLogFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestLogFilter.class);

    public RequestLogFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest originalRequest = null;
        RequestWrapper requestWrapper = null;
        boolean var14 = false;

        try {
            var14 = true;
            log.debug("=== begin request processing ===");
            originalRequest = (HttpServletRequest) request;
            requestWrapper = new RequestWrapper(originalRequest);
            this.logRequest(requestWrapper, originalRequest);
            chain.doFilter(requestWrapper, response);
            log.debug("=== end request processing ===");
            var14 = false;
        } finally {
            if (var14) {
                long elapsedTime = System.currentTimeMillis() - start;
                if (null != originalRequest && null != requestWrapper && !originalRequest.getRequestURI().contains("health")) {
                    log.info("remoteAddress={}|requestURL={}|method={}|elapsedTime={}", requestWrapper.getRemoteAddr(), requestWrapper.getRequestURL(), requestWrapper.getMethod(), elapsedTime);
                }

            }
        }

        long elapsedTime = System.currentTimeMillis() - start;
        if (null != originalRequest && null != requestWrapper && !originalRequest.getRequestURI().contains("health")) {
            log.info("remoteAddress={}|requestURL={}|method={}|elapsedTime={}", requestWrapper.getRemoteAddr(), requestWrapper.getRequestURL(), requestWrapper.getMethod(), elapsedTime);
        }

    }

    private void logRequest(RequestWrapper requestWrapper, HttpServletRequest originalRequest) throws IOException {
        String url = requestWrapper.getRequestURL().toString();
        String method = requestWrapper.getMethod();
        log.debug("requestURL={}", url);
        log.debug("method={}", method);
        this.logHeaders(originalRequest, url, method);
        this.logParameters(originalRequest, url, method);
        log.debug("remoteAddress={}", requestWrapper.getRemoteAddr());
        if (this.logBody(requestWrapper)) {
            log.info("url = {}, method= {}, body={}", url, method, requestWrapper.getOriginalBody());
        }

    }

    private void logHeaders(HttpServletRequest request, String url, String method) {
        Enumeration headers = request.getHeaderNames();

        while (headers.hasMoreElements()) {
            String headerName = (String) headers.nextElement();
            log.debug("url = {}, method= {}, [header] {}={}", url, method, headerName, request.getHeader(headerName));
        }

    }

    private void logParameters(HttpServletRequest request, String url, String method) {
        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            log.info("url = {}, method= {}, [param] {}={}", url, method, paramName, request.getParameter(paramName));
        }

    }

    private boolean logBody(RequestWrapper request) {
        String method = request.getMethod().toUpperCase();
        return ("POST".equals(method) || "PUT".equals(method)) && !request.isMultipart();
    }

    public void destroy() {
    }
}
