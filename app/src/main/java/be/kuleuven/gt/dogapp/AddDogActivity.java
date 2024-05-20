package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
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
    private ArrayList<String> dogSex;
    private int positionOfSpinner;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getParcelableExtra("user");

        dogSex = new ArrayList<>();
        dogSex.add("Male");
        dogSex.add("Female");
        updateSpinner();


    }




    public void onBtnButtonAddDog_Clicked(View Caller) {
        fetchInfo(dogSex.get(positionOfSpinner));
    }


    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.spinner2);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogSex);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // `position` parameter contains the position of the selected item in the dropdown
                // Now you can use `position` as needed
                positionOfSpinner = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }
    private void openMyDogs() {
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void fetchInfo(String sex) {
        EditText dogn = findViewById(R.id.dogName);
        EditText dogb = findViewById(R.id.dogBreed);
        EditText doga = findViewById(R.id.dogAge);
        EditText dogw = findViewById(R.id.dogWeight);
        EditText dogh = findViewById(R.id.heightDog);
        EditText dogm = findViewById(R.id.medicalDog);

        id = user.getIdUser();
        int isMale;

        if(sex.equals("Male"))
        {
            isMale = 1;
        }
        else {
            isMale = 0;
        }

        dogName = dogn.getText().toString();
        dogBreed = dogb.getText().toString().replace(" ", "_");
        dogAge = doga.getText().toString();
        dogWeight = dogw.getText().toString();
        dogHeight = dogh.getText().toString();
        String dogMed = dogm.getText().toString();
        dogMedical = dogMed.replace(" ", "_");

        if (dogMedical.isEmpty()) {
            dogMedical = "_";
        }
        if (dogBreed.isEmpty()) {
            dogBreed = "_";
        }
        if (dogAge.isEmpty()) {
            dogAge = "_";
        }
        if (dogWeight.isEmpty()) {
            dogWeight = "20";
        }
        if (dogHeight.isEmpty()) {
            dogHeight = "_";
        }

        if (dogName.isEmpty()) {
            Toast.makeText(
                    AddDogActivity.this,
                    "Please fill all necessary info! Feel free to approximate magnitudes.",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (id.equals("unknown")) {
                Toast.makeText(
                        AddDogActivity.this,
                        "Error connecting - user unknown",
                        Toast.LENGTH_SHORT).show();
            } else {

                String baseUrl = "https://studev.groept.be/api/a23PT106/add_dog";


                ProgressDialog progressDialog = new ProgressDialog(AddDogActivity.this);
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
                                Toast.makeText(
                                        AddDogActivity.this,
                                        "Dog added! Taking you to dashboard!",
                                        Toast.LENGTH_SHORT).show();
                                openMyDogs();

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        AddDogActivity.this,
                                        "Unable to add dog: " + error,
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
                        params.put("user", id);
                        params.put("medical", dogMedical);
                        params.put("sex",Integer.toString(isMale));

                        return params;
                    }
                };
                requestQueue.add(submitRequest);
            }
        }
    }


}