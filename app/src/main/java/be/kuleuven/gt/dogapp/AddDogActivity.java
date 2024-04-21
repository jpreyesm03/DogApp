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

//    private void onAddDogBtn_Clicked(View Caller)
//    {
//        EditText dogn = findViewById(R.id.dogName);
//        EditText dogb = findViewById(R.id.breedDog);
//        EditText doga = findViewById(R.id.ageDog);
//        EditText dogw = findViewById(R.id.weightDog);
//        EditText dogh = findViewById(R.id.heightDog);
//        EditText dogm = findViewById(R.id.medicalDog);
//        ProgressDialog progressDialog = new ProgressDialog(AddDogActivity.this);
//        progressDialog.setMessage("Logging in, please wait...");
//        progressDialog.show();
//        String dogName = dogn.getText().toString();
//        String dogBreed = dogb.getText().toString();
//        String dogAge = doga.getText().toString();
//        String dogWeight = dogw.getText().toString();
//        String dogHeight = dogh.getText().toString();
//        String dogMed = dogm.getText().toString();
//        String dogMedical = dogMed.replace(" ", "_");
//        String userid = findUserID(user.getUsername(),user.getEmail(),user.getPassword());
//
//        String baseUrl = "https://studev.groept.be/api/a23PT106/login/";
//        String urlCreate = baseUrl + "/" + dogName + "/" + dogBreed + "/" + dogAge+ "/" + dogWeight+ "/" + dogHeight + "/" + userid + "/" + dogMedical ;
//
//
//
//        ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this);
//        progressDialog.setMessage("Uploading, please wait...");
//        progressDialog.show();
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest submitRequest = new StringRequest(
//                Request.Method.POST,
//                urlCreate,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        Toast.makeText(
//                                CreateAccountActivity.this,
//                                "Account created! Back to the loading screen!",
//                                Toast.LENGTH_SHORT).show();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(
//                                CreateAccountActivity.this,
//                                "Unable to create account: " + error,
//                                Toast.LENGTH_LONG).show();
//
//                    }
//                }
//        )
//
//        { //NOTE THIS PART: here we are passing the POST parameters to the webservice
//            @Override
//            protected Map<String, String> getParams() {
//                /* Map<String, String> with key value pairs as data load */
//                Map<String, String> params = new HashMap<>();
//                params.put("name", u);
//                params.put("email", e);
//                params.put("password", p1);
//                return params;
//            }
//        };
//        requestQueue.add(submitRequest);
//
//    }

    private String findUserID(String username,String email,String password)
    {
        return null;
    }

}