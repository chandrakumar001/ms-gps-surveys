package com.chandranedu.api.tracking;

import com.chandranedu.api.util.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class MsRequestContext {

    private boolean isInitialised;
    private String requestId;
    private String businessTxId;

    private String requestUri;
    private String verb;

    public void init(final HttpServletRequest request,
                     final HttpServletResponse response) {
        if (isInitialised) {
            return;
        }
        this.isInitialised = true;
        this.requestId = getRequestId(request, response);
        this.businessTxId = getBusinessTxId(request, response);
        this.requestUri = request.getRequestURI();
        this.verb = request.getMethod();
    }

    public boolean isInitialised() {
        return isInitialised;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getVerb() {
        return verb;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getBusinessTxId() {
        return businessTxId;
    }

    private String getBusinessTxId(final HttpServletRequest httpServletRequest,
                                   final HttpServletResponse httpServletResponse) {
        return defineHeader(httpServletRequest, httpServletResponse,
                MsRequestHeader.BUSINESS_ID, UUID.randomUUID().toString());
    }

    private String getRequestId(final HttpServletRequest httpServletRequest,
                                final HttpServletResponse httpServletResponse) {

        return defineHeader(httpServletRequest, httpServletResponse,
                MsRequestHeader.REQUEST_ID, UUID.randomUUID().toString());
    }

    private static String defineHeader(final HttpServletRequest httpServletRequest,
                                       final HttpServletResponse httpServletResponse,
                                       final MsRequestHeader msRequestHeader,
                                       final String defaultValue) {

        final String headerName = msRequestHeader.getHeaderName();
        final String requestValue = httpServletRequest.getHeader(headerName);
        final String resultingValue = getString(defaultValue, requestValue);

        if (resultingValue != null) {
            MDC.put(headerName, resultingValue);
            httpServletResponse.setHeader(headerName, resultingValue);
        }
        return resultingValue;
    }

    private static String getString(final String defaultValue,
                                    final String requestValue) {
        return StringUtils.isStringNullOrBlank(requestValue) ? defaultValue : requestValue;
    }
}