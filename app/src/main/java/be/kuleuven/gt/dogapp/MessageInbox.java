package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

import be.kuleuven.gt.dogapp.R;

public class MessageInbox extends AppCompatActivity {


    private User user;
    private ArrayList<String> messageSenders;
    private ArrayList<String> messageNames;
    private ArrayList<String> messageText;
    private ArrayList<String> messageDate;
    private ArrayList<String> messageTime;
    private ArrayList<String> messageID;

    private TextView senderName;
    private TextView senderEmail;
    private TextView messageTextview;
    private TextView messageDate1;
    private TextView messageTime1;




    private int positionOfSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_inbox);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.selectMessage), (v, insets) -> {
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
        messageID = new ArrayList<>();
        senderName = findViewById(R.id.senderName);
        senderEmail = findViewById(R.id.senderEmail);
        messageTextview = findViewById(R.id.messageText);
        messageDate1 = findViewById(R.id.messageDate);
        messageTime1 = findViewById(R.id.messageTime);

        Button btnBack = findViewById(R.id.btnBack);


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

    public void onBtnRefresh_Clicked(View Caller) {
        getMessage();
    }

    public void onBtnSendNewMessage_Clicked(View Caller) {
        Intent intent = new Intent(this, SendMessages.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }


    private void getMessage() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/messageInbox"; // Assuming this endpoint provides message sender emails
        String url = baseUrl + "/" + user.getUsername() + "/" + user.getEmail();

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
                                if(jsonObject.getString("read").equals("0"))
                                {
                                    messageSenders.add("●  " + senderEmail);



                                }
                                else
                                {
                                    messageSenders.add(senderEmail);
                                }
                                messageNames.add(jsonObject.getString("sender_name"));
                                messageText.add(jsonObject.getString("text"));
                                messageDate.add(jsonObject.getString("date"));
                                messageTime.add(jsonObject.getString("time"));
                                messageID.add(jsonObject.getString("idMessage"));

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

                if (messageSenders.get(positionOfSpinner).charAt(0) == '●')
                {
                    String realEmail =messageSenders.get(positionOfSpinner).replace("●", "");
                    senderEmail.setText(realEmail);

                }
                else {
                    senderEmail.setText(messageSenders.get(positionOfSpinner));
                }
                senderName.setText(messageNames.get(positionOfSpinner));
                messageTextview.setText(messageText.get(positionOfSpinner));
                messageDate1.setText(messageDate.get(positionOfSpinner));
                messageTime1.setText(messageTime.get(positionOfSpinner));

                readMessage(positionOfSpinner);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }

    private void readMessage(int positionOfSpinner)
    {
        String baseUrl = "https://studev.groept.be/api/a23PT106/readMessage"; // Assuming this endpoint provides message sender emails
        String url = baseUrl + "/" + "1" + "/" + messageID.get(positionOfSpinner);
        ProgressDialog progressDialog = new ProgressDialog(MessageInbox.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
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


}