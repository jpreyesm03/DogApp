package be.kuleuven.gt.dogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class MainActivity extends AppCompatActivity {

    private boolean match;
    private String nameMatch;
    private String emailMatch;
    private String passwordMatch;

    private User user;

    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        boolean isLoggedIn = checkIfUserIsLoggedIn();

        if (isLoggedIn) {
            // If user is logged in, retrieve user information and navigate to HomeScreenActivity
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String username = preferences.getString("username", null);
            String email = preferences.getString("email", null);
            String password = preferences.getString("password", null);
            String id = preferences.getString("id", null);

            user = new User(username, email, password);
            user.setIdUser(id);

            Intent intent = new Intent(this, HomeScreenActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it when pressing back in HomeScreenActivity
        }
    }


    private boolean checkIfUserIsLoggedIn() {
        // Check SharedPreferences for stored user information (cookie)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String id = preferences.getString("id", null);

        // Return true if user information is found (i.e., user is logged in)
        return username != null && email != null && id != null;
    }

    public void onBtnCreateAcc_Clicked(View Caller) {


        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);

    }

    public void onBtnLogin_Clicked(View Caller) {
        requestLogin();
    }



    private void requestLogin() {
        EditText emailOrUser = findViewById(R.id.emailLogin);
        EditText passwordProvided = findViewById(R.id.password);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging in, please wait...");
        progressDialog.show();
        String e = emailOrUser.getText().toString();
        String p1 = passwordProvided.getText().toString();


        String baseUrl = "https://studev.groept.be/api/a23PT106/login";
        String urlCreate = baseUrl + "/" + e + "/" + e + "/" + p1;



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
                                String name = o.getString("name");
                                String email = o.getString("email");
                                String password = o.getString("password");

                                if ((name.equals(e) || email.equals(e)) && (password.equals(p1))) {
                                    match = true;
                                    nameMatch = name;
                                    emailMatch = email;
                                    passwordMatch = password;
                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                        if (match) {
                            user = new User(nameMatch,emailMatch,passwordMatch);
                            Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                            intent.putExtra("user",user);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Login successful! Taking you to main screen!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Login unsuccessful. Please try signing in again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }





}