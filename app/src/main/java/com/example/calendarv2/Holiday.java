package com.example.calendarv2;

public class Holiday {

    private String day;
    private String month;
    private String year;
    private String dayOfWeek;
    private String isHolidayRed;
    private String holidayName;
    private String isHolidayImage;
    private String isFasting;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsHolidayRed() {
        return isHolidayRed;
    }

    public void setIsHolidayRed(String isHolidayRed) {
        this.isHolidayRed = isHolidayRed;
    }

    public String getIsHolidayImage() {
        return isHolidayImage;
    }

    public void setIsHolidayImage(String isHolidayImage) {
        this.isHolidayImage = isHolidayImage;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getIsFasting() {
        return isFasting;
    }

    public void setIsFasting(String isFasting) {
        this.isFasting = isFasting;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", isHolidayRed='" + isHolidayRed + '\'' +
                ", isHolidayImage='" + isHolidayImage + '\'' +
                ", holidayName='" + holidayName + '\'' +
                ", isFasting='" + isFasting + '\'' +
                '}';
    }
}

