package com.englishvocabularygame.evog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

/**
 * Created  on 4.05.2017.
 */

public class SettingsActivity extends Activity {
    //    public int soruSayisiString;
    final String userStats = "userStats";
    private String vibrationPreference = "vibrationPreference";
    private SharedPreferences stats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Activityler", "SettingsActivity started");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        stats = getSharedPreferences(userStats, MODE_PRIVATE);
        ImageView closeMark = findViewById(R.id.cancel_mark_settings);
        vibrationStatus();
        sendEmail();
        closeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void sendEmail() {
        ImageView fab = findViewById(R.id.floating_email_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"englishvocabularygame@gmail.com"}); // recipients
                startActivity(emailIntent);
            }
        });

    }

    private void vibrationStatus() {
        final CheckBox vibrationStatusCheckBox = findViewById(R.id.user_vibration);
        if (!stats.getBoolean(vibrationPreference, true)) {
            vibrationStatusCheckBox.setChecked(false);
        }
        final SharedPreferences.Editor vibrationEditor = stats.edit();
        vibrationStatusCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vibrationEditor.putBoolean(vibrationPreference, true);
                    vibrationEditor.apply();
                } else {
                    vibrationEditor.putBoolean(vibrationPreference, false);
                    vibrationEditor.apply();
                }
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//    }

}
