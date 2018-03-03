package almaland.net.almaland.TwitterLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import almaland.net.almaland.R;

public class TwitterLoginActivity extends AppCompatActivity {

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

        setContentView(R.layout.activity_twitter_login);

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
                        Toast.makeText(this, "Authentication failed.",
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result to the Twitter login button.
        loginTwitter.onActivityResult(requestCode, resultCode, data);
    }
}
