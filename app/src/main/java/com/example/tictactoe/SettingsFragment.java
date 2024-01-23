package com.example.tictactoe;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
        EditTextPreference one = (EditTextPreference) findPreference("player_one");
        EditTextPreference two = (EditTextPreference) findPreference("player_two");
        if (one != null) {
            one.setSummary(MainActivity.getActivity().getPlayerOneName());
            one.setText(MainActivity.getActivity().getPlayerOneName());
            one.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    String s = (String) newValue;
                    MainActivity.getActivity().setPlayerOneName(s);
                    Log.d(">>>player one", s);
                    EditTextPreference pref = (EditTextPreference) preference;
                    pref.setSummary(s);
                    return false;
                }
            });
        }
        if (two != null) {
            two.setSummary(MainActivity.getActivity().getPlayerTwoName());
            two.setText(MainActivity.getActivity().getPlayerTwoName());
            two.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull @NotNull Preference preference, Object newValue) {
                    String s = (String) newValue;
                    MainActivity.getActivity().setPlayerTwoName(s);
                    Log.d(">>>player one", s);
                    EditTextPreference pref = (EditTextPreference) preference;
                    pref.setSummary(s);
                    return false;
                }
            });
        }
    }


}