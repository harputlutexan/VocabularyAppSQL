package com.englishvocabularygame.evog.Statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.englishvocabularygame.evog.Fragments.AchievementFragment;
import com.englishvocabularygame.evog.R;

/**
 * Created on 29.01.2017.
 */

public class AchievementActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_template);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new AchievementFragment()).commit();

    }

    @Override
    public void onBackPressed() {
    }

}
