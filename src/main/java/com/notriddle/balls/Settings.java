package com.notriddle.balls;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class Settings extends PreferenceActivity {
    @Override public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        addPreferencesFromResource(R.xml.settings);
    }
};

