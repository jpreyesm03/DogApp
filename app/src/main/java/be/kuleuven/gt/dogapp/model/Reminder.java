package be.kuleuven.gt.dogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Reminder implements Parcelable {

    private String details;
    private String date;
    private String month;
    private String year;
    private String time;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reminder(String details, String date, String month, String year, String time, String id) {

        this.details = details;
        this.date = date;
        this.month = month;
        this.year = year;
        this.time = time;
        this.id = id;
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


    public Reminder(Parcel in)
    {
        details = in.readString();
        date = in.readString();
        month = in.readString();
        year = in.readString();
        time = in.readString();
        id = in.readString();

    }

    public Reminder(JSONObject o) {
        try {
            details = o.getString("details");
            date = o.getString("date");
            month = o.getString("month");
            year = o.getString("year");
            time = o.getString("time");
            id = o.getString("idReminder");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {

        parcel.writeString(details);
        parcel.writeString(date);
        parcel.writeString(month);
        parcel.writeString(year);
        parcel.writeString(time);
        parcel.writeString(id);

    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
