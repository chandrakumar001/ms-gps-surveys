package com.chandranedu.api.logging;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MsMdcCleanUpFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest,
                                    final HttpServletResponse httpServletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {

        MDC.clear();
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        MDC.clear();
    }
}