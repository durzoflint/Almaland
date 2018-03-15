package almaland.net.almaland.RegisterUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 05-Mar-18.
 */

public class PasswordFragment extends Fragment {

    View rootView;

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
                String data[] = RegisterUserActivity.data;
                data[17] = password;
                //Todo: Send data to server
            }
        });
        return rootView;
    }
}
