package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.gt.dogapp.model.User;

public class HomeScreenActivity extends AppCompatActivity {

    private TextView txtInfo;
    private User user;
    private boolean hasdogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = (User) getIntent().getParcelableExtra("user");
        hasdogs = getIntent().getBooleanExtra("hasdogs",false);
        txtInfo = (TextView) findViewById(R.id.btnChangeProfileInfo);
        txtInfo.setText("Username: " + user.getUsername());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });








    }









    public void onBtnAddDogs_Clicked(View Caller)
    {
        Intent intent = new Intent(this,AddDogActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public void onBtnSeeMyDogs_Clicked(View Caller)
    {
        Intent intent = new Intent(this,MyDogsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public void onBtnLogout_Clicked(View Caller)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUserInformation(String name, String email, String password, String id) {
        // Get SharedPreferences editor
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Store user information
        editor.putString("username", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("id", id); // Save user ID

        // Commit changes
        editor.apply();
    }
}