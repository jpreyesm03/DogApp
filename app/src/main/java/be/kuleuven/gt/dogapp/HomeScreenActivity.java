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
    private String id;
    private Button btnAdd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = (User) getIntent().getParcelableExtra("user");
        txtInfo = (TextView) findViewById(R.id.currentUser);
        btnAdd1 = findViewById(R.id.addDogs);
        btnAdd1.setClickable(false);
        String info = null;
        if (user != null) {
            txtInfo.setText("Username: " + user.getUsername());
            findUserID(user.getUsername(), user.getEmail(), user.getPassword());
            Intent intent = new Intent(this,MyDogsActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        } else {
            txtInfo.setText("User unknown");
        }

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    private void findUserID(String username,String email,String password)
    {
        String baseUrl = "https://studev.groept.be/api/a23PT106/find_user";
        String urlCreate = baseUrl + "/" + username + "/" + email + "/" + password;
        id = "";


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean match = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                id = o.getString("idUser");
                                btnAdd1.setClickable(true);
                                user.setIdUser(id);
                                saveUserInformation(user.getUsername(),user.getEmail(),user.getPassword(),user.getIdUser());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(HomeScreenActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);

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