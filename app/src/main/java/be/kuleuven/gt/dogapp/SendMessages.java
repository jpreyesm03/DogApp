package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class SendMessages extends AppCompatActivity {

    private EditText messageContent;
    private EditText receipientEmail;
    private EditText receipientUsername;
    private User user;




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

        messageContent = findViewById(R.id.messageContent);

        receipientUsername = findViewById(R.id.receipientUsername);


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
        Intent intent = new Intent(this, MessageInbox.class);
        intent.putExtra("user", user);
        startActivity(intent);
        
    }

    public void onBtnSend_Clicked(View Caller) {
        sendMessage();
    }

    private void sendMessage() {

        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

// Get current local time
        LocalTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
        }

// Convert LocalDate and LocalTime to strings
        String dateString = currentDate.toString();
        String timeString = currentTime.toString();

        String content = messageContent.getText().toString();

        String rname = receipientUsername.getText().toString();



        if (content.isEmpty() || rname.isEmpty()) {
            Toast.makeText(
                    SendMessages.this,
                    "Please fill all necessary info!",
                    Toast.LENGTH_SHORT).show();
        }
            else {

                String baseUrl = "https://studev.groept.be/api/a23PT106/sendMessages";


                ProgressDialog progressDialog = new ProgressDialog(SendMessages.this);
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
                                        SendMessages.this,
                                        "Message sent!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        SendMessages.this,
                                        "Unable to send message: " + error,
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                    @Override
                    protected Map<String, String> getParams() {
                        /* Map<String, String> with key value pairs as data load */
                        Map<String, String> params = new HashMap<>();
                        params.put("sname", user.getUsername());
                        params.put("semail", user.getEmail());
                        params.put("rname", rname);
                        params.put("remail", rname);
                        params.put("txt", content);
                        params.put("dat", dateString);
                        params.put("tim", timeString);

                        return params;
                    }
                };
                requestQueue.add(submitRequest);
            }
        }
    }


