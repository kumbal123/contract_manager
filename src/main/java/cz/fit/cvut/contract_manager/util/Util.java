package cz.fit.cvut.contract_manager.util;

import java.util.Calendar;
import java.util.Date;

public class Util {

    private static Calendar cal = Calendar.getInstance();
    public static String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};

    public static int getYear(final Date date) {
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(final Date date) {
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

}
