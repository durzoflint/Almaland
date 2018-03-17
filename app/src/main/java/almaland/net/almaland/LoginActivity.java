package almaland.net.almaland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import almaland.net.almaland.RegisterUser.RegisterUserActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    SharedPreferences.Editor loginPrefsEditor;
    TwitterLoginButton loginTwitter;
    private static final String linkedinUrl = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name," +
            "public-profile-url,picture-url,email-address,picture-urls::(original))";

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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();

        loginTwitter = findViewById(R.id.login_twitter);
        loginTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data);
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        });

        TextView notRegistered = findViewById(R.id.notregistered);
        notRegistered.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });

        EditText emailET = findViewById(R.id.email);
        EditText passwordET = findViewById(R.id.password);

        CheckBox rememberpasswordbox = findViewById(R.id.checkBox);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        boolean savelogin = loginPreferences.getBoolean("savelogin", false);
        if(savelogin)
        {
            String email = loginPreferences.getString("id","");
            String password = loginPreferences.getString("password","");
            emailET.setText(email);
            if (!password.equals("API"))
                passwordET.setText(password);
            rememberpasswordbox.setChecked(true);
            signIn(email, password);
        }

        Button login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            if (!email.isEmpty() && !password.isEmpty())
            {
                if (!rememberpasswordbox.isChecked())
                {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                    new Login().execute(email, password);
                }
                else
                    signIn(email, password);
            }
            else
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        });
    }

    private class Login extends AsyncTask<String,Void,Void> {
        String webPage = "";
        String baseUrl = "http://almaland.net/app/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(LoginActivity.this, "Please Wait!","Logging In!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... strings){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"login.php?email="+strings[0]+"&password="+strings[1];
                myURL = myURL.replaceAll(" ","%20");
                myURL = myURL.replaceAll("\\+","%2B");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\(", "%28");
                myURL = myURL.replaceAll("\\)", "%29");
                myURL = myURL.replaceAll("\\{", "%7B");
                myURL = myURL.replaceAll("\\}", "%7B");
                myURL = myURL.replaceAll("\\]", "%22");
                myURL = myURL.replaceAll("\\[", "%22");
                url = new URL(myURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(webPage.equals("success"))
            {
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
            else
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Linkedin
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        loginTwitter.onActivityResult(requestCode, resultCode, data);
    }

    private void handleTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        signIn(user.getEmail(), "API");
                    }
                    else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void signIn(String email, String password) {
        loginPrefsEditor.putBoolean("savelogin", true);
        loginPrefsEditor.putString("id", email);
        loginPrefsEditor.putString("password", password);
        loginPrefsEditor.apply();
        new Login().execute(email, password);
    }

    public void loginLinkedin(View view){
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        linkededinApiHelper();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    }
                }, true);
    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, linkedinUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                JSONObject response = result.getResponseDataAsJson();
                try {
                    String email = response.get("emailAddress").toString();
                    signIn(email, "API");

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                Toast.makeText(LoginActivity.this, "Linkedin API Error" + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}