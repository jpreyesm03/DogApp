package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onBtnCreateAcc_Clicked(View Caller) {
        EditText email = (EditText) findViewById(R.id.emailCreate);
        EditText username = (EditText) findViewById(R.id.usernameCreate);
        EditText password1 = (EditText) findViewById(R.id.passwordCreate1);
        EditText password2 = (EditText) findViewById(R.id.passwordCreate2);
        String urlCreate = "https://studev.groept.be/api/a23PT106/create_account/";

        if(!password1.getText().toString().equals(password2.getText().toString()))
        {
            Toast.makeText(this, "The passwords do not match! Please retype.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Account created! Back to the loading screen!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }



    }
}