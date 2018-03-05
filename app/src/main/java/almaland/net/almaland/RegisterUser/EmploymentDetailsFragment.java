package almaland.net.almaland.RegisterUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 05-Mar-18.
 */

public class EmploymentDetailsFragment extends Fragment {

    View rootView;
    ArrayList<String> job;
    ArrayList<String> jobId;
    ArrayList<String> role;
    ArrayList<String> roleId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_employment_details, container, false);
        Button prev = rootView.findViewById(R.id.prev);
        prev.setOnClickListener(view -> {
            int id = RegisterUserActivity.id;
            ViewPager viewPager = getActivity().findViewById(id);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        });
        Button next = rootView.findViewById(R.id.next);
        next.setOnClickListener(view -> {
            int id = RegisterUserActivity.id;
            ViewPager viewPager = getActivity().findViewById(id);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        });
        new FetchJob().execute();
        new FetchRole().execute();
        return rootView;
    }

    private class FetchJob extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchjobdomain.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                notConnected = true;
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
            if(!webPage.isEmpty())
            {
                job = new ArrayList<>();
                jobId = new ArrayList<>();
                while(webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    job.add(name);
                    jobId.add(id);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, job);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner jobSpinner = rootView.findViewById(R.id.jobdomain);
                jobSpinner.setAdapter(dataAdapter);
            }
        }
    }

    private class FetchRole extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchrole.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String data;
                while ((data=br.readLine()) != null)
                    webPage=webPage+data;
            }
            catch (IOException e)
            {
                notConnected = true;
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
            if(!webPage.isEmpty())
            {
                role = new ArrayList<>();
                roleId = new ArrayList<>();
                while(webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    role.add(name);
                    roleId.add(id);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, role);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner roleSpinner = rootView.findViewById(R.id.role);
                roleSpinner.setAdapter(dataAdapter);
            }
        }
    }

}
