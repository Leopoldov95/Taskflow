package com.taskflow.taskflow.utils;



import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;

public class ExceptionUtils {

    public static String buildEnumErrorMessage(InvalidFormatException ex) {

        Class<?> targetType = ex.getTargetType();

        String invalidValue = String.valueOf(ex.getValue());

        Object[] acceptedValues = targetType.getEnumConstants();

        return String.format(
                "Invalid value '%s' for enum %s. Accepted values are: %s",
                invalidValue,
                targetType.getSimpleName(),
                Arrays.toString(acceptedValues)
        );
    }
}