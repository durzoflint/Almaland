package almaland.net.almaland.RegisterUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import almaland.net.almaland.R;

/**
 * Created by Abhinav on 04-Mar-18.
 */

public class EducationDetailsFragment extends Fragment {
    View rootView;

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
            int id = RegisterUserActivity.id;
            ViewPager viewPager = getActivity().findViewById(id);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        });
        return rootView;
    }
}
