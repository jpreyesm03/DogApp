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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import be.kuleuven.gt.dogapp.model.CaesarCipher;
import be.kuleuven.gt.dogapp.model.EnhancedEncryption;

public class CreateAccountActivity extends AppCompatActivity {

    private EnhancedEncryption encryptor;

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
    }


    public void onBtnCreateAcc_Clicked(View Caller) {
        EditText email = (EditText) findViewById(R.id.emailCreate);
        EditText username = (EditText) findViewById(R.id.usernameCreate);
        EditText password1 = (EditText) findViewById(R.id.password1);
        EditText password2 = (EditText) findViewById(R.id.password2);

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