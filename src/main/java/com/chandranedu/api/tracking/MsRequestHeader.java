package com.chandranedu.api.tracking;

public enum MsRequestHeader {

    REQUEST_ID("X-Request-ID"),
    BUSINESS_ID("X-BusinessTx-ID");

    private final String headerName;

    MsRequestHeader(final String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}