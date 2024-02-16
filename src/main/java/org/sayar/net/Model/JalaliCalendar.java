package org.sayar.net.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JalaliCalendar {
    public static int gregorianDaysInMonth[] = { 31, 28, 31, 30, 31, 30, 31,
            31, 30, 31, 30, 31 };
    public static int jalaliDaysInMonth[] = { 31, 31, 31, 31, 31, 31, 30, 30,
            30, 30, 30, 29 };

    public final static int FARVARDIN = 0;
    public final static int ORDIBEHESHT = 1;
    public final static int KHORDAD = 2;
    public final static int TIR = 3;
    public final static int MORDAD = 4;
    public final static int SHAHRIVAR = 5;
    public final static int MEHR = 6;
    public final static int ABAN = 7;
    public final static int AZAR = 8;
    public final static int DEY = 9;
    public final static int BAHMAN = 10;
    public final static int ESFAND = 11;

    private static TimeZone timeZone = TimeZone.getDefault();
    private static boolean isTimeSeted = false;

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;






    public static String getJalaliDate(Date gDate) {

        Calendar gCal = Calendar.getInstance();
        gCal.clear();
        gCal.setTime(gDate);
        int myYear = gCal.get(Calendar.YEAR);
        int myMonth = gCal.get(Calendar.MONTH);
        int myDay = gCal.get(Calendar.DAY_OF_MONTH);
        YearMonthDate gMyDate = new YearMonthDate(myYear, myMonth, myDay);

        YearMonthDate jDate = gregorianToJalali(gMyDate);

        String jalali = jDate.year + "/" + (jDate.month + 1) + "/" + jDate.date;

        return jalali;
    }

    public static class YearMonthDate {

        public YearMonthDate(int year, int month, int date) {
            this.year = year;
            this.month = month;
            this.date = date;
        }

        private int year;
        private int month;
        private int date;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String toString() {
            return getYear() + "/" + getMonth() + "/" + getDate();
        }
    }

    public static YearMonthDate gregorianToJalali(YearMonthDate gregorian) {

        if (gregorian.getMonth() > 11 || gregorian.getMonth() < -11) {
            throw new IllegalArgumentException();
        }
        int jalaliYear;
        int jalaliMonth;
        int jalaliDay;

        int gregorianDayNo, jalaliDayNo;
        int jalaliNP;
        int i;

        gregorian.setYear(gregorian.getYear() - 1600);
        gregorian.setDate(gregorian.getDate() - 1);

        gregorianDayNo = 365 * gregorian.getYear()
                + (int) Math.floor((gregorian.getYear() + 3) / 4)
                - (int) Math.floor((gregorian.getYear() + 99) / 100)
                + (int) Math.floor((gregorian.getYear() + 399) / 400);
        for (i = 0; i < gregorian.getMonth(); ++i) {
            gregorianDayNo += gregorianDaysInMonth[i];
        }

        if (gregorian.getMonth() > 1
                && ((gregorian.getYear() % 4 == 0 && gregorian.getYear() % 100 != 0) || (gregorian
                .getYear() % 400 == 0))) {
            ++gregorianDayNo;
        }

        gregorianDayNo += gregorian.getDate();

        jalaliDayNo = gregorianDayNo - 79;

        jalaliNP = (int) Math.floor(jalaliDayNo / 12053);
        jalaliDayNo = jalaliDayNo % 12053;

        jalaliYear = 979 + 33 * jalaliNP + 4 * (int) (jalaliDayNo / 1461);
        jalaliDayNo = jalaliDayNo % 1461;

        if (jalaliDayNo >= 366) {
            jalaliYear += (int) Math.floor((jalaliDayNo - 1) / 365);
            jalaliDayNo = (jalaliDayNo - 1) % 365;
        }

        for (i = 0; i < 11 && jalaliDayNo >= jalaliDaysInMonth[i]; ++i) {
            jalaliDayNo -= jalaliDaysInMonth[i];
        }
        jalaliMonth = i;
        jalaliDay = jalaliDayNo + 1;

        return new YearMonthDate(jalaliYear, jalaliMonth, jalaliDay);
    }
}
