package com.cvsnewsletter.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CvsUtility {

    public static boolean isValidOhrId(String ohrId) {
        return ohrId != null && ohrId.matches("\\d{9}");
    }

}
