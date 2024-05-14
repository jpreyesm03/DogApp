package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import be.kuleuven.gt.dogapp.model.User;

public class SeeUserInfoActivity extends AppCompatActivity {

    private String username;
    private String email;
    private TextView emailText;
    private TextView nameText;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = getIntent().getParcelableExtra("user");
        username = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        emailText = findViewById(R.id.ownerEmail);
        nameText = findViewById(R.id.ownerUsername);
        emailText.setText("Email: " + email);
        nameText.setText("Username: " + username);



    }
    public void onBtnSendMessage_Clicked(View Caller) {
       Intent intent = new Intent(this, SendMessages.class);
       intent.putExtra("user",user);
       startActivity(intent);

    }
    public void onBtnSeeMessages_Clicked(View Caller) {
        Intent intent = new Intent(this, MessageInbox.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }
    public void onBtnBack_Clicked(View Caller) {
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }

}