package almaland.net.almaland;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        EditText emailET = findViewById(R.id.email);
        EditText passwordET = findViewById(R.id.password);

        final CheckBox rememberpasswordbox = findViewById(R.id.checkBox);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        boolean savelogin = loginPreferences.getBoolean("savelogin", false);
        if(savelogin)
        {
            emailET.setText(loginPreferences.getString("id",""));
            passwordET.setText(loginPreferences.getString("password",""));
            rememberpasswordbox.setChecked(true);
        }

        Button login = findViewById(R.id.login);
        login.setOnClickListener(view -> {
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            if (!email.isEmpty() && !password.isEmpty())
            {
                if (rememberpasswordbox.isChecked()) {
                    loginPrefsEditor.putBoolean("savelogin", true);
                    loginPrefsEditor.putString("id", email);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                new Login().execute(email, password);
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
                //startActivity(new Intent(getContext(), CheckMailActivity.class));
            }
            else
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
    }
}