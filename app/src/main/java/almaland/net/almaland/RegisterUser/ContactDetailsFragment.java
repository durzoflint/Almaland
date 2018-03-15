package almaland.net.almaland.RegisterUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        String data[] = RegisterUserActivity.data;
        EditText emailET = rootView.findViewById(R.id.email);
        data[10] = emailET.getText().toString();
        EditText phoneET = rootView.findViewById(R.id.phone);
        data[11] = phoneET.getText().toString();
        EditText workPhoneET = rootView.findViewById(R.id.workphone);
        data[12] = workPhoneET.getText().toString();
        /*if(!(!TextUtils.isEmpty(data[10]) && Patterns.EMAIL_ADDRESS.matcher(data[10]).matches()))
        {
            Toast.makeText(getContext(), "Please Enter a valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(!TextUtils.isEmpty(data[11]) && Patterns.PHONE.matcher(data[11]).matches() && data[11].length() > 9))
        {
            Toast.makeText(getContext(), "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(!TextUtils.isEmpty(data[12]) && Patterns.PHONE.matcher(data[12]).matches() && data[12].length() > 9))
        {
            Toast.makeText(getContext(), "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

}