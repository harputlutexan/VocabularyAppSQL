package com.englishvocabularygame.evog.Statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.englishvocabularygame.evog.R;

import java.util.ArrayList;

/**
 * Created on 15.12.2016.
 */

public class ProfileArrayAdapter extends ArrayAdapter<Results> {


private Context mContext;

    public ProfileArrayAdapter(Context context, int resource, ArrayList<Results> resultsArrayList) {
        super(context, resource, resultsArrayList);
        this.mContext =context;
    }


    static class ViewHolder {
        TextView questionIdView;
        TextView testTypeView;
        TextView correctAnswerCountView;
        TextView wrongAnswerCountView;
        TextView emptyAnswerCountView;
        TextView testDateView;
        TextView successRateView;
    }


    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder viewHolder;
        Results results = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.overall_summary_template, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.questionIdView = convertView.findViewById(R.id.overall_question_id);
            viewHolder.testTypeView = convertView.findViewById(R.id.overall_test_type);
            viewHolder.correctAnswerCountView = convertView.findViewById(R.id.overall_correct_answer);
            viewHolder.wrongAnswerCountView = convertView.findViewById(R.id.overall_wrong_answer);
            viewHolder.emptyAnswerCountView = convertView.findViewById(R.id.overall_empty_answer);
            viewHolder.testDateView = convertView.findViewById(R.id.overall_test_date);
            viewHolder.successRateView = convertView.findViewById(R.id.success_rate);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (results!=null) {
            String testId = " #" + results.getItemTestIdString();
            viewHolder.questionIdView.setText(testId);
            String testType = results.getItemTestType();
            viewHolder.testTypeView.setText(testType);
            String testDate = results.getItemTestDate();
            viewHolder.testDateView.setText(testDate);
            String wrongOnesText = mContext.getString(R.string.wrong_summary);
            String falseOnes = wrongOnesText + String.valueOf(results.getItemNumberOfFalseAnswers());
            viewHolder.wrongAnswerCountView.setText(falseOnes);
            String emptyOnesText = mContext.getString(R.string.empty_summary);
            String emptyOnes = emptyOnesText + String.valueOf(results.getItemNumberOfEmptyAnswers());
            viewHolder.emptyAnswerCountView.setText(emptyOnes);
            String correctOnesText = mContext.getString(R.string.correct_summary);
            String correctOnes = correctOnesText + String.valueOf(results.getItemNumberOfCorrectAnswers());
            viewHolder.correctAnswerCountView.setText(correctOnes);
            String successRate = "% " + results.getItemSuccessRate();
            viewHolder.successRateView.setText(successRate);
        }
        return convertView;
    }

//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
}

