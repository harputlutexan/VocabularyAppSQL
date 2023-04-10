package com.englishvocabularygame.evog.Statistics;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.englishvocabularygame.evog.Data.DataContract;
import com.englishvocabularygame.evog.Data.StatisticsSqlDatabaseHelper;
import com.englishvocabularygame.evog.Fragments.TestFragment;
import com.englishvocabularygame.evog.R;

/**
 * Created on 11.12.2016.
 */

public class EmptyAnswerFragment extends Fragment {

    public EmptyAnswerFragment() {
        // Required empty public constructor
    }

    StatisticsSqlDatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String testIdPreference = "testIdPreference";
    SharedPreferences stats;
    String userStats = "userStats";
    Cursor mCursor;
    ListView listView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.summary_listviews, container, false);
        listView = (ListView) rootView.findViewById(R.id.summary_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StatisticsCursorAdapter customAdapter = new StatisticsCursorAdapter
                (getContext(), getCursor(), 0);
        listView.setAdapter(customAdapter);

    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if ( rootView != null) {
//            ViewGroup parentViewGroup = (ViewGroup) rootView.getParent();
//            if (parentViewGroup != null) {
//                parentViewGroup.removeAllViews();
//            }
//        }
//    }

    private Cursor getCursor() {
        dbHelper = new StatisticsSqlDatabaseHelper(getContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        String tableName = DataContract.StatisticsTable.TABLE_NAME;
        String selection = DataContract.StatisticsTable.COLUMN_TEST_ID + " = ? AND "
                + DataContract.StatisticsTable.COLUMN_ACCURACY + " = ?";
//        stats = getContext().getSharedPreferences(userStats, Context.MODE_APPEND);
//        int previoudTestId = stats.getInt(testIdPreference, 1) - 1;
        String testId = String.valueOf(TestFragment.mPrevioudTestId);
        String selectionArgs[] = {testId, " - "};
        mCursor = sqLiteDatabase.query(tableName
                , null
                , selection
                , selectionArgs, null, null, null);
        return mCursor;
    }
}
