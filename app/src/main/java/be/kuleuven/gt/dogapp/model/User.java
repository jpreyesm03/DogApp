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

    /*

        !!!!!
        in current stage this class is not needed, may be useful later

        */
    private String username;
    private String email;
    private String password;



    public User (String n, String e, String p)
    {
        username = n;
        email = e;
        password = p;


    }
    public User(Parcel in)
    {

        username = in.readString();
        email = in.readString();
        password = in.readString();

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
    }

    public Map<String, String> getPostParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("name", username);
        params.put("email", email);
        params.put("password", password);
        return params;
    }
}
