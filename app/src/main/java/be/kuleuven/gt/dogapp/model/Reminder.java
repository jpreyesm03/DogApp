package be.kuleuven.gt.dogapp.model;

public class Reminder {

    private String details;
    private String date;
    private String month;
    private String year;
    private String time;

    public Reminder(String details, String date, String month, String year, String time) {

        this.details = details;
        this.date = date;
        this.month = month;
        this.year = year;
        this.time = time;
    }



    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
