package com.englishvocabularygame.evog.Statistics;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.englishvocabularygame.evog.Data.DataContract;
import com.englishvocabularygame.evog.R;

/**
 * Created on 15.12.2016.
 */

public class StatisticsCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public StatisticsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
//        Context mContext = context;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.summary_fragments_template, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView questionView = view.findViewById(R.id.question_summary);
        String question = cursor.getString(cursor.getColumnIndex(DataContract.StatisticsTable.COLUMN_QUESTION));
        questionView.setText(question);
        TextView userAnswerView = view.findViewById(R.id.user_answer_summary);
        String userAnswer = cursor.getString(cursor.getColumnIndex(DataContract.StatisticsTable.COLUMN_USER_ANSWER));
        userAnswerView.setText(userAnswer);
        TextView correctAnswerView = view.findViewById(R.id.correct_answer_summary);
        String correctAnswer = cursor.getString(cursor.getColumnIndex(DataContract.StatisticsTable.COLUMN_CORRECT_ANSWER));
        correctAnswerView.setText(correctAnswer);
    }
}
