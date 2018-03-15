package almaland.net.almaland.RegisterUser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 05-Mar-18.
 */

public class PasswordFragment extends Fragment {

    View rootView;
    String data[] = RegisterUserActivity.data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_password, container, false);
        Button prev = rootView.findViewById(R.id.prev);
        prev.setOnClickListener(view -> {
            int id = RegisterUserActivity.id;
            ViewPager viewPager = getActivity().findViewById(id);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        });
        Button submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            EditText passwordET = rootView.findViewById(R.id.password);
            EditText password2ET = rootView.findViewById(R.id.password2);
            String password = passwordET.getText().toString();
            String password2 = password2ET.getText().toString();
            if (!password.equals(password2) || password.isEmpty() || password2.isEmpty())
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            else
            {
                data[25] = password;
                Log.d("Abhinav", Arrays.toString(data));
                /*boolean flag = true;
                for (int i=0;i<26;i++)
                    if (data[i]==null)
                        flag = false;
                if (flag)*/
                    new RegisterUser().execute();
            }
        });
        return rootView;
    }

    private class RegisterUser extends AsyncTask<Void,Void,Void> {
        String webPage = "";
        String baseUrl = "http://almaland.net/app/";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getContext(), "Please Wait!","Registering!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                String myURL = baseUrl+"register.php?firstname="+data[0]+"&lastname="+data[1]+"&username="+data[2]+
                        "&fathername="+data[3]+"&location="+data[4]+"&country="+data[5]+"&state="+data[6]+"&city="+data[7]+
                        "&dob="+data[8]+"&gender="+data[9]+"&email="+data[10]+"&phone="+data[11]+"&phonevisible="+data[12]+
                        "&workphone="+data[13]+"&college="+data[14]+"&major="+data[15]+"&specialization="+data[16]+
                        "&branch="+data[17]+"&entry="+data[18]+"&graduation="+data[19]+"&employer="+data[20]+
                        "&domain="+data[21]+"&role="+data[22]+"&dateempfrom="+data[23]+"&dateempto="+data[24]+
                        "&password="+data[25];
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
                Log.d("Abhinav", myURL);
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
                Toast.makeText(getContext(), "Event Save Success", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Some error Occurred: "+webPage, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
