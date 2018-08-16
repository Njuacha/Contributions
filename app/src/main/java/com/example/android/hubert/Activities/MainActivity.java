package com.example.android.hubert.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.android.hubert.Adapters.ContributionsAdapter;
import com.example.android.hubert.Adapters.MembersAdapter;
import com.example.android.hubert.DatabaseClasses.A_list;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.R;
import com.example.android.hubert.View_model_classes.ContributionsViewModel;
import com.example.android.hubert.View_model_classes.MembersViewModel;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mDb = AppDatabase.getDatabaseInstance(this);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ContributionsAdapter.ItemClickListerners  {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final int MEMBERS_SECTION = 1 ;
        private static final int CONTRIBUTIONS_SECTION = 2;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.tv_empty);
            // Declare and instantiate recycler view
            final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            // Set the layout of the recycler view to be a linear la
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            // Declare, instantiate and a divider that would separate items in recycler view
            DividerItemDecoration decoration = new DividerItemDecoration(getContext().getApplicationContext(), VERTICAL);
            recyclerView.addItemDecoration(decoration);
            // Get the section number
            int sectionNumb = getArguments().getInt(ARG_SECTION_NUMBER);
            // Use sectionNumb in a switch
            switch(sectionNumb){
                case MEMBERS_SECTION:
                    // Declare and instantiate Members Adapter
                    final MembersAdapter membersAdapter = new MembersAdapter(getContext());
                    recyclerView.setAdapter(membersAdapter);
                    // Instantiates view model which provides list of members data
                    MembersViewModel membView_model = ViewModelProviders.of(this).get(MembersViewModel.class);
                    membView_model.getMembers().observe(this, new Observer<List<Member>>() {
                        @Override
                        public void onChanged(@Nullable List<Member> members) {
                            if (members.size() == 0){
                                recyclerView.setVisibility(View.INVISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText(R.string.no_list_available);
                            }else{
                                recyclerView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.INVISIBLE);
                                membersAdapter.setMembers(members);
                            }
                        }
                    });
                    break;
                case CONTRIBUTIONS_SECTION:
                    // Declare and instantiate Contributions Adapter
                    final ContributionsAdapter contributionsAdapter = new ContributionsAdapter(getContext(),this);
                    // Set the adapter to the recycler view
                    recyclerView.setAdapter(contributionsAdapter);
                    // Instantiates view model which provides list of contributions data
                    ContributionsViewModel contViewModel = ViewModelProviders.of(this).get(ContributionsViewModel.class);
                    contViewModel.getLists().observe(this, new Observer<List<A_list>>() {
                        @Override
                        public void onChanged(@Nullable List<A_list> a_lists) {
                            if (a_lists.size() == 0){
                                recyclerView.setVisibility(View.INVISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText(R.string.no_list_available);
                            }else{
                                recyclerView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.INVISIBLE);
                                contributionsAdapter.setListEntries(a_lists);
                            }
                        }
                    });

                    break;

            }
            return rootView;
        }


        @Override
        public void onItemCLicked(int itemId, String name) {

        }

        @Override
        public void onItemLongClicked(int itemId, String name) {

        }

        @Override
        public void onOptionTextViewClicked(int itemId, String name, View view) {

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
