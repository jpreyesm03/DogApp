package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.CaesarCipher;
import be.kuleuven.gt.dogapp.model.User;

public class BreedingActivity extends AppCompatActivity {
    private User user;
    private String dogBreed;
    private String dogSex;
    private ArrayList<String> dogNames;
    private ArrayList<String> medicals;
    private ArrayList<String> dogsAge;
    private ArrayList<String> dogsBreed;
    private ArrayList<String> owners;
    private int positionOfSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breeding);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dogNames = new ArrayList<>();
        medicals = new ArrayList<>();
        dogsBreed = new ArrayList<>();
        dogsAge = new ArrayList<>();
        owners = new ArrayList<>();


        Button btnBack = findViewById(R.id.btnBack);
        String dogID = getIntent().getParcelableExtra("dogID");
        fetchInfo(dogID);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
            }
        });
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }




    
    private void fetchInfo(String dogID) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/find_dog";

        String urlCreate = baseUrl + "/" + dogID ;
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
                                dogBreed = o.getString("breed");
                                dogSex = o.getString("sex");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BreedingActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);

    }

    public void onBtnSeeAvailableDogs_Clicked(View Caller) {
        findDogs();

    }

    private void findDogs() {
        String lookingFor;

        if(dogSex.equals("0"))
        {
             lookingFor = "1";
        }
        else
        {
             lookingFor = "0";
        }

        String baseUrl = "https://studev.groept.be/api/a23PT106/getBreedablePets";
        String urlCreate = baseUrl + "/" + dogBreed  + "/"+ lookingFor;



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dogNames.clear();
                        medicals.clear();
                        dogsAge.clear();
                        dogsBreed.clear();
                        owners.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String userid = o.getString("user_id");
                                String dogName = o.getString("name");
                                String medical = o.getString("medical_cond");
                                String dogAge = o.getString("age");
                                String dogBreed = o.getString("breed");

                                medicals.add(medical);
                                dogNames.add(dogName);
                                dogsAge.add(dogAge);
                                dogsBreed.add(dogBreed);
                                owners.add(userid);





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateSpinner();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(BreedingActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }

    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.spinner3);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);

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


}