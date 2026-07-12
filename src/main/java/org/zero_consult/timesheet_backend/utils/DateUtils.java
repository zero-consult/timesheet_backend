package org.zero_consult.timesheet_backend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final ThreadLocal<SimpleDateFormat> dateFormat =
            ThreadLocal.withInitial(() -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf;
            });

    public static Date parseDate(String date) throws ParseException {
        return dateFormat.get().parse(date);
    }

    public static String formatDate(Date date) {
        return dateFormat.get().format(date);
    }

    public static Long toIdl(Date date) {
        return date.getTime();
    }

    public static Date fromIdl(Long date) {
        return new Date(date);
    }
}
