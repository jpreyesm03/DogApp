package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class ChangeDogInfo extends AppCompatActivity {
    private User user;
    private ArrayList<String> dogNames = new ArrayList<>();
    private ArrayList<String> dogIDs = new ArrayList<>();
    private ArrayList<Boolean> dogsBreedingState = new ArrayList<>();
    private int positionOfSpinner;
    private Button btnBack = findViewById(R.id.btnBackChangeDog);
    private Button btnSubmit = findViewById(R.id.btnSubmitChangeDog);
    private Button btnChangeImage = findViewById(R.id.btnChangeDogImage);
    private Switch switchBreedable = findViewById(R.id.schBreedableChange);
    private TextView txtChangeName = findViewById(R.id.txtChangeName);
    private TextView txtChangeBreed = findViewById(R.id.txtChangeBreed);
    private TextView txtChangeWeight = findViewById(R.id.txtChangeWeight);
    private TextView txtChangeHeight = findViewById(R.id.txtChangeHeight);
    private TextView txtChangeMedicalConditions = findViewById(R.id.txtChangeMedCond);
    private int counter = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_dog_info);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getDogs();
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




    }

    private void updateFields() {
        String getTextFromChangeName = txtChangeName.getText().toString();
        if (!getTextFromChangeName.isEmpty()) {
            counter ++;
            updateName(getTextFromChangeName);
        }
        String getTextFromChangeBreed = txtChangeBreed.getText().toString();
        if (!getTextFromChangeBreed.isEmpty()) {
            counter ++;
            updateBreed(getTextFromChangeBreed);
        }
        String getTextFromChangeWeight = txtChangeWeight.getText().toString();
        if (!getTextFromChangeWeight.isEmpty()) {
            counter ++;
            updateWeight(getTextFromChangeWeight);
        }String getTextFromChangeHeight= txtChangeName.getText().toString();
        if (!getTextFromChangeHeight.isEmpty()) {
            counter ++;
            updateHeight(getTextFromChangeHeight);
        }
        String getTextFromChangeMedCond = txtChangeMedicalConditions.getText().toString();
        if (!getTextFromChangeMedCond.isEmpty()) {
            counter ++;
            updateMedCond(getTextFromChangeMedCond);
        }




        // SwitchUpdate
        switchBreedable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String currentDogID = dogIDs.get(positionOfSpinner).toString();
                counter ++;
                if (isChecked) {
                    // Switch is ON
                    // Do something when the switch is on
                    System.out.println("Turned On");
                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "1" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(ChangeDogInfo.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, true);
//                                    Toast.makeText(
//                                            ChangeDogInfo.this,
//                                            "Set to Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            ChangeDogInfo.this,
                                            "Something went wrong [Breedable]..." + error,
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                    ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                        @Override
                        protected Map<String, String> getParams() {
                            /* Map<String, String> with key value pairs as data load */
                            Map<String, String> params = new HashMap<>();
                            params.put("breedable", "1");
                            params.put("idpet", currentDogID);


                            return params;
                        }
                    };
                    requestQueue.add(submitRequest);

                } else {
                    // Switch is OFF
                    // Do something when the switch is off
                    System.out.println("Turned Off");
                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "0" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(ChangeDogInfo.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, false);
//                                    Toast.makeText(
//                                            ChangeDogInfo.this,
//                                            "Set to Not Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            ChangeDogInfo.this,
                                            "Something went wrong [Breedable]..." + error,
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                    ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                        @Override
                        protected Map<String, String> getParams() {
                            /* Map<String, String> with key value pairs as data load */
                            Map<String, String> params = new HashMap<>();
                            params.put("breedable", "0");
                            params.put("idpet", currentDogID);


                            return params;
                        }
                    };
                    requestQueue.add(submitRequest);
                }
            }
        });
        if (counter > 0) {
            Toast.makeText(
                    ChangeDogInfo.this,
                    "Changes applied.",
                    Toast.LENGTH_LONG).show();
            getDogs();
        }
        else {
            Toast.makeText(
                    ChangeDogInfo.this,
                    "No changes made.",
                    Toast.LENGTH_LONG).show();
        }
        counter = 0;
    }

    private void updateName(String name) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeDogName";
        String urlCreate = baseUrl + "/" + name + "/" + dogIDs.get(positionOfSpinner).toString();


        ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeDogInfo.this,
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
                params.put("idpet", dogIDs.get(positionOfSpinner).toString());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void updateBreed(String breed) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeDogBreed";
        String urlCreate = baseUrl + "/" + breed + "/" + dogIDs.get(positionOfSpinner).toString();


        ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeDogInfo.this,
                                "Unable to update breed.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("breed", breed);
                params.put("idpet", dogIDs.get(positionOfSpinner).toString());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void updateWeight(String weight) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeDogWeight";
        String urlCreate = baseUrl + "/" + weight + "/" + dogIDs.get(positionOfSpinner).toString();


        ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeDogInfo.this,
                                "Unable to update weight.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("weight", weight);
                params.put("idpet", dogIDs.get(positionOfSpinner).toString());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void updateHeight(String height) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeDogHeight";
        String urlCreate = baseUrl + "/" + height + "/" + dogIDs.get(positionOfSpinner).toString();


        ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeDogInfo.this,
                                "Unable to update height.",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("height", height);
                params.put("idpet", dogIDs.get(positionOfSpinner).toString());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void getDogs() {
        {
            String baseUrl = "https://studev.groept.be/api/a23PT106/user_dogs";
            String urlCreate = baseUrl + "/" + user.getIdUser();



            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest queueRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    urlCreate,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            dogIDs.clear();
                            dogNames.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    // This if statement IDK why is needed. If its removed then every dog is added twice.
                                    if (dogNames.size() < response.length()) {
                                        JSONObject o = response.getJSONObject(i);
                                        System.out.println(response.length());
                                        System.out.print(response);
                                        String dogName = o.getString("name");
                                        String dogID = o.getString("idPet");
                                        String dogBreedingState = o.getString("breedable");
                                        dogNames.add(dogName);
                                        dogIDs.add(dogID);



                                        if (dogBreedingState.equals("0")) { dogsBreedingState.add(false); }
                                        else { dogsBreedingState.add(true); }
                                    }





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (String dog: dogNames) {
                                System.out.println(dog);
                            }
                            updateSpinner();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ChangeDogInfo.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(queueRequest);
        }
    }

    private void updateMedCond(String medCond) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/changeDogMedCond";
        String urlCreate = baseUrl + "/" + medCond + "/" + dogIDs.get(positionOfSpinner).toString();


        ProgressDialog progressDialog = new ProgressDialog(ChangeDogInfo.this);
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChangeDogInfo.this,
                                "Unable to update MedCond",
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("medcond", medCond);
                params.put("idpet", dogIDs.get(positionOfSpinner).toString());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }




    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.spMyDogsChange);


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
                System.out.println(dogIDs.get(positionOfSpinner).toString());
                switchBreedable.setChecked(dogsBreedingState.get(positionOfSpinner));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }
}