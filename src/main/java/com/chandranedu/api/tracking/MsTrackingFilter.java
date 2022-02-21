package com.chandranedu.api.tracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MsTrackingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsTrackingFilter.class);

    private final MsRequestContext msRequestContext;

    public MsTrackingFilter(final MsRequestContext msRequestContext) {
        this.msRequestContext = msRequestContext;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        LOGGER.trace("Tracking filter Start");
        this.msRequestContext.init(httpServletRequest, httpServletResponse);
        LOGGER.trace("Tracking filter End");

        // Continue Processing
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}