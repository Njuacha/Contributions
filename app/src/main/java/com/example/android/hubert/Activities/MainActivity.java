package com.example.android.hubert.Activities;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;


import com.example.android.hubert.DialogFragments.NameDialog;
import com.example.android.hubert.R;
import com.example.android.hubert.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {

    public static final String MEMBERS_TAB = "members tab";
    public static final String CONTRIBUTIONS_TAB = "contritutions tab";
    public static final String EXTRA_TAB = "tab";

    public static int mFabState = 0;
    private FloatingActionButton mFab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFabState = position;
                if(mFabState == 1){
                    mFab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_action_add_list));
                }else {
                    mFab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_action_add_person));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mFab = findViewById(R.id.fab);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 stands for first page which is members page
                if(mFabState == 0){
                    openNameDialog(MEMBERS_TAB);
                }else if (mFabState == 1){
                    openNameDialog(CONTRIBUTIONS_TAB);
                }
            }
        });

    }


    private void openNameDialog(String tab) {

        NameDialog dialog = new NameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB, tab);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "edit_list_name_dialog");
    }




}
