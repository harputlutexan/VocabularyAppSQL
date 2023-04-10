package com.englishvocabularygame.evog.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.englishvocabularygame.evog.R;
import com.englishvocabularygame.evog.Statistics.ProfileArrayAdapter;
import com.englishvocabularygame.evog.Statistics.Results;
import com.englishvocabularygame.evog.Statistics.SummeryTabActivity;

import java.util.ArrayList;

import static com.englishvocabularygame.evog.Fragments.TestFragment.updatedList;


/**
 * Created on 7.10.2016.
 */

public class StatisticsFragment extends Fragment {

    ListView mListView;

    public StatisticsFragment() {
    }

// tekrardan baslatip sifirlanmamasi icin
    public static StatisticsFragment newInstance(ArrayList<Results> list) {
//        if (fragment == null) {
//            fragment = new StatisticsFragment();
//        } else {
        StatisticsFragment s = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("num", list);
        s.setArguments(args);
//        }
        return s;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Activityler","StatisticsFragment started");
        View rootView = inflater.inflate(R.layout.overall_summary_listviews, container, false);
        mListView = rootView.findViewById(R.id.overall_summary_list);
        if (updatedList!=null) {
            ProfileArrayAdapter arrayAdapter = new ProfileArrayAdapter(getContext(), 0, updatedList);
            mListView.setAdapter(arrayAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // ilk position i sifir olarak algiladigi icin buradan wronganswerfragmentin
                    // kullacanagi deger ataniyor
                    int realPosition = updatedList.size();
                    TestFragment.mPrevioudTestId = realPosition - (position);
                    Intent intent = new Intent(getActivity(), SummeryTabActivity.class);
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }
}
