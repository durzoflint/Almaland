package almaland.net.almaland.RegisterUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 03-Mar-18.
 */

public class ContactDetailsFragment extends Fragment{

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);
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
        return rootView;
    }

    boolean check() {
        /*String data[] = RegisterUserActivity.data;
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
        TextView dobTV = rootView.findViewById(R.id.dob);
        data[8] = dobTV.getText().toString();
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
        RadioGroup gender = rootView.findViewById(R.id.gender);
        int radioButtonId = gender.getCheckedRadioButtonId();
        if (radioButtonId == -1)
        {
            Toast.makeText(getContext(), "Please Select a Gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        RadioButton userGenderRB = rootView.findViewById(radioButtonId);
        data[9] = userGenderRB.getText().toString();*/
        return true;
    }

}