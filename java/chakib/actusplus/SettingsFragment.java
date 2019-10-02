package chakib.actusplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;


/**
 * Created by Chakib on 07/08/2017.
 */



public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
        , Preference.OnPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);

            if(!(p instanceof CheckBoxPreference))
            {
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }
        }
        Preference preference = findPreference(getString(R.string.pref_load_key));
        preference.setOnPreferenceChangeListener(this);

    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);

            if(prefIndex >=0)
            {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }else if(preference instanceof EditTextPreference)
        {
            preference.setSummary(value);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference != null  )
            if(!(preference instanceof CheckBoxPreference))
            {
                String value = sharedPreferences.getString(preference.getKey(),"");

                setPreferenceSummary(preference,value);
            }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //convert the preference into a number and then checks that the number is between 0 and 3 , then test
        Toast error = Toast.makeText(getContext(), "Veuillez sélectionnez un nombre compris entre 2 et 10", Toast.LENGTH_SHORT);
        Toast error1 = Toast.makeText(getContext(), "S'il vous plaît, mettez une adresse email valide", Toast.LENGTH_SHORT);

        String sizeKey = getString(R.string.pref_load_key);
        if (preference.getKey().equals(sizeKey)) {
            String stringSize = ((String) (newValue)).trim();
            if (stringSize.equals("")) stringSize = "2";
            try {
                int size = Integer.parseInt(stringSize);
                if (size > 10 || size < 2) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                error.show();
                return false;
            }
        }
        if (preference.getKey().equals(getString(R.string.pref_mail_key))) {
            String stringmail = ((String) (newValue)).trim();
            if(isValidEmail(stringmail))
            {
                return true;
            }else{
                error1.show();
                return false;
            }
        }
        return true;
    }

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
