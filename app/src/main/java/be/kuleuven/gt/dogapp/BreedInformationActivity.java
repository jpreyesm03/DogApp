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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class BreedInformationActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> breedInfo;
    private ArrayList<String> dogNames;
    private WebView urlToShow;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_breed_information);
        user = (User) getIntent().getParcelableExtra("user");
        dogNames = new ArrayList<>();
        breedInfo = new ArrayList<>();
        urlToShow = findViewById(R.id.webDogs);
        urlToShow.setWebViewClient(new WebViewClient()); // to open links inside the WebView

        // Enable JavaScript
        WebSettings webSettings = urlToShow.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
                        boolean match = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String breed = o.getString("breed");
                                String dogName = o.getString("name");
                                breedInfo.add(breed);
                                System.out.println(breed);
                                dogNames.add(dogName);




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(breedInfo);
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
        urlToShow.loadUrl("https://www.dogster.com/lifestyle/popular-and-famous-dogs-in-history");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the text of the TextView based on the selected item
                String urlToBeUsed = produceURL(breedInfo.get(position));
                urlToShow.loadUrl(urlToBeUsed);
                System.out.println(breedInfo.get(position));
//                if (urlExists(urlToBeUsed)) {
//                    urlToShow.loadUrl(urlToBeUsed);
//                } else {
//                    Toast.makeText(BreedInformationActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
//                    urlToShow.loadUrl("https://www.dogster.com/lifestyle/popular-and-famous-dogs-in-history");
//                }
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

    private boolean urlExists(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean urlExists2(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check if the response code is HTTP_OK
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the HTML content of the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                inputStream.close();

                // Analyze the HTML content to detect error messages or absence of content
                String htmlContent = content.toString();
                if (isErrorPage(htmlContent)) {
                    // URL does not exist, suggest a default link
                    System.out.println("URL does not exist. Suggesting default link...");
                    return false;
                } else {
                    // URL exists
                    return true;
                }
            } else {
                // Response code is not HTTP_OK
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isErrorPage(String htmlContent) {
        // Check if the HTML content contains the error message
        return htmlContent.contains("We weren't able to fetch that page for you.");
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
        startActivity(intent);

    }
}

/*
android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import android.widget.TextView;
        import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.textView);

        // Define the options for the dropdown
        String[] options = {"Option 1", "Option 2", "Option 3"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set up a listener for the spinner to respond to item selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the text of the TextView based on the selected item
                textView.setText(options[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });*/
