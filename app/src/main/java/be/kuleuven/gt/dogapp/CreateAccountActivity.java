package be.kuleuven.gt.dogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onBtnCreateAcc_Clicked(View Caller) {
        EditText email = (EditText) findViewById(R.id.emailCreate);
        EditText username = (EditText) findViewById(R.id.usernameCreate);
        EditText password1 = (EditText) findViewById(R.id.passwordCreate1);
        EditText password2 = (EditText) findViewById(R.id.passwordCreate2);

        String e = email.getText().toString();
        String u = username.getText().toString();
        String p1 = password1.getText().toString();
        String p2 = password2.getText().toString();
        String urlCreate = "https://studev.groept.be/api/a23PT106/create_account/";



        if(!(e.isEmpty()) && !(u.isEmpty())&& !(p1.isEmpty()) && !(p2.isEmpty()) ) {

            if (!password1.getText().toString().equals(password2.getText().toString())) {
                Toast.makeText(this, "The passwords do not match! Please retype.", Toast.LENGTH_SHORT).show();
            } else {

                //User user = new User(u,e,p1);
                ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                )

                { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                    @Override
                    protected Map<String, String> getParams() {
                    /* Map<String, String> with key value pairs as data load */
                        Map<String, String> params = new HashMap<>();
                        params.put("name", u);
                        params.put("email", e);
                        params.put("password", p1);
                        return params;
                }
                };


                requestQueue.add(submitRequest);






            }
        }
        else {
            Toast.makeText(this, "Please fill all necessary info!", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}