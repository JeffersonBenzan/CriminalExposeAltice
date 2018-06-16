package com.jefferson.criminalexposealtice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ActionBar toolbar;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = getSupportActionBar();
//
//        toolbar.setTitle("Crime Report");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new CrimeViewFragment());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new CrimeViewFragment();
//                    toolbar.setTitle(R.string.title_home);
                    break;
                case R.id.navigation_add_crime_report:
                    fragment = new AddCrimeReportFragment();
//                    toolbar.setTitle(R.string.title_add_crime);
                    break;
                case R.id.navigation_personal:

                    fragment = new PersonalViewFragment();

//                    toolbar.setTitle(R.string.title_personal);
                    break;
                default:
                    fragment = new CrimeViewFragment();
//                    toolbar.setTitle(R.string.title_home);
                    break;
            }

            loadFragment(fragment);
            return true;
        }
    };




    private void loadFragment(Fragment fragment) {
        // load fragment
        if (fragment==null) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
