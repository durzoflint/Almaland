package almaland.net.almaland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import almaland.net.almaland.RegisterUser.RegisterUserActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextView notRegistered = findViewById(R.id.notregistered);
        notRegistered.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });
        Button login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            EditText email = findViewById(R.id.email);
            EditText password = findViewById(R.id.password);
            //Escape the password as below
            /*myURL = myURL.replaceAll(" ", "%20");
            myURL = myURL.replaceAll("\'", "%27");
            myURL = myURL.replaceAll("\'", "%22");
            myURL = myURL.replaceAll("\\(", "%28");
            myURL = myURL.replaceAll("\\)", "%29");
            myURL = myURL.replaceAll("\\{", "%7B");
            myURL = myURL.replaceAll("\\}", "%7B");
            myURL = myURL.replaceAll("\\]", "%22");
            myURL = myURL.replaceAll("\\[", "%22");*/
            //Todo use email and password to login at
            //http://almaland.net/app/login.php?email=aaaorabhinav@gmail.com&password=password
            //If Email not registered then display invalid username or password
        });
    }
}