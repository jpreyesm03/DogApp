package be.kuleuven.gt.dogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
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

import be.kuleuven.gt.dogapp.model.CaesarCipher;
import be.kuleuven.gt.dogapp.model.EnhancedEncryption;
import be.kuleuven.gt.dogapp.model.User;

public class MainActivity extends AppCompatActivity {

    private String nameMatch;
    private String emailMatch;
    private String passwordMatch;

    private boolean hasdogs;

    private User user;

    private String id;

    private EnhancedEncryption encryptor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        encryptor = new EnhancedEncryption();
        boolean isLoggedIn = checkIfUserIsLoggedIn();

        if (isLoggedIn) {
            // If user is logged in, retrieve user information and navigate to HomeScreenActivity
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String username = preferences.getString("username", null);
            String email = preferences.getString("email", null);
            String encryptedPassword = preferences.getString("password", null); // Retrieve encrypted password
            String id = preferences.getString("id", null);

            // Decrypt the stored password
            String password = CaesarCipher.decrypt(encryptedPassword);

            user = new User(username, email, password);
            user.setIdUser(id);
            checkIfUserHasDogs();
            if (hasdogs) {
                goMyDogs();
            } else {
                goHomeScreen();
            }
        }
    }


    private void findUserID(String username, String email, String password) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/find_user";
        String encryptedPassword = CaesarCipher.encrypt(password); // Encrypt the password

        String urlCreate = baseUrl + "/" + username + "/" + email + "/" + encryptedPassword;
        id = "";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                id = o.getString("idUser");
                                user.setIdUser(id);
                                saveUserInformation(user.getUsername(), user.getEmail(), password, user.getIdUser()); // Save the unencrypted password
                                checkIfUserHasDogs();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }
    private void checkIfUserHasDogs() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/checkUserDogs";
        String url = baseUrl + "/" + user.getIdUser();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // If the user has dogs, start MyDogsActivity
                        hasdogs = response.length() > 0;

                        if (hasdogs){
                            goMyDogs();
                        }
                        else {
                            goHomeScreen();
                        }




                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);

    }

    private void goMyDogs()
    {
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish(); // Fi

    }






    private void goHomeScreen() {

        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("hasdogs",hasdogs);
        startActivity(intent);
        finish(); // Finish the MainActivity to prevent going back to it when pressing back in HomeScreenActivity
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

    public void onBtnForgotPassword_Clicked(View Caller)
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);

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
        String urlCreate = baseUrl + "/" + e + "/" + e ;



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
                                String pasDecrypted = encryptor.decrypt(password);

                                if (p1.equals(pasDecrypted))
                                    if (name.equals(e) || email.equals(e)) {
                                        match = true;
                                        nameMatch = name;
                                        emailMatch = email;
                                        passwordMatch = pasDecrypted;
                                        break;
                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                        if (match) {
                            user = new User(nameMatch,emailMatch,passwordMatch);
                            findUserID(nameMatch,emailMatch,passwordMatch);

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