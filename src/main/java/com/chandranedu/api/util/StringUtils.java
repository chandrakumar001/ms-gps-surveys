package com.chandranedu.api.util;

import java.util.Objects;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("StringUtils class");
    }

    public static boolean isStringNullOrBlank(final String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}