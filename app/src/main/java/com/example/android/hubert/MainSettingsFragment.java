package com.example.android.hubert;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by hubert on 7/10/18.
 */

public class MainSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
         addPreferencesFromResource(R.xml.pref_association_name);
    }
}
