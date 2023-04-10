package com.englishvocabularygame.evog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.englishvocabularygame.evog.Fragments.ProfileFragment;

/**
 * Created  on 4.11.2016.
 */

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_template);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, new ProfileFragment()).commit();

    }


    @Override
    public void onBackPressed() {
    }

}
