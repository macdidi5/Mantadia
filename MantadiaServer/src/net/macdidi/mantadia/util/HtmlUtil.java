package net.macdidi.mantadia.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * HTML公用類別
 * 
 * @author macdidi
 */
public class HtmlUtil {

    /**
     * 西元年選單
     * 
     * @param name HTML表單元件名稱
     * @param year 年
     * @return 選單HTML內容
     */
    public static String getYearSelect(String name, int year) {
        StringBuffer temp = new StringBuffer();
        temp.append("<select name='").append(name).append("'>");
        int start = year - 10;
        
        for (int i = 0; i < 10; i++) {
            temp.append("<option value='")
                .append(start + i)
                .append("'>")
                .append(start + i)
                .append("</option>");
        }
        
        temp.append("<option value='")
            .append(year)
            .append("' selected>")
            .append(year)
            .append("</option>");
        start = year + 1;
        
        for (int i = 0; i < 3; i++) {
            temp.append("<option value='")
                .append(start + i)
                .append("'>")
                .append(start + i)
                .append("</option>");
        }
        
        temp.append("</select>");
        return temp.toString();
    }

    /**
     * 月份選單
     * 
     * @param name HTML表單元件名稱
     * @param month 月
     * @return 選單HTML內容
     */
    public static String getMonthSelect(String name, int month) {
        StringBuffer temp = new StringBuffer();
        temp.append("<select name='").append(name).append("'>");

        for (int i = 1; i <= 12; i++) {
            temp.append("<option value='").append(i).append("' ");

            if (i == month) {
                temp.append("selected");
            }

            temp.append(">").append(i).append("</option>");
        }
        
        temp.append("</select>");
        return temp.toString();
    }

    /**
     * 日期選單
     * 
     * @param name HTML表單元件名稱
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 選單HTML內容
     */
    public static String getDaySelect(String name, int year, int month,
            int day) {
        StringBuffer temp = new StringBuffer();
        temp.append("<select name='").append(name).append("'>");
        int days = getDays(year, month);
        
        for (int i = 1; i <= days; i++) {
            temp.append("<option value='").append(i).append("' ");

            if (i == day) {
                temp.append("selected");
            }

            temp.append(">").append(i).append("</option>");
        }
        
        temp.append("</select>");
        return temp.toString();
    }

    /**
     * 取得天數
     * 
     * @param year 年
     * @param month 月
     * @return 天數
     */
    public static int getDays(int year, int month) {
        int result = new GregorianCalendar().isLeapYear(year) ? 29 : 28;

        switch (month) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            result = 31;
        case 4:
        case 6:
        case 9:
        case 11:
            result = 30;
        }

        return result;
    }
    
    /**
     * 目前日期選單
     * 
     * @return 選單HTML內容
     */
    public static String getDateNowSelect() {
        // 取得目前日期物件
        Calendar now = new GregorianCalendar();
        // 取得目前年、月、日
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        
        return getDateSelect(year, month, day);
    }
    
    /**
     * 指定日期選單
     * 
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 選單HTML內容
     */
    public static String getDateSelect(int year, int month, int day) {
        StringBuffer temp = new StringBuffer();
        temp.append(getYearSelect("year", year))
            .append(" - ")
            .append(getMonthSelect("month", month))
            .append(" - ")
            .append(getDaySelect("day", year, month, day));
        return temp.toString();
    }

}
