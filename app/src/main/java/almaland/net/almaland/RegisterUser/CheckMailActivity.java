package almaland.net.almaland.RegisterUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import almaland.net.almaland.LoginActivity;
import almaland.net.almaland.R;

public class CheckMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail);
        Button continueButton = findViewById(R.id.continuebutton);
        continueButton.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}