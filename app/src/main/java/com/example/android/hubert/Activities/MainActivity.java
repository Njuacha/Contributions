package com.example.android.hubert.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.android.hubert.DialogFragments.NameDialog;
import com.example.android.hubert.R;
import com.example.android.hubert.SectionsPagerAdapter;
import com.example.android.hubert.Utils.MyMobileAds;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static final String MEMBERS_TAB = "members tab";
    public static final String CONTRIBUTIONS_TAB = "contributions tab";
    public static final String EXTRA_TAB = "tab";
    private static final int RC_SIGN_IN = 123;

    public static int mFabState;
    private FloatingActionButton mFab;
    private TabLayout tabLayout;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        // Intialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                } else {
                    // user is not signed in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()

                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

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

        tabLayout = findViewById(R.id.tabs);

        mFabState = tabLayout.getSelectedTabPosition();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFabState = position;
                if (mFabState == 1) {
                    mFab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_action_add_list));
                } else {
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
                openNameDialog();
            }
        });

        // Initialize MyMobileAds
        AdView adView = findViewById(R.id.adView1);
        MyMobileAds.loadAdIntoAdView(this,adView);

        // Initialize the NavigationView
        mNavigationView = findViewById(R.id.nav_view);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            if (resultCode == RESULT_OK){
            } else if (resultCode == RESULT_CANCELED){
                
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Sign Out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("Sign Out")){
            AuthUI.getInstance().signOut(this);
        }

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add listener to respond to navigation item clicked
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        return true;
                    }
                }
        );
        
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void openNameDialog() {
        int position = tabLayout.getSelectedTabPosition();
        String tab ;
        if (position == 0){
            tab = MEMBERS_TAB;
        }else if (position == 1){
            tab = CONTRIBUTIONS_TAB;
        }else {
            return;
        }
        NameDialog dialog = new NameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB, tab);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "edit_list_name_dialog");
    }

}
