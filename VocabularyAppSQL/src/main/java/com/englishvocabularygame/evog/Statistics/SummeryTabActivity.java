package com.englishvocabularygame.evog.Statistics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.englishvocabularygame.evog.R;
import com.englishvocabularygame.evog.TabActivity;

/**
 * Created on 1.11.2016.
 */


public class SummeryTabActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_main_summary);
        // ust menulerimizi sececek ViewPageri ayarliyoruz
        mViewPager = (ViewPager) findViewById(R.id.viewpager_summary);
        // Hangi tab (sekme) altinda neyin acilacagini belirlemek icin bir TabAdapter hazirlandi ve
        // viewPager uzerine atandi
        TabLayout.LayoutParams layoutParams = new TabLayout.LayoutParams
                (TabLayout.LayoutParams.WRAP_CONTENT, TabLayout.LayoutParams.WRAP_CONTENT);
        SummaryTabAdapter tabAdapter = new SummaryTabAdapter(this, getSupportFragmentManager());
        ImageView trueView = new ImageView(this);
        trueView.setImageResource(R.drawable.correct_buttonunselected);
//        trueView.setLayoutParams(layoutParams);
        ImageView falseView = new ImageView(this);
        falseView.setImageResource(R.drawable.false_buttonunselected);
        falseView.setLayoutParams(layoutParams);
        ImageView emptyView = new ImageView(this);
        emptyView.setImageResource(R.drawable.empty_buttonunselected);
        emptyView.setLayoutParams(layoutParams);
        mViewPager.setAdapter(tabAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.summarytabs2);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(trueView);
        tabLayout.getTabAt(1).setCustomView(falseView);
        tabLayout.getTabAt(2).setCustomView(emptyView);
        // Sekmelerin Layout unu dizaynini belirleyen design ogesi seciliyor ve
        // setupWithViewPager metodu ile sekme layout u viewpager ile baglantisi kuruluyor.
        ImageView closeMark = (ImageView) findViewById(R.id.cancel_mark_2);
        closeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String questionCountPreference = "questionCount";
                String userStats = "userStats";
                SharedPreferences stats = getSharedPreferences(userStats, Context.MODE_PRIVATE);
                stats.edit().remove(questionCountPreference).apply();
                // test activitynin icerisinde metot gelistirip secenek ile correctanswer karsilastirilmali
                // veya esit ise bu activity degil ise baska aktivite acilabilir
                Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                startActivity(intent);
            }
        });
    }

//
//    @Override
//    public void onBackPressed() {
//        if (mViewPager.getCurrentItem() == 0) {
//            new AlertDialog.Builder(this)
//                    .setIcon(R.drawable.exit_icon)
//                    .setTitle(getString(R.string.exit_application))
//                    .setMessage(getString(R.string.confirm_exit))
//                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    }).setNegativeButton("No", null).show();
//        } else if (mViewPager.getCurrentItem() == 1){
//            Intent intent = new Intent(this, SummeryTabActivity.class);
//            startActivity(intent);
//        }
//    }
}
