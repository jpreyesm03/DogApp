package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import be.kuleuven.gt.dogapp.model.ImageUploadTask;
import be.kuleuven.gt.dogapp.model.User;

public class MyDogsActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String SELECTED_IMAGE_URI_KEY = "selected_image_uri";
    private User user;
    private ArrayList<String> dogNames;
    private ArrayList<String> dogIDs;
    private ArrayList<String> dogSex;
    private ArrayList<String> petImages = new ArrayList<>();


    private ArrayList<String> dogsAge = new ArrayList<>();
    private ArrayList<String> dogsBreed = new ArrayList<>();
    private ArrayList<Boolean> dogsBreedingState;
    private int positionOfSpinner;
    private Switch schBreedable;
    private TextView txtAge;
    private TextView txtBreed;
    private ImageView btnMyDog;
    private SharedPreferences sharedPreferences;
    private boolean returningFromThreeBars;

    @Override //
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_dogs);
        dogNames = new ArrayList<>();
        dogIDs = new ArrayList<>();
        dogSex = new ArrayList<>();
        dogsBreedingState = new ArrayList<>();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            positionOfSpinner = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("position")));
            returningFromThreeBars = true;
            System.out.println("Parameter passed correctly, position: " + positionOfSpinner);
        }
        catch (Exception e) {
            System.out.println("Not returning from three bars: " + e);
        }
        txtAge = findViewById(R.id.txtAge);
        txtBreed = findViewById(R.id.txtBreed);
        ImageView btnThreeBars = findViewById(R.id.btnThreeBars);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);
        ImageView btnCalculator = findViewById(R.id.btnCalculator);
        ImageView btnBreeding = findViewById(R.id.btnBreeding);
        ImageView btnMap= findViewById(R.id.btnMap);
        ImageView btnTrainingVideos = findViewById(R.id.btnVideos);
        btnMyDog = findViewById(R.id.imgMyDog);
        schBreedable = findViewById(R.id.schBreedable);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        schBreedable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String currentDogID = dogIDs.get(positionOfSpinner).toString();
                if (isChecked) {
                    // Switch is ON
                    // Do something when the switch is on

                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "1" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(MyDogsActivity.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(MyDogsActivity.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, true);
//                                    Toast.makeText(
//                                            MyDogsActivity.this,
//                                            "Set to Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            MyDogsActivity.this,
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

                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "0" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(MyDogsActivity.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(MyDogsActivity.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, false);
//                                    Toast.makeText(
//                                            MyDogsActivity.this,
//                                            "Set to Not Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            MyDogsActivity.this,
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



        btnThreeBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openCalendar();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openMap();
            }
        });

        btnBreeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openBreeding();
            }
        });

        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openFoodCalculator();
            }
        });

        btnTrainingVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openTrainingVideos();
            }
        });

        btnMyDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openGallery();
            }
        });

//        String selectedImageUriString = sharedPreferences.getString(SELECTED_IMAGE_URI_KEY, null);
//        if (selectedImageUriString != null) {
//            Uri selectedImageUri = Uri.parse(selectedImageUriString);
//            btnMyDog.setImageURI(selectedImageUri);
//        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            btnMyDog.setImageURI(selectedImageUri);
            postImage();

//            // Save the selected image URI to SharedPreferences
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(SELECTED_IMAGE_URI_KEY, selectedImageUri.toString());
//            editor.apply();
        }
    }
    private void postImage() {
        BitmapDrawable drawable = (BitmapDrawable) btnMyDog.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String petImage = bitmapToString(bitmap);

        String dogID = dogIDs.get(positionOfSpinner);

        String baseUrl = "https://studev.groept.be/api/a23PT106/changeImage";


        ProgressDialog progressDialog = new ProgressDialog(MyDogsActivity.this);
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
                        petImages.set(positionOfSpinner, petImage);
                        Toast.makeText(
                                MyDogsActivity.this,
                                "Image changed!",
                                Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                MyDogsActivity.this,
                                "Unable to add dog: " + error,
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("petimage", petImage);
                params.put("idpet", dogID);
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }




    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void openThreeBarsFunction() {
        // Implement your functionality here
        Intent intent = new Intent(this, ThreeBarsActivity.class);

        intent.putExtra("user", user);
        intent.putExtra("name", dogNames.get(positionOfSpinner));
        intent.putExtra("position", String.valueOf(positionOfSpinner));
        startActivity(intent);
    }

    private void openCalendar() {
        // Implement your functionality here
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openMap() {
        // Implement your functionality here
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openBreeding() {
        // Implement your functionality here
        Intent intent = new Intent(this, BreedingActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("dogID",dogIDs.get(positionOfSpinner));
        startActivity(intent);
    }

    private void openTrainingVideos() {
        // Implement your functionality here
        Intent intent = new Intent(this, TrainingVideosActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openFoodCalculator() {
        // Implement your functionality here
        Intent intent = new Intent(this, FoodCalculatorActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void loadDogData(User user) {
        dogNames.clear(); // Clear previous data
        dogIDs.clear(); // Clear previous data
        getDogs(user);
    }

    protected void onResume() {
        dogNames.clear(); // Clear previous data
        dogIDs.clear();
        super.onResume();
        loadDogData(user);
    }

    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.spMyDogs);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        if (returningFromThreeBars) {
            spinner.setSelection(positionOfSpinner);
            System.out.println("OnItem, position: " + positionOfSpinner);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // `position` parameter contains the position of the selected item in the dropdown
                // Now you can use `position` as needed
                if (!returningFromThreeBars) {
                    positionOfSpinner = position;
                }
                else {
                    returningFromThreeBars = false;
                }


                if (petImages.get(positionOfSpinner).equals("null") || petImages.get(positionOfSpinner).isEmpty()) {
                    btnMyDog.setImageResource(R.drawable.dogs);
                }
                else {
                    btnMyDog.setImageBitmap(imageDecode(petImages.get(positionOfSpinner)));
                }

                schBreedable.setChecked(dogsBreedingState.get(positionOfSpinner));
                int txtToChangeAge = 0;
                String txtToChangeBreed = "";
                if (!dogsAge.get(positionOfSpinner).equals(" ") && !dogsAge.get(positionOfSpinner).equals("_") && !(dogsAge.get(positionOfSpinner) == null)) {
                    try {
                        Integer.parseInt(dogsAge.get(positionOfSpinner));
                        txtAge.setText("Age: " + dogsAge.get(positionOfSpinner));
                    }
                    catch (NumberFormatException e){

                        txtAge.setText("Age is not specified.");
                    }
                }
                else {
                    txtAge.setText("Age is not specified.");
                }
                if (!dogsBreed.get(positionOfSpinner).equals(" ") && !dogsBreed.get(positionOfSpinner).equals("_") && !(dogsBreed.get(positionOfSpinner) == null)) {
                    txtBreed.setText("Breed: " + dogsBreed.get(positionOfSpinner));
                }
                else {
                    txtBreed.setText("Breed is not specified.");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)

                }
        });
    }

    private Bitmap imageDecode(String petImage) {
        byte[] decodedBytes = Base64.decode(petImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    private void getDogs(User u)
   {
        String baseUrl = "https://studev.groept.be/api/a23PT106/user_dogs";
        String urlCreate = baseUrl + "/" + u.getIdUser();



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dogNames.clear();
                        dogIDs.clear();
                        dogSex.clear();
                        boolean match = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String id = o.getString("idPet");
                                String dogName = o.getString("name");
                                String dogBreedingState = o.getString("breedable");
                                String dogAge = o.getString("age");
                                String dogBreed = o.getString("breed");
                                String petImage = o.getString("petimage");
                                String sex = o.getString("sex");
                                dogIDs.add(id);
                                dogSex.add(sex);
                                dogNames.add(dogName);
                                dogsAge.add(dogAge);
                                dogsBreed.add(dogBreed);
                                petImages.add(petImage);

                                if (dogBreedingState.equals("0")) { dogsBreedingState.add(false); }
                                else { dogsBreedingState.add(true); }



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

                        Toast.makeText(MyDogsActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }



}