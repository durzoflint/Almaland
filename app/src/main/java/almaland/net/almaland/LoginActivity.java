package almaland.net.almaland;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TwitterLoginButton loginTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();
        Twitter.initialize(twitterConfig);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        loginTwitter = findViewById(R.id.login_twitter);
        loginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("Abhinav", "twitterLogin:success" + result);
                //Handle valid login here
                handleTwitterSession(result.data);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("Abhinav", "twitterLogin:failure", exception);
                //Handle invalid login here
                //updateUI(null);
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
            //If Email not registered then take display invalid username or password
        });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d("Abhinav", "handleTwitterSession:" + session);
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Abhinav", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Abhinav", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
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
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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

        // Pass the activity result to the Twitter login button.
        loginTwitter.onActivityResult(requestCode, resultCode, data);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}