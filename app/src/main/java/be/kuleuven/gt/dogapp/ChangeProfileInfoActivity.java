package be.kuleuven.gt.dogapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.CaesarCipher;
import be.kuleuven.gt.dogapp.model.EnhancedEncryption;
import be.kuleuven.gt.dogapp.model.User;

public class ChangeProfileInfoActivity extends AppCompatActivity {
    private Button btnBack;
    private Button btnSubmit;
    private Button btnDeleteUser;
    private TextView txtChangeNameProfile;
    private TextView txtChangeEmail;
    private TextView txtCurrentPassword;
    private TextView txtNewPassword;
    private User user;
    private int counter;

    private String password;
    private boolean passwordUpdated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getParcelableExtra("user");
        btnBack = findViewById(R.id.btnBackChangeProfile);
        btnSubmit = findViewById(R.id.btnSubmitChangeProfile);
        btnDeleteUser = findViewById(R.id.btnRemoveUser);
        txtChangeNameProfile = findViewById(R.id.txtChangeProfileName);
        txtChangeEmail = findViewById(R.id.txtChangeProfileEmail);
        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        getDecryptedPassword();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                updateFields();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
            }
        });

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                areYouSure();
            }
        });
    }

    private void getDecryptedPassword() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/getPassword";
        String urlCreate = baseUrl + "/" + user.getIdUser();



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
                                // This if statement IDK why is needed. If its removed then every dog is added twice.
                                JSONObject o = response.getJSONObject(i);
                                System.out.println("The encrypted password: " + o.getString("password"));
                                System.out.println("The decrypted password: " + EnhancedEncryption.decrypt(o.getString("password")));
                                password = EnhancedEncryption.decrypt(o.getString("password"));





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ChangeProfileInfoActivity.this, "Could not get password.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void updateFields() {
        String getTextFromChangeNameProfile = txtChangeNameProfile.getText().toString();
        if (!getTextFromChangeNameProfile.isEmpty()) {
            counter ++;
            updateName(getTextFromChangeNameProfile);
        }
        String getTextFromChangeEmail = txtChangeEmail.getText().toString();
        if (!getTextFromChangeEmail.isEmpty()) {
            counter ++;
            updateEmail(getTextFromChangeEmail);
        }

        String getTextFromCurrentPassword = txtCurrentPassword.getText().toString();
        String getTextFromNewPassword = txtNewPassword.getText().toString();
        if (!getTextFromCurrentPassword.isEmpty() || !getTextFromNewPassword.isEmpty()) {
            if (!getTextFromNewPassword.isEmpty()) {
                if (getTextFromCurrentPassword.isEmpty()) {
                    Toast.makeText(
                            ChangeProfileInfoActivity.this,
                            "Type your current password as well.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!(getTextFromCurrentPassword.equals(password))) {
                    Toast.makeText(
                            ChangeProfileInfoActivity.this,
                            "This is not your current password.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if (getTextFromNewPassword.equals(getTextFromCurrentPassword)) {
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "Your new password is no different.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        counter ++;
                        passwordUpdated = true;
                        updatePassword(getTextFromNewPassword);
                    }
                }
            }
            else {
                Toast.makeText(
                        ChangeProfileInfoActivity.this,
                        "Type also your new password.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (counter > 0 || passwordUpdated) {
            if (counter > 0 && passwordUpdated) {
                Toast.makeText(
                        ChangeProfileInfoActivity.this,
                        "Changes applied. New password saved.",
                        Toast.LENGTH_LONG).show();
            }
            else if (counter > 0) {
                Toast.makeText(
                        ChangeProfileInfoActivity.this,
                        "Changes applied.",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(
                        ChangeProfileInfoActivity.this,
                        "New password saved.",
                        Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(
                    ChangeProfileInfoActivity.this,
                    "No changes made.",
                    Toast.LENGTH_LONG).show();
        }
        counter = 0;
        passwordUpdated = false;
    }

    private void updatePassword(String getTextFromNewPassword) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changePassword";

        ProgressDialog progressDialog = new ProgressDialog(ChangeProfileInfoActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("Posted: " + getTextFromNewPassword);
                        System.out.println(EnhancedEncryption.encrypt(getTextFromNewPassword));
                        getDecryptedPassword();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "Unable to update password.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("password", EnhancedEncryption.encrypt(getTextFromNewPassword));
                params.put("iduser", user.getIdUser());
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void updateEmail(String email) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeEmail";


        ProgressDialog progressDialog = new ProgressDialog(ChangeProfileInfoActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "Unable to update email.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("iduser", user.getIdUser());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void updateName(String name) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeUserName";


        ProgressDialog progressDialog = new ProgressDialog(ChangeProfileInfoActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "Unable to update name.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("iduser", user.getIdUser());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void areYouSure() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Permanent Changes")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeUser();
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "User removed. Logging you out...",
                                Toast.LENGTH_LONG).show();
                        logOut();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    private void removeUser() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/removeUser";


        ProgressDialog progressDialog = new ProgressDialog(ChangeProfileInfoActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeProfileInfoActivity.this,
                                "Unable to remove user.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("iduser", user.getIdUser());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void logOut() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}