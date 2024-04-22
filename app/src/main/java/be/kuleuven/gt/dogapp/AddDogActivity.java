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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class AddDogActivity extends AppCompatActivity {
    private User user;
    private String dogName;
    private String dogBreed;
    private String dogAge;
    private String dogWeight;
    private String dogHeight;

    private String dogMedical;

    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getParcelableExtra("user");
    }

    private void onBtnAddDo1g_Clicked(View Caller)
    {
        EditText dogn = findViewById(R.id.dogName);
        EditText dogb = findViewById(R.id.dogBreed);
        EditText doga = findViewById(R.id.dogAge);
        EditText dogw = findViewById(R.id.dogWeight);
        EditText dogh = findViewById(R.id.heightDog);
        EditText dogm = findViewById(R.id.medicalDog);

        dogName = dogn.getText().toString();
        dogBreed = dogb.getText().toString();
        dogAge = doga.getText().toString();
        dogWeight = dogw.getText().toString();
        dogHeight = dogh.getText().toString();
        String dogMed = dogm.getText().toString();
        dogMedical = dogMed.replace(" ", "_");

        if (dogMedical.isEmpty())
        {
            dogMedical = "_";
        }

        if(dogName.isEmpty()||dogBreed.isEmpty()||dogAge.isEmpty()||dogWeight.isEmpty()||dogHeight.isEmpty())
        {
            Toast.makeText(
                    AddDogActivity.this,
                    "Please fill all necessary info!",
                    Toast.LENGTH_SHORT).show();
        }
        if(id.equals("unknown"))
        {
            Toast.makeText(
                    AddDogActivity.this,
                    "Error connecting - user unknown",
                    Toast.LENGTH_SHORT).show();
        }

        else {

            String userid = findUserID(user.getUsername(), user.getEmail(), user.getPassword());

            String baseUrl = "https://studev.groept.be/api/a23PT106/add_dog/";
            String urlCreate = baseUrl + "/" + dogName + "/" + dogBreed + "/" + dogAge + "/" + dogWeight + "/" + dogHeight + "/" + userid + "/" + dogMedical;


            ProgressDialog progressDialog = new ProgressDialog(AddDogActivity.this);
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
                                    AddDogActivity.this,
                                    "Account created! Back to the loading screen!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    AddDogActivity.this,
                                    "Unable to create account: " + error,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
            ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                @Override
                protected Map<String, String> getParams() {
                    /* Map<String, String> with key value pairs as data load */
                    Map<String, String> params = new HashMap<>();
                    params.put("name", dogName);
                    params.put("breed", dogBreed);
                    params.put("age", dogAge);
                    params.put("weight", dogWeight);
                    params.put("height", dogHeight);
                    params.put("user", userid);
                    params.put("medical", dogMedical);

                    return params;
                }
            };
            requestQueue.add(submitRequest);
        }

    }

    private String findUserID(String username,String email,String password)
    {
        String baseUrl = "https://studev.groept.be/api/a23PT106/login/";
        String urlCreate = baseUrl + "/" + username + "/" + email + "/" + password;



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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(AddDogActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
        if(id.isEmpty())
        {
            return "unknown";
        }
        return id;

    }


    public void onBtnAddDog_Clicked(View view) {
        EditText dogn = findViewById(R.id.dogName);
        EditText dogb = findViewById(R.id.dogBreed);
        EditText doga = findViewById(R.id.dogAge);
        EditText dogw = findViewById(R.id.dogWeight);
        EditText dogh = findViewById(R.id.heightDog);
        EditText dogm = findViewById(R.id.medicalDog);

        dogName = dogn.getText().toString();
        dogBreed = dogb.getText().toString();
        dogAge = doga.getText().toString();
        dogWeight = dogw.getText().toString();
        dogHeight = dogh.getText().toString();
        String dogMed = dogm.getText().toString();
        dogMedical = dogMed.replace(" ", "_");

        if (dogMedical.isEmpty())
        {
            dogMedical = "_";
        }

        if(dogName.isEmpty()||dogBreed.isEmpty()||dogAge.isEmpty()||dogWeight.isEmpty()||dogHeight.isEmpty())
        {
            Toast.makeText(
                    AddDogActivity.this,
                    "Please fill all necessary info!",
                    Toast.LENGTH_SHORT).show();
        }
        if(id.equals("unknown"))
        {
            Toast.makeText(
                    AddDogActivity.this,
                    "Error connecting - user unknown",
                    Toast.LENGTH_SHORT).show();
        }

        else {

            String userid = findUserID(user.getUsername(), user.getEmail(), user.getPassword());

            String baseUrl = "https://studev.groept.be/api/a23PT106/add_dog/";
            String urlCreate = baseUrl + "/" + dogName + "/" + dogBreed + "/" + dogAge + "/" + dogWeight + "/" + dogHeight + "/" + userid + "/" + dogMedical;


            ProgressDialog progressDialog = new ProgressDialog(AddDogActivity.this);
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
                                    AddDogActivity.this,
                                    "Account created! Back to the loading screen!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    AddDogActivity.this,
                                    "Unable to create account: " + error,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
            ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                @Override
                protected Map<String, String> getParams() {
                    /* Map<String, String> with key value pairs as data load */
                    Map<String, String> params = new HashMap<>();
                    params.put("name", dogName);
                    params.put("breed", dogBreed);
                    params.put("age", dogAge);
                    params.put("weight", dogWeight);
                    params.put("height", dogHeight);
                    params.put("user", userid);
                    params.put("medical", dogMedical);

                    return params;
                }
            };
            requestQueue.add(submitRequest);
        }
    }
}