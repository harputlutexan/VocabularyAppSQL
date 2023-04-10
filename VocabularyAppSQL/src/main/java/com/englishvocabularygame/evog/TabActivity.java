package com.englishvocabularygame.evog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created on 1.11.2016.
 */


public class TabActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TabActivity", "tabactivity called");
        super.onCreate(savedInstanceState);
        TabLayout.LayoutParams layoutParams = new TabLayout.LayoutParams
                (TabLayout.LayoutParams.MATCH_PARENT, TabLayout.LayoutParams.WRAP_CONTENT);
        ImageView homeView = new ImageView(this);
        homeView.setImageResource(R.drawable.selection_icon1);
        homeView.setLayoutParams(layoutParams);
        ImageView personalView = new ImageView(this);
        personalView.setImageResource(R.drawable.selection_icon2);
//        personalView.setLayoutParams(layoutParams);
        ImageView statsView = new ImageView(this);
        statsView.setImageResource(R.drawable.selection_icon3);
        statsView.setLayoutParams(layoutParams);
        ImageView achievementView = new ImageView(this);
        achievementView.setImageResource(R.drawable.selection_icon4);
        achievementView.setLayoutParams(layoutParams);
//        final int[] icons = new int[]{
//                R.drawable.home_icon,
//                R.drawable.personal_icon,
//                R.drawable.statistics_chart_tab
//        };
        setContentView(R.layout.template_main);
        FrameLayout settingsImage = findViewById(R.id.settings_icon);
        settingsImage.setOnClickListener(this);
        // ust menulerimizi sececek ViewPageri ayarliyoruz
        mViewPager = findViewById(R.id.viewpager);
        // Hangi tab (sekme) altinda neyin acilacagini belirlemek icin bir TabAdapter hazirlandi ve
        // viewPager uzerine atandi
        TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(tabAdapter);
        // Sekmelerin Layout unu dizaynini belirleyen design ogesi seciliyor ve
        // setupWithViewPager metodu ile sekme layout u viewpager ile baglantisi kuruluyor.
        TabLayout tabLayout = findViewById(R.id.maintabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(homeView);
        tabLayout.getTabAt(1).setCustomView(personalView);
        tabLayout.getTabAt(2).setCustomView(statsView);
        tabLayout.getTabAt(3).setCustomView(achievementView);


    }


    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            AlertDialog alertDialog =
            new AlertDialog.Builder(this,R.style.exit_dialog)
                    .setIcon(R.drawable.poweroff_icon)
                    .setTitle(getString(R.string.exit_application))
                    .setMessage(getString(R.string.confirm_exit))
                    .setPositiveButton(getString(R.string.approve_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton(getString(R.string.no_exit), null).show();
        } else if (mViewPager.getCurrentItem() == 1) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
        } else if (mViewPager.getCurrentItem() == 2) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
        } else if (mViewPager.getCurrentItem() == 3) {
            Intent intent = new Intent(this, TabActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_icon:
                Intent settingsIntent = new Intent(TabActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

        }
    }
}
