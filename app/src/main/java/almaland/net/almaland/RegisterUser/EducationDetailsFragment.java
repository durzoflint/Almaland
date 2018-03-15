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
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 04-Mar-18.
 */

public class EducationDetailsFragment extends Fragment {

    View rootView;
    ArrayList<String> colleges;
    ArrayList<String> collegesIds;
    ArrayList<String> major;
    ArrayList<String> majorIds;
    ArrayList<String> branch;
    ArrayList<String> branchIds;
    ArrayList<String> enrtyDate;
    ArrayList<String> graduationDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_education_details, container, false);
        Button prev = rootView.findViewById(R.id.prev);
        prev.setOnClickListener(view -> {
            int id = RegisterUserActivity.id;
            ViewPager viewPager = getActivity().findViewById(id);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        });
        Button next = rootView.findViewById(R.id.next);
        next.setOnClickListener(view -> {
            if (check())
            {
                int id = RegisterUserActivity.id;
                ViewPager viewPager = getActivity().findViewById(id);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });
        new FetchCollege().execute();
        new FetchMajor().execute();
        new FetchBranch().execute();
        enrtyDate = new ArrayList<>();
        graduationDate = new ArrayList<>();
        fillDates(enrtyDate);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, enrtyDate);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner entrySpinner = rootView.findViewById(R.id.entry);
        entrySpinner.setAdapter(dataAdapter);
        fillDates(graduationDate);
        dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, graduationDate);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner graduationSpinner = rootView.findViewById(R.id.graduation);
        graduationSpinner.setAdapter(dataAdapter);
        return rootView;
    }

    private boolean check() {
        String data[] = RegisterUserActivity.data;
        EditText specializationET = rootView.findViewById(R.id.specialization);
        data[13] = specializationET.getText().toString();
        /*if (data[13].isEmpty())
        {
            Toast.makeText(getContext(), "Specialization cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    void fillDates(ArrayList<String> arrayList) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = 1991; i <= currentYear; i++)
            arrayList.add(i+"");
    }

    private class FetchCollege extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchcollege.php");
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
                colleges = new ArrayList<>();
                collegesIds = new ArrayList<>();
                while(webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    collegesIds.add(id);
                    colleges.add(name);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, colleges);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner collegeSpinner = rootView.findViewById(R.id.college);
                collegeSpinner.setAdapter(dataAdapter);
            }
        }
    }

    private class FetchMajor extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchmajor.php");
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
                major = new ArrayList<>();
                majorIds = new ArrayList<>();
                while(webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    majorIds.add(id);
                    major.add(name);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, major);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner majorSpinner = rootView.findViewById(R.id.major);
                majorSpinner.setAdapter(dataAdapter);
            }
        }
    }

    private class FetchBranch extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchbranch.php");
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
                branch = new ArrayList<>();
                branchIds = new ArrayList<>();
                while(webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    branchIds.add(id);
                    branch.add(name);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, branch);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner branchSpinner = rootView.findViewById(R.id.branch);
                branchSpinner.setAdapter(dataAdapter);
            }
        }
    }
}
