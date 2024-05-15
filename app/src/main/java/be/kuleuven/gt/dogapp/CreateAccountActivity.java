package be.kuleuven.gt.dogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import be.kuleuven.gt.dogapp.model.CaesarCipher;
import be.kuleuven.gt.dogapp.model.EnhancedEncryption;

public class CreateAccountActivity extends AppCompatActivity {

    private EnhancedEncryption encryptor;
    private EditText email;
    private EditText username;
    private EditText password1;
    private EditText password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        encryptor = new EnhancedEncryption();
        email = (EditText) findViewById(R.id.emailCreate);
        username = (EditText) findViewById(R.id.usernameCreate);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
    }
    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }


    public void onBtnCreateAcc_Clicked(View Caller) {
        checkIfUserExists();




    }

    private void checkIfUserExists() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/checkIfUserExists";
        String url = baseUrl + "/" + username.getText().toString() + "/" + email.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // If the user has dogs, start MyDogsActivity


                        if (response.length() == 0){
                            createAccount();
                        }
                        else {
                            Toast.makeText(CreateAccountActivity.this, "A user with this username or email already exists. ", Toast.LENGTH_SHORT).show();
                        }




                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateAccountActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void createAccount()
    {


        String e = email.getText().toString();
        String u = username.getText().toString();
        String p1 = password1.getText().toString();
        String p2 = password2.getText().toString();
        String urlCreate = "https://studev.groept.be/api/a23PT106/create_account/";

        if(!(e.isEmpty()) && !(u.isEmpty()) && !(p1.isEmpty()) && !(p2.isEmpty()) ) {
            if (!password1.getText().toString().equals(password2.getText().toString())) {
                Toast.makeText(this, "The passwords do not match! Please retype.", Toast.LENGTH_SHORT).show();
            } else {
                // Generate random int for forgot password
                int randomInt = generateRandomInt();
                String in1 = Integer.toString(randomInt);
                String pas1 = encryptor.encrypt(p1);

                // Display random int in a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your random int for forgot password: " + randomInt)
                        .setCancelable(false)
                        .setPositiveButton("I have stored this key", (dialog, id) -> {
                            // Dismiss dialog and submit the request
                            dialog.dismiss();

                            // Create request
                            ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this);
                            progressDialog.setMessage("Uploading, please wait...");
                            progressDialog.show();
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);

                            RequestQueue requestQueue = Volley.newRequestQueue(CreateAccountActivity.this);
                            StringRequest submitRequest = new StringRequest(
                                    Request.Method.POST,
                                    urlCreate,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog.dismiss();
                                            Toast.makeText(
                                                    CreateAccountActivity.this,
                                                    "Account created! Back to the loading screen!",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(
                                                    CreateAccountActivity.this,
                                                    "Unable to create account: " + error,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("name", u);
                                    params.put("email", e);
                                    params.put("password", pas1);
                                    params.put("forgot", in1);
                                    return params;
                                }
                            };
                            requestQueue.add(submitRequest);
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            Toast.makeText(this, "Please fill all necessary info!", Toast.LENGTH_SHORT).show();
        }



    }

    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(10000); // Modify range as needed
    }
}