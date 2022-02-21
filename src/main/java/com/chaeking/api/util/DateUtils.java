package com.chaeking.api.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static DateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat DATE_yyMMdd = new SimpleDateFormat("yyMMdd");
    public static DateFormat DATE_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    public static Date stringToDate(String s) {
        if(StringUtils.isBlank(s))
            return null;
        try {
            s = s.replaceAll("[^0-9]", "");
            if(s.length() < 6)
                return null;
            if(s.length() == 8)
                return DATE_yyyyMMdd.parse(s);
            return DATE_yyMMdd.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
