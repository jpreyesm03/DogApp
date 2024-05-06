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

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText key;
    private EditText newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        key = findViewById(R.id.forgotKey);
        newPass = findViewById(R.id.passwordProvided);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onBtnSubmitRequest_Clicked(View Caller) {
        String keyProvided = key.getText().toString();
        String newPassword = newPass.getText().toString();

        String baseUrl = "https://studev.groept.be/api/a23PT106/forgotPassword";
        String urlCreate = baseUrl + "/" + newPassword + "/" + keyProvided ;


        ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        Intent intent = new Intent(this, MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("If you provided the correct key, your password was changed")
                .setCancelable(false)
                .setPositiveButton("Dismiss", (dialog, id) -> {
                    // Dismiss dialog and submit the request
                    dialog.dismiss();
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasswordActivity.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    startActivity(intent);


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            "Something went wrong [Breedable]..." + error,
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                    ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                        @Override
                        protected Map<String, String> getParams() {
                            /* Map<String, String> with key value pairs as data load */
                            Map<String, String> params = new HashMap<>();
                            params.put("newpass", newPassword);
                            params.put("forgot", keyProvided);


                            return params;
                        }
                    };
                    requestQueue.add(submitRequest);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}