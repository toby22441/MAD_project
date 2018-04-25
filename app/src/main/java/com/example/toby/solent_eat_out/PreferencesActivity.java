package com.example.toby.solent_eat_out;

import android.os.Bundle;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity   extends PreferenceActivity {

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
