package com.englishvocabularygame.evog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.englishvocabularygame.evog.Fragments.TestFragment;

//implements TestFragment.Callback
public class TestActivity extends AppCompatActivity {


    final String tempUserStats = "tempUserStats";
    private SharedPreferences stats;

// dogru cevap yanlis cevap olarak farkli iki metod da yapilabilir
    // dogruysa devam et yanlis ise yeni oyun soracak ortada bir metin veya button
    //  transitionactiivty yapmadan testfragment da tiklaninca buraya yonlendirse bura da ayri bir
    // transition fragment a yonlendirse..

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TestFragment","TestActivity started");
        setContentView(R.layout.empty_template);
        String userStats = "userStats";
        stats = getSharedPreferences(userStats, Context.MODE_PRIVATE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new TestFragment())
                .commit();
    }


    @Override
    public void onBackPressed() {
        // soru devam ederken geriye bastiginda sifirlamak icin
        SharedPreferences tempStats = getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
        if (TestFragment.timer!=null) {
            TestFragment.timer.cancel();
            Log.d("Fragment", "timer cancelled");
        }
        String questionCountPreference = "questionCount";
        stats.edit().remove(questionCountPreference).apply();
        tempStats.edit().clear().apply();
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
