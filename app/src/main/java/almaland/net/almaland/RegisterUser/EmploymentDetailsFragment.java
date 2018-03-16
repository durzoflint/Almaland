package almaland.net.almaland.RegisterUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
            if (check())
            {
                int id = RegisterUserActivity.id;
                ViewPager viewPager = getActivity().findViewById(id);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });
        Button dateOfEmploymentFrom = rootView.findViewById(R.id.dateofemploymentfrom);
        dateOfEmploymentFrom.setOnClickListener(view -> {
            getDate(view);
        });
        Button dateOfEmploymentTo = rootView.findViewById(R.id.dateofemploymentto);
        dateOfEmploymentTo.setOnClickListener(view -> {
            getDate(view);
        });
        new FetchJob().execute();
        new FetchRole().execute();
        return rootView;
    }

    boolean check() {
        String data[] = RegisterUserActivity.data;
        EditText employerET = rootView.findViewById(R.id.employer);
        data[20] = employerET.getText().toString();
        Spinner jobDomainSinner = rootView.findViewById(R.id.jobdomain);
        data[21] = RegisterUserActivity.getIdsFromSpinnerValues(job, jobId, jobDomainSinner.getSelectedItem().toString());
        Spinner roleSinner = rootView.findViewById(R.id.role);
        data[22] = RegisterUserActivity.getIdsFromSpinnerValues(role, roleId, roleSinner.getSelectedItem().toString());
        Button dateOfEmploymentFrom = rootView.findViewById(R.id.dateofemploymentfrom);
        data[23] = dateOfEmploymentFrom.getText().toString();
        Button dateOfEmploymentTo = rootView.findViewById(R.id.dateofemploymentto);
        data[24] = dateOfEmploymentTo.getText().toString();
        if (data[20].isEmpty())
        {
            Toast.makeText(getContext(), "Employer cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data[23].equalsIgnoreCase("Date of Employment From"))
        {
            Toast.makeText(getContext(), "Please Select a Valid Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (data[24].equalsIgnoreCase("Date of Employment To"))
        {
            Toast.makeText(getContext(), "Please Select a Valid Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void getDate(View view) {
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
