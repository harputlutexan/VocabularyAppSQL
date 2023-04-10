package com.englishvocabularygame.evog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.englishvocabularygame.evog.Fragments.AchievementFragment;
import com.englishvocabularygame.evog.Fragments.HomeFragment;
import com.englishvocabularygame.evog.Fragments.ProfileFragment;
import com.englishvocabularygame.evog.Fragments.StatisticsFragment;

import static com.englishvocabularygame.evog.Fragments.TestFragment.updatedList;

/**
 * Created on 7.10.2016.
 */

public class TabAdapter extends FragmentPagerAdapter {


    private final FragmentManager mFragmentManager;
    private Context mContext;


    // constructor ile yeni nesneler icin kullanilacak parametreler belirlendi
    TabAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        StatisticsFragment statisticsFragment;
        if (updatedList != null) {
            statisticsFragment = StatisticsFragment.newInstance(updatedList);
        } else {
            statisticsFragment = new StatisticsFragment();
        }
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new ProfileFragment();
        } else if (position == 2) {
            return statisticsFragment;
        } else {
            return new AchievementFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (position == 0) {
//            return "ANA EKRAN";
//        } else if (position == 1) {
//            return "";
//        } else if (position == 2) {
//            return "EKRAN";
//        } else {
//            return "EKRAN";
//        }
//    }
}


