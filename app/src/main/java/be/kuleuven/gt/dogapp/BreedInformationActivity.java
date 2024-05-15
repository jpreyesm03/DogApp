package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import java.util.ArrayList;
import java.util.Objects;

import be.kuleuven.gt.dogapp.model.User;



public class BreedInformationActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> breedInfo;
    private ArrayList<String> dogNames;
    private WebView urlToShow;
    private String name;
    private int position;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breed_information);
        user = (User) getIntent().getParcelableExtra("user");
        name = getIntent().getStringExtra("name");
        position = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("position")));
        dogNames = new ArrayList<>();
        breedInfo = new ArrayList<>();
        urlToShow = findViewById(R.id.webDogs);
        urlToShow.setWebViewClient(new WebViewClient()); // to open links inside the WebView

        // Enable JavaScript
        WebSettings webSettings = urlToShow.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Condition that looks for "unable to fetch"... and gives a default link.
        urlToShow.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Inject JavaScript to search for the desired content
                urlToShow.evaluateJavascript(
                        "(function() { " +
                                "   var h3Elements = document.getElementsByTagName('h3');" +
                                "   for (var i = 0; i < h3Elements.length; i++) {" +
                                "       if (h3Elements[i].textContent.includes('We weren\\'t able to fetch that page for you.')) {" +
                                "           return true;" +
                                "       }" +
                                "   }" +
                                "   return false;" +
                                "})();",
                        result -> {
                            if ("true".equals(result)) {
                                // Handle the case where the error message is found
                                urlToShow.loadUrl("https://www.dogster.com/lifestyle/popular-and-famous-dogs-in-history");
                                System.out.println("The error message was found in an <h3> element.");
                            } else {
                                // Handle the case where the error message is not found
                                System.out.println("The error message was not found in any <h3> element.");
                            }
                        });
            }
        });




        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                System.out.println("Went back.");
                openPrevious();

            }
        });

    }

    private void loadDogData(User user) {
        dogNames.clear(); // Clear previous data
        getDogs(user);
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
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // This if statement IDK why is needed. If its removed then every dog is added twice.
                                if (dogNames.size() < response.length()) {
                                    JSONObject o = response.getJSONObject(i);
                                    System.out.println(response.length());
                                    System.out.print(response);
                                    String breed = o.getString("breed");
                                    String dogName = o.getString("name");
                                    breedInfo.add(breed);
                                    dogNames.add(dogName);
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

                        Toast.makeText(BreedInformationActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }

    private void updateSpinner() {
        // Get the spinner from the layout
        Spinner spinner = findViewById(R.id.spUserDogs);

        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
//        urlToShow.loadUrl("https://www.dogster.com/lifestyle/popular-and-famous-dogs-in-history");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String urlToBeUsed = produceURL(breedInfo.get(position));
                urlToShow.loadUrl(urlToBeUsed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not sure about this yet.
                urlToShow.loadUrl(produceURL(breedInfo.get(0)));
            }
        });
    }





    private String produceURL(String breed)  {
        if (breed == null) {
            return "https://www.dogster.com/lifestyle/popular-and-famous-dogs-in-history";
        }
        else {
            String breedForLink = breed.replace(' ', '-');
            return "https://www.dogster.com/dog-breeds/" + breedForLink.toLowerCase();
        }
    }






    protected void onResume() {
        super.onResume();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, ThreeBarsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("name", name);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);

    }
}
