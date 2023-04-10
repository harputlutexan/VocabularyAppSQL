package com.englishvocabularygame.evog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.englishvocabularygame.evog.Statistics.SummeryTabActivity;


/**
 * Created  on 16.11.2016.
 */

public class SummaryActivity extends AppCompatActivity {

    final String userStats = "userStats";
    final String tempUserStats = "tempUserStats";
    final String tempCorrectAnswerPreference = "tempCorrectAnswer";
    final String tempFalseAnswerPreference = "tempFalseAnswer";
    final String tempRemainingTimerPreference = "tempRemainingTimerPreference";
    final String tempScorePreference = "tempScoreCount";
    final String scorePreference = "scoreCount";
    final String questionCountPreference = "questionCount";
    private Boolean adShowed = false;
    InterstitialAd fullScreenAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_page);
        setUserStats();
        final Button returnHomeButton = findViewById(R.id.to_home_button);
        final Button testDetailsButton = findViewById(R.id.detailed_results);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm!=null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                returnHomeButton.setEnabled(false);
                testDetailsButton.setEnabled(false);
            }
        }
        fullScreenAd = new InterstitialAd(this);
        fullScreenAd.setAdUnitId(getString(R.string.interstitial_full_screen_summary_activity));
        AdRequest adRequest = new AdRequest.Builder().build();
        fullScreenAd.loadAd(adRequest);
        fullScreenAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (fullScreenAd.isLoaded()) {
                    fullScreenAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                adShowed=true;
                returnHomeButton.setEnabled(true);
                testDetailsButton.setEnabled(true);
            }
        });

    }


    // shared preference TestFragment da olusturuldu
    private void setUserStats() {
        SharedPreferences loginStats = getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
        SharedPreferences stats = getSharedPreferences(userStats, Context.MODE_PRIVATE);
        int tempCurrentCorrectScore = loginStats.getInt(tempCorrectAnswerPreference, 0);
        ((TextView) findViewById(R.id.correct_answer_count)).setText(String.valueOf(tempCurrentCorrectScore));
        int tempCurrentFalseScore = loginStats.getInt(tempFalseAnswerPreference, 0);
        ((TextView) findViewById(R.id.wrong_answer)).setText(String.valueOf(tempCurrentFalseScore));
        long totalTime = loginStats.getLong(tempRemainingTimerPreference, 0);
        double tempAverageTime = (totalTime) / ((double) (tempCurrentCorrectScore + tempCurrentFalseScore));
        String tempAverageTimeString = String.format("%.2f", tempAverageTime);
        String tempAverageTimeStringWithTime = tempAverageTimeString + getString(R.string.second);
        ((TextView) findViewById(R.id.average_time)).setText(tempAverageTimeStringWithTime);
        int score = loginStats.getInt(tempScorePreference, 0);
        ((TextView) findViewById(R.id.highest_score)).setText(String.valueOf(score));
        int totalScore = getSharedPreferences(userStats, Context.MODE_PRIVATE).getInt(scorePreference, 0);
        ((TextView) findViewById(R.id.total_score)).setText(String.valueOf(totalScore));
        int level = (totalScore / 1000) + 1;
        ((TextView) findViewById(R.id.summary_level)).setText(String.valueOf(level));
        stats.edit().remove(questionCountPreference).apply();
        loginStats.edit().clear().apply();
    }

    public void returnHome(View view) {
        view = findViewById(R.id.to_home_button);
        SharedPreferences stats = getSharedPreferences(userStats, Context.MODE_PRIVATE);
        stats.edit().remove(questionCountPreference).apply();
        // test activitynin icerisinde metot gelistirip secenek ile correctanswer karsilastirilmali
        // veya esit ise bu activity degil ise baska aktivite acilabilir
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }

    public void startDetailedResults(View view){
        view = findViewById(R.id.detailed_results);
        SharedPreferences stats = getSharedPreferences(userStats, Context.MODE_PRIVATE);
        stats.edit().remove(questionCountPreference).apply();

        // test activitynin icerisinde metot gelistirip secenek ile correctanswer karsilastirilmali
        // veya esit ise bu activity degil ise baska aktivite acilabilir
        Intent intent = new Intent(this, SummeryTabActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        if (adShowed) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
        }
    }
}
