package net.cowtopia.dscjava.libs;

import java.util.concurrent.TimeUnit;

public class TimeToString {
    // private long _years;
    // private long _months;
    private long _days;
    private long _hours;
    private long _minutes;
    private long _seconds;
    private long _milliseconds;

    // koliko milisekundi ima u kom vremenskom periodu
    private static final long m_second = 1000L;
    private static final long m_minute = 60 * m_second;
    private static final long m_hour = 24 * m_minute;
    private static final long m_day = 7 * m_hour;
    // private static final long m_month = 30 * m_day;
    // private static final long m_year = 12 * m_day;

    // konstruktoru saljemo milisekunde
    public TimeToString(long milliseconds) {
        // miliToYears(milliseconds);
        // miliToMonths(milliseconds);
        miliToDays(milliseconds);
        miliToHours(milliseconds);
        miliToMinutes(milliseconds);
        miliToSeconds(milliseconds);
        miliToMilliseconds(milliseconds);
    }

    @Override
    public String toString() {
        return valuecomma(_days, TimeUnit.DAYS)
                + valuecomma(_hours, TimeUnit.HOURS)
                + valuecomma(_minutes, TimeUnit.MINUTES)
                + Long.toString(_seconds) + " seconds and "
                + Long.toString(_milliseconds) + " milliseconds";
    }

    private String valuecomma(long value, TimeUnit t) {
        if(value > 0) {
            return Long.toString(value) + " " + timeUnitToString(t) + ", ";
        }
        else return "";
    }

    private String timeUnitToString(TimeUnit t) {
        if(t == TimeUnit.DAYS) {
            return "days";
        }
        else if(t == TimeUnit.HOURS) {
            return "hours";
        }
        else if(t == TimeUnit.MINUTES) {
            return "minutes";
        }
        else if(t == TimeUnit.SECONDS) {
            return "seconds";
        }
        else if(t == TimeUnit.MILLISECONDS) {
            return "milliseconds";
        }
        else return "";
    }

    /*private void miliToYears(long milliseconds) {
        _years = milliseconds / m_year;
    }

    private void miliToMonths(long milliseconds) {
        _months = milliseconds % m_year / m_month;
    }

    //private void miliToDays(long milliseconds) {
    //    _days = milliseconds % m_month / m_day;
    //}*/

    private void miliToDays(long milliseconds) {
        _days = milliseconds / m_day;
    }

    private void miliToHours(long milliseconds) {
        _hours = milliseconds % m_day / m_hour;
    }

    private void miliToMinutes(long milliseconds) {
        _minutes = milliseconds % m_hour / m_minute;
    }

    private void miliToSeconds(long milliseconds) {
        _seconds = milliseconds % m_minute / m_second;
    }

    private void miliToMilliseconds(long milliseconds) {
        _milliseconds = milliseconds % m_second;
    }
}
