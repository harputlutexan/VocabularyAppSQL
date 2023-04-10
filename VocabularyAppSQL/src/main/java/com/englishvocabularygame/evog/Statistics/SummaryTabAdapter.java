package com.englishvocabularygame.evog.Statistics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created on 7.10.2016.
 */

public class SummaryTabAdapter extends FragmentPagerAdapter {

    private Context mContext;

    // constructor ile yeni nesneler icin kullanilacak parametreler belirlendi
    SummaryTabAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CorrectAnswerFragment();
        } else if (position == 1) {
            return new WrongAnswerFragment();
        } else {
            return new EmptyAnswerFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        if (position == 0) {
//            return mContext.getString(R.string.summary_correct_answers);
//        } else if (position == 1) {
//            return mContext.getString(R.string.summary_wrong_answers);
//        } else {
//            return mContext.getString(R.string.summary_empty_answers);
//        }
//    }
}
