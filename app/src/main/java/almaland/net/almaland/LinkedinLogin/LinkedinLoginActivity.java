package almaland.net.almaland.LinkedinLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import almaland.net.almaland.HomeActivity;
import almaland.net.almaland.LoginActivity;
import almaland.net.almaland.R;

public class LinkedinLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_login);
    }

    // Authenticate with linkedin and intialize Session.
    public void loginLinkedin(View view){
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LinkedinLoginActivity.this, HomeActivity.class));
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // handle the response by calling LISessionManager and start new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Linkedin
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}