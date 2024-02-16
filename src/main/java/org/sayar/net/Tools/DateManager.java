package org.sayar.net.Tools;

import java.util.Date;

public class DateManager {

    private static final long HOURS_IN_MILLI= 60 * 60 *1000;
    private static final long DAYS_IN_MILLI= 60 * 60 *1000* 24;

    public static Date convertPersianDateToJavaDate(String persianDate){
        persianDate = convertPersianNumberCharToEnglish(persianDate);
        int indexFirstDot=persianDate.indexOf("/");
        int indexLastDot=persianDate.lastIndexOf("/");
        int indexLastDash=persianDate.lastIndexOf("-");
        int indexLastTowDot=persianDate.lastIndexOf(":");

        int year=Integer.parseInt(persianDate.substring(0,indexFirstDot));
        int month=Integer.parseInt(persianDate.substring(indexFirstDot+1,indexLastDot));
        int day=Integer.parseInt(persianDate.substring(indexLastDot+1,indexLastDash));
        int hour=Integer.parseInt(persianDate.substring(indexLastDash+1,indexLastTowDot));
        int minute=Integer.parseInt(persianDate.substring(indexLastTowDot+1,persianDate.length()));
        return PersianCalendar.getGregorainCalendar(year,month,day,hour,minute,0).getTime();
    }

    public boolean isAfter(String persianDate1,String persianDate2){

        Date date1= convertPersianDateToJavaDate(persianDate1);
        Date date2=convertPersianDateToJavaDate(persianDate2);

        return date1.after(date2);
    }

    public boolean isBefore(String persianDate1,String persianDate2){
        Date date1= convertPersianDateToJavaDate(persianDate1);
        Date date2=convertPersianDateToJavaDate(persianDate2);
        return date1.before(date2);
    }

    public static String convertJavaDateToPersianDate(Date date){

        return convertEnglishNumberCharToPersian(
                PersianCalendar.getPersianDate(date)+"-"+date.getHours()+":"+date.getMinutes()
        );
    }

    private static String convertEnglishNumberCharToPersian(String input){

        String result="";


        for (int i = 0; i <input.length() ; i++) {

            switch (input.charAt(i)){
                case '0':{
                    result+="۰";
                    break;
                }

                case '1':{
                    result+="۱";
                    break;
                }
                case '2':{
                    result+="۲";
                    break;
                }
                case '3':{
                    result+="۳";
                    break;
                }
                case '4':{
                    result+="۴";
                    break;
                }
                case '5':{
                    result+="۵";
                    break;
                }
                case '6':{
                    result+="۶";
                    break;
                }
                case '7':{
                    result+="۷";
                    break;
                }
                case '8':{
                    result+="۸";
                    break;
                }
                case '9':{
                    result+="۹";
                    break;
                }
                case '/':{
                    result+="/";
                    break;
                }

                case '-':{
                    result+="-";
                    break;
                }

                case ':':{
                    result+=":";
                    break;
                }
            }


        }

        return result;

    }

    private static String convertPersianNumberCharToEnglish(String input){

        String result="";


        for (int i = 0; i <input.length() ; i++) {

            switch (input.charAt(i)){
                case '۰':{
                    result+="0";
                    break;
                }

                case '۱':{
                    result+="1";
                    break;
                }
                case '۲':{
                    result+="2";
                    break;
                }
                case '۳':{
                    result+="3";
                    break;
                }
                case '۴':{
                    result+="4";
                    break;
                }
                case '۵':{
                    result+="5";
                    break;
                }
                case '۶':{
                    result+="6";
                    break;
                }
                case '۷':{
                    result+="7";
                    break;
                }
                case '۸':{
                    result+="8";
                    break;
                }
                case '۹':{
                    result+="9";
                    break;
                }
                case '/':{
                    result+="/";
                    break;
                }

                case '-':{
                    result+="-";
                    break;
                }

                case ':':{
                    result+=":";
                    break;
                }
            }


        }

        return result;

    }

    public static long calculateDiffrenceBetweenDataInHour(Date startDate,Date endDate){
       long dif=endDate.getTime()-  startDate.getTime();
       return dif / HOURS_IN_MILLI;
    }

    public static long calculateDiffrenceBetweenDataInDay(Date startDate,Date endDate){
        long dif=endDate.getTime()-  startDate.getTime();
        return dif / DAYS_IN_MILLI;
    }

}
