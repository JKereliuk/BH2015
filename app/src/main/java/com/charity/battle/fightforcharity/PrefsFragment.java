package com.charity.battle.fightforcharity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
/**
 * Created by bge on 15-07-18.
 */
public class PrefsFragment extends PreferenceFragment {//implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

//        onSharedPreferenceChanged(null, "");
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Set up a listener whenever a key changes
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // Set up a listener whenever a key changes
//        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedprefs, String key) {
//        // just update all
//
//        String donation_string = sharedprefs.getString("donation_amount", "DEFAULT");
//
//    }
}