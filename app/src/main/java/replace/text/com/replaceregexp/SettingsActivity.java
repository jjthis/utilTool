package replace.text.com.replaceregexp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.R;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static org.mozilla.javascript.tools.shell.Global.readFile;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(replace.text.com.replaceregexp.R.xml.pref_settings);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference.getKey().equals("clearButton")) {
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
            ClearEditText.setUseClearIcon(mPref.getBoolean("clearButton", true));
        } else if(preference.getKey().equals("load")){
            startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("text/*"), "Choose an text"), 1);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            String[] colors = {"Replace", "Search", "Encode", "Decode", "Source", "Rhino"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick a View");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.editArr[which].setText(readFile(data.getData()).toString());
                    MainActivity.vp.setCurrentItem(which);
                    finish();
                }
            });
            builder.show();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        }
    }

    public StringBuilder readFile(Uri ur) {
        StringBuilder texts = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(ur.getPath())));
            String line;

            while ((line = br.readLine()) != null) {
                texts.append(line).append('\n');
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            //You'll need to add proper error handling here
        }
        return texts;
    }
}

