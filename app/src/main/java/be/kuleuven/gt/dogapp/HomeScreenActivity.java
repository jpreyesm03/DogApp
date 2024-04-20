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

public class HomeScreenActivity extends AppCompatActivity {

    private TextView txtInfo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = (User) getIntent().getParcelableExtra("user");
        txtInfo = (TextView) findViewById(R.id.currentUser);
        String info = null;
        if (user != null) {
            info = "Username: " + user.getUsername();
            txtInfo.setText(info);
        }
        else {
            txtInfo.setText("User unknown");
        }

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void onBtnAddDog_Clicked(View Caller)
    {
        Intent intent = new Intent(this,AddDogActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}