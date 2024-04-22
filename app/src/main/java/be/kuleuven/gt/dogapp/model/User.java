package be.kuleuven.gt.dogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    private String username;
    private String email;
    private String password;
    private String idUser;
    private boolean dogwalker;

    public boolean isDogwalker() {
        return dogwalker;
    }

    public void setDogwalker(boolean dogwalker) {
        this.dogwalker = dogwalker;
    }

    public User (String n, String e, String p)
    {
        username = n;
        email = e;
        password = p;
        idUser = "";

    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser()
    {
        return idUser;
    }


    public User(Parcel in)
    {
        username = in.readString();
        email = in.readString();
        password = in.readString();
        idUser = in.readString();

    }

    public User(JSONObject o) {
        try {
            username = o.getString("name");
            email = o.getString("email");
            password = o.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(idUser);
    }


}
