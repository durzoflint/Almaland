package almaland.net.almaland.RegisterUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 03-Mar-18.
 */

public class PersonalDetailsFragment extends Fragment{

    View rootView;
    ArrayList<String> country;
    ArrayList<String> countryId;
    ArrayList<String> state;
    ArrayList<String> stateId;
    ArrayList<String> city;
    ArrayList<String> cityId;
    String selectedCountryId, selectedStateId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_personal_details, container, false);
        Button dob = rootView.findViewById(R.id.dob);
        dob.setOnClickListener(view -> {
            LayoutInflater inflaterDatePick = LayoutInflater.from(getContext());
            final View pickDate = inflaterDatePick.inflate(R.layout.layout_date_pick, null);
            new AlertDialog.Builder(getContext())
                    .setView(pickDate)
                    .setIcon(android.R.drawable.ic_menu_agenda)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        DatePicker datePicker = pickDate.findViewById(R.id.pickdate);
                        String year = "" + datePicker.getYear();
                        String month = "" + (datePicker.getMonth() + 1);
                        String day = "" + datePicker.getDayOfMonth();
                        if (month.length() == 1)
                            month = "0" + month;
                        if (day.length() == 1)
                            day = "0" + day;
                        String date = day +" - "+ month +" - "+ year;
                        TextView textView = (TextView) view;
                        textView.setText(date);
                    })
                    .create().show();
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
        new FetchCountry().execute();
        return rootView;
    }

    boolean check() {
        String data[] = RegisterUserActivity.data;
        TextView firstNameTV = rootView.findViewById(R.id.firstname);
        data[0] = firstNameTV.getText().toString();
        TextView lastNameTV = rootView.findViewById(R.id.lastname);
        data[1] = lastNameTV.getText().toString();
        TextView usernameTV = rootView.findViewById(R.id.username);
        data[2] = usernameTV.getText().toString();
        TextView fathernameTV = rootView.findViewById(R.id.fathername);
        data[3] = fathernameTV.getText().toString();
        TextView locationTV = rootView.findViewById(R.id.location);
        data[4] = locationTV.getText().toString();
        Spinner countrySpinner = rootView.findViewById(R.id.country);
        data[5] = countrySpinner.getSelectedItem().toString();
        Spinner stateSpinner = rootView.findViewById(R.id.state);
        data[6] = stateSpinner.getSelectedItem().toString();
        Spinner citySpinner = rootView.findViewById(R.id.city);
        data[7] = citySpinner.getSelectedItem().toString();
        Button dobTV = rootView.findViewById(R.id.dob);
        data[8] = dobTV.getText().toString();
        RadioGroup gender = rootView.findViewById(R.id.gender);
        int radioButtonId = gender.getCheckedRadioButtonId();
        if (radioButtonId == -1)
        {
            Toast.makeText(getContext(), "Please Select a Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        RadioButton userGenderRB = rootView.findViewById(radioButtonId);
        data[9] = userGenderRB.getText().toString();
        if (data[0].isEmpty())
        {
            Toast.makeText(getContext(), "First Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data[1].isEmpty())
        {
            Toast.makeText(getContext(), "Last Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data[2].isEmpty())
        {
            Toast.makeText(getContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data[8].equalsIgnoreCase("Date of Birth"))
        {
            Toast.makeText(getContext(), "Please Select Date of Birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class FetchCountry extends AsyncTask<Void,Void,Void> {
        boolean notConnected = false;
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(Void... voids){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchcountry.php");
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
            if (notConnected)
            {
                Toast.makeText(getActivity(), "Connection Error. Please Connect to the Internet and Try Again", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
            else
            {
                if(!webPage.isEmpty())
                {
                    country = new ArrayList<>();
                    countryId = new ArrayList<>();
                    while (webPage.contains("<br>"))
                    {
                        int brI = webPage.indexOf("<br>");
                        String id = webPage.substring(0, brI);
                        webPage = webPage.substring(brI + 4);
                        brI = webPage.indexOf("<br>");
                        String name = webPage.substring(0, brI);
                        webPage = webPage.substring(brI + 4);
                        country.add(name);
                        countryId.add(id);
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, country);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner countrySpinner = rootView.findViewById(R.id.country);
                    countrySpinner.setAdapter(dataAdapter);
                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedCountryId = countryId.get(country.indexOf(adapterView.getItemAtPosition(i).toString()));
                            new FetchState().execute(selectedCountryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });
                }
            }
        }
    }

    private class FetchState extends AsyncTask<String,Void,Void> {
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(String... strings){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchstate.php?countryid="+strings[0]);
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
            if(!webPage.isEmpty())
            {
                state = new ArrayList<>();
                stateId = new ArrayList<>();
                while (webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    state.add(name);
                    stateId.add(id);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, state);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner stateSpinner = rootView.findViewById(R.id.state);
                stateSpinner.setAdapter(dataAdapter);
                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedStateId = stateId.get(state.indexOf(adapterView.getItemAtPosition(i).toString()));
                        new FetchCity().execute(selectedCountryId, selectedStateId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }
        }
    }

    private class FetchCity extends AsyncTask<String,Void,Void> {
        String webPage="";
        String baseUrl = "http://almaland.net/app/";
        @Override
        protected Void doInBackground(String... strings){
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(baseUrl+"fetchcity.php?countryid="+strings[0]+"&stateid="+strings[1]);
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
            city = new ArrayList<>();
            cityId = new ArrayList<>();
            if(!webPage.isEmpty())
            {
                while (webPage.contains("<br>"))
                {
                    int brI = webPage.indexOf("<br>");
                    String id = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    brI = webPage.indexOf("<br>");
                    String name = webPage.substring(0, brI);
                    webPage = webPage.substring(brI + 4);
                    city.add(name);
                    cityId.add(id);
                }
            }
            else if (city.size() == 0)
            {
                city.add("No city found in database");
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, city);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner citySpinner = rootView.findViewById(R.id.city);
            citySpinner.setAdapter(dataAdapter);
        }
    }
}