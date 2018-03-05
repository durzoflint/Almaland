package almaland.net.almaland.RegisterUser;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import almaland.net.almaland.R;

public class RegisterUserActivity extends AppCompatActivity {

    static String data[];
    static int id;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.beginFakeDrag();
        mViewPager.setOffscreenPageLimit(4);
        data = new String[10];
    }

    @Override
    public void onBackPressed() {
        int pageNumber = mViewPager.getCurrentItem();
        if(pageNumber == 0)
            super.onBackPressed();
        else
            mViewPager.setCurrentItem(pageNumber - 1, true);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            id = mViewPager.getId();

            switch (position) {
                case 0:
                    return new PersonalDetailsFragment();
                case 1:
                    return new ContactDetailsFragment();
                case 2:
                    return new EducationDetailsFragment();
                case 3:
                    return new EmploymentDetailsFragment();
                case 4:
                    return new PasswordFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }
}
