package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class MessageInbox extends AppCompatActivity {


    private User user;
    private ArrayList<String> messageSenders;
    private ArrayList<String> messageNames;
    private ArrayList<String> messageText;
    private ArrayList<String> messageDate;
    private ArrayList<String> messageTime;

    private TextView senderName;
    private TextView senderEmail;
    private TextView messageTextview;
    private Button refresh;



    private int positionOfSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_messages);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = getIntent().getParcelableExtra("user");
        messageSenders = new ArrayList<>();
        messageText = new ArrayList<>();
        messageDate = new ArrayList<>();
        messageTime = new ArrayList<>();
        messageNames = new ArrayList<>();
        senderName = findViewById(R.id.senderName);
        senderEmail = findViewById(R.id.senderEmail);
        messageTextview = findViewById(R.id.messageText);
        refresh = findViewById(R.id.refresh);



    }

    public void onBtnRefresh_Clicked(View Caller) {
        getMessage();
    }

    private void getMessage() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/messageInbox"; // Assuming this endpoint provides message sender emails
        String url = baseUrl + "/" + user.getIdUser();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        messageSenders.clear(); // Clear previous data
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String senderEmail = jsonObject.getString("sender_email");
                                if(jsonObject.getString("read").equals("1"))
                                {
                                    messageSenders.add("●" + senderEmail);



                                }
                                else
                                {
                                    messageSenders.add(senderEmail);
                                }
                                messageNames.add(jsonObject.getString("sender_name"));
                                messageText.add(jsonObject.getString("text"));
                                messageDate.add(jsonObject.getString("date"));
                                messageTime.add(jsonObject.getString("time"));

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
                        Toast.makeText(MessageInbox.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.selectMessage);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, messageSenders);

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
                if (messageSenders.get(positionOfSpinner).contains("●"))
                {
                    String realEmail =messageSenders.get(positionOfSpinner).replace("● ", "");
                    senderEmail.setText(realEmail);

                }
                else {
                    senderEmail.setText(messageSenders.get(positionOfSpinner));
                }
                senderName.setText(messageNames.get(positionOfSpinner));
                messageTextview.setText(messageText.get(positionOfSpinner));



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }



}