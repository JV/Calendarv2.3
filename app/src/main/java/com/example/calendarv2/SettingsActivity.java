package com.example.calendarv2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useNotifications = prefs.getBoolean("key_notifications", false);
        Log.d("NotifiSet", String.valueOf(useNotifications));

    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getPreferenceManager().getSharedPreferences();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        prefs = getPreferenceManager().getSharedPreferences();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useNotifications = prefs.getBoolean("key_notifications", false);
        Log.d("NotifChange", String.valueOf(useNotifications));
    }
}