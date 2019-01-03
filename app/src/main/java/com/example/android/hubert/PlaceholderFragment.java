package com.example.android.hubert;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hubert.Activities.ContributionSummaryActivity;
import com.example.android.hubert.Activities.DisplayAList;
import com.example.android.hubert.Activities.MemberActivity;
import com.example.android.hubert.Activities.MemberSummaryActivity;
import com.example.android.hubert.Adapters.ContributionsAdapter;
import com.example.android.hubert.Adapters.MembersAdapter;
import com.example.android.hubert.DatabaseClasses.Alist;
import com.example.android.hubert.DatabaseClasses.AppDatabase;
import com.example.android.hubert.DatabaseClasses.Member;
import com.example.android.hubert.DialogFragments.NameDialog;
import com.example.android.hubert.ViewModels.ContributionsViewModel;
import com.example.android.hubert.ViewModels.MembersViewModel;

import java.util.List;
import java.util.Objects;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.example.android.hubert.Activities.MainActivity.CONTRIBUTIONS_TAB;
import static com.example.android.hubert.Activities.MainActivity.EXTRA_TAB;
import static com.example.android.hubert.Activities.MainActivity.MEMBERS_TAB;
import static com.example.android.hubert.Activities.MainActivity.mFabState;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements ContributionsAdapter.ItemClickListeners, MembersAdapter.ItemClickListeners {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String EXTRA_EDIT_ITEM = "edit item";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int MEMBERS_SECTION = 1;
    private static final int CONTRIBUTIONS_SECTION = 2;
    public static final String LIST_EXTRA = "list";
    public static final String EXTRA_MEMBER = "member";
    private static AppDatabase mDb;

    private MembersAdapter mMembersAdapter;
    private ContributionsAdapter mContributionsAdapter;


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
        setHasOptionsMenu(true);
        // Instantiate the database i would be using
        mDb = AppDatabase.getDatabaseInstance(getContext());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = rootView.findViewById(R.id.tv_empty);
        // Declare and instantiate recycler view
        final RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        // Set the layout of the recycler view to be a linear la
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Declare, instantiate and a divider that would separate items in recycler view
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        // Get the section number
        int sectionNumb = getArguments().getInt(ARG_SECTION_NUMBER);
        // Use sectionNumb in a switch
        // Instantiate Members Adapter
        mMembersAdapter = new MembersAdapter(getContext(), this);
        // Instantiate Contributions Adapter
        mContributionsAdapter = new ContributionsAdapter(getContext(), this);

        switch (sectionNumb) {
            case MEMBERS_SECTION:

                recyclerView.setAdapter(mMembersAdapter);
                // Instantiates view model which provides list of members data
                MembersViewModel mMembView_model = ViewModelProviders.of(this).get(MembersViewModel.class);
                mMembView_model.getMembers().observe(this, new Observer<List<Member>>() {
                    @Override
                    public void onChanged(@Nullable List<Member> members) {
                        if (members.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(R.string.no_members_so_add);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            textView.setText(R.string.no_members_so_add);
                            mMembersAdapter.setMembers(members);
                        }
                    }
                });



                break;
            case CONTRIBUTIONS_SECTION:

                // Set the adapter to the recycler view
                recyclerView.setAdapter(mContributionsAdapter);
                // Instantiates view model which provides list of contributions data
                ContributionsViewModel mContribViewModel = ViewModelProviders.of(this).get(ContributionsViewModel.class);
                mContribViewModel.getLists().observe(this, new Observer<List<Alist>>() {
                    @Override
                    public void onChanged(@Nullable List<Alist> lists) {
                        if (lists.size() == 0) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(R.string.no_contrib_so_add);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            textView.setText(R.string.no_contrib_so_add);
                            mContributionsAdapter.setListEntries(lists);
                        }
                    }
                });


                break;

        }
        return rootView;
    }


    @Override
    public void onContributionListClicked(Alist a_list) {
        Intent intent = new Intent(getActivity(), DisplayAList.class);
        intent.putExtra(LIST_EXTRA,a_list);
        startActivity(intent);
    }

    @Override
    public void onContributionOptionViewClicked(final Alist alist, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.list);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        openNameDialog(CONTRIBUTIONS_TAB,alist);
                        break;
                    case R.id.action_details:
                        Intent showSummaryIntent = new Intent(getActivity(), ContributionSummaryActivity.class);
                        showSummaryIntent.putExtra(LIST_EXTRA, alist);
                        startActivity(showSummaryIntent);
                        break;
                    case R.id.action_delete:
                        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.aListDao().deleteAList(alist.getListId());
                            }
                        });
                        break;

                }
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onMemberOptionViewClicked(final Member member, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(),view);
        popupMenu.inflate(R.menu.list);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_edit:
                        openNameDialog(MEMBERS_TAB,member);
                        break;
                    case R.id.action_details:
                        Intent intent = new Intent(getActivity(),MemberSummaryActivity.class);
                        startActivity(intent.putExtra(EXTRA_MEMBER,member));
                        break;
                    case R.id.action_delete:
                        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.memberDao().deleteMember(member);
                            }
                        });
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onMemberClicked(Member member) {
        startActivity(new Intent(getActivity(),MemberActivity.class).putExtra(EXTRA_MEMBER, member));
    }

    private void openNameDialog(String tab, Parcelable itemToEdit){

        NameDialog dialog = new NameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TAB,tab);
        bundle.putParcelable(EXTRA_EDIT_ITEM,itemToEdit);
        dialog.setArguments(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.show(Objects.requireNonNull(getFragmentManager()), "name dialog");
        }else{
            dialog.show(getFragmentManager(), "name dialog");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);


        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (mFabState == 0){
                    // If there is no member yet then there is nothing to search
                    if (mMembersAdapter.getItemCount() == 0) return false;
                    mMembersAdapter.saveOriginalList();

                }else if (mFabState == 1){
                    // if there is no contribution yet then there is nothing to search
                    if (mContributionsAdapter.getItemCount() == 0) return false;
                    mContributionsAdapter.saveOriginalList();
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (mFabState == 0){
                    mMembersAdapter.restoreOriginalList();
                }else if (mFabState == 1){
                    mContributionsAdapter.restoreOriginalList();
                }
                return true;
            }
        });

        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("section Number",String.valueOf(mFabState));
                if (mFabState == 0){
                    mMembersAdapter.searchMembersStartingWith(newText);
                }else if (mFabState == 1){
                    mContributionsAdapter.searchListStartingWith(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }





}



