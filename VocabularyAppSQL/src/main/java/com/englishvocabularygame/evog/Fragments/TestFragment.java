package com.englishvocabularygame.evog.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.englishvocabularygame.evog.CustomToast;
import com.englishvocabularygame.evog.Data.DataAdapter;
import com.englishvocabularygame.evog.Data.DataContract;
import com.englishvocabularygame.evog.Data.StatisticsSqlDatabaseHelper;
import com.englishvocabularygame.evog.Data.TempDatabaseHelper;
import com.englishvocabularygame.evog.R;
import com.englishvocabularygame.evog.Statistics.ProfileArrayAdapter;
import com.englishvocabularygame.evog.Statistics.Results;
import com.englishvocabularygame.evog.SummaryActivity;
import com.englishvocabularygame.evog.TestActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.COLUMN_ACCURACY;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.COLUMN_CORRECT_ANSWER;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.COLUMN_TEST_TYPE;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.TEMP_TABLE_NAME;
import static com.englishvocabularygame.evog.Data.DataContract.TEMP_DATABASE_NAME;


public class TestFragment extends Fragment {

    private String TAG = "TestFragment";


    public TempDatabaseHelper tempDbHelper;
    public static SQLiteDatabase tempSqlDb;
    int dbNumber = HomeFragment.databaseNumber;
    private String testType;
    private String userAnswerforDb;
    private String correctAnswerforDb;
    private String questionWordforDb;
    private String correctAnswerforTimeUp;
    private String questionWordforTimeUp;
    private String testDate;
    private int defaultTestId = 1;
    private static final int COLUMN_ENGLISH_INDEX = 1;
    private static final int COLUMN_TRANSLATED_WORDS_1_INDEX = 2;
    private static final int COLUMN_TRANSLATED_WORDS_2_INDEX = 3;
    private static final int COLUMN_TRANSLATED_WORDS_3_INDEX = 4;
    private static final int COLUMN_TRANSLATED_WORDS_4_INDEX = 5;
    final String correctAnswerPreference = "correctAnswer";
    final String falseAnswerPreference = "falseAnswer";
    final String questionCountPreference = "questionCount";
    final String scorePreference = "scoreCount";
    private SharedPreferences stats;
    private final String tempUserStats = "tempUserStats";
    final String tempCorrectAnswerPreference = "tempCorrectAnswer";
    final String tempFalseAnswerPreference = "tempFalseAnswer";
    final String tempRemainingTimerPreference = "tempRemainingTimerPreference";
    final String tempScorePreference = "tempScoreCount";
    private SharedPreferences tempStats;
    public static String listFilename = "statisticalResults";
    private static final int DATABASE_GRE_NUMBER = 30;
    private static final int DATABASE_TOEFL_NUMBER = 15;
    private static final int DATABASE_TOEIC_NUMBER = 10;
    private static final int DATABASE_IELTS_NUMBER = 150;
    final String remainingTimerPreference = "remainingTimerPreference";
    // bir test de sorulacak soru sayisi
    private int testQuestionCount = 10;
    private long remainingTime;
    private TextView correctTextViewForTimeUp;
    TextView mCountDownView;
    long countDownTime = 15000;
    public static CountDownTimer timer;
    int delayTimeBetweenActivities = 1000;
    public static int mPrevioudTestId;
    public static transient ArrayList<Results> updatedList;
    private String userStats = "userStats";
    private String testIdPreference = "testIdPreference";
    public static int testIdForOverall;
    private TextToSpeech tts;
    private String engVoice;
    private String questionWord;
    private String timeIsUp;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;


    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = getContext();
        Log.d(TAG, "database " + dbNumber);
        final View mainView = inflater.inflate(R.layout.fragment_test, container, false);
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        DataAdapter mDbHelper = new DataAdapter(getContext());
        mDbHelper.useCreatedDatabase();
        mDbHelper.useOpenedDatabase();
        Cursor selectedCursor = mDbHelper.getWords();
        TextView question = mainView.findViewById(R.id.question);
        timeIsUp = getString(R.string.time_is_up);
        // DataAdapter da rastgele bir sayi ile belirlenen id kullanilarak olusturulan cursor ile
        // questionWord, correctAnswer, choice2Word, choice3Word ve choice4Word belirleniyor
        // bunlar sirasi ile choice1-2-3-4 textView lerine ataniyor daha sonra kendi iclerinde shuffle yapilip
        // R.id.lere nihai atamalari yapiliyor.
        final String correctAnswer = selectedCursor.getString(COLUMN_TRANSLATED_WORDS_1_INDEX);
        final String choice2Word = selectedCursor.getString(COLUMN_TRANSLATED_WORDS_2_INDEX);
        final String choice3Word = selectedCursor.getString(COLUMN_TRANSLATED_WORDS_3_INDEX);
        final String choice4Word = selectedCursor.getString(COLUMN_TRANSLATED_WORDS_4_INDEX);
        String[] options = new String[]{correctAnswer, choice2Word, choice3Word, choice4Word};
        int[] choices = new int[]{0, 1, 2, 3};
        shuffleArray(choices);
        questionWord = selectedCursor.getString(COLUMN_ENGLISH_INDEX);
        changeActionBarDifficulty(mainView);
        selectedCursor.close();
        mDbHelper.close();
//        Handler handler = new Handler();
//        Runnable delay = new Runnable() {
//            @Override
//            public void run() {
        initCountdownTimer(mainView);
//            }
//        };
//        handler.postDelayed(delay, delayTimeBetweenActivities);

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    engVoice = tts.getDefaultEngine();
                    Log.d("Engine Info ", engVoice);
                    tts.setLanguage(Locale.ENGLISH);
                    tts.setPitch((float) 1);
                    tts.setSpeechRate((float) 0.8);
                }
            }
        }, engVoice);
        question.setText(questionWord);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(questionWord, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        textView1 = mainView.findViewById(R.id.choice1);
        textView1.setText(options[choices[0]]);
        textView1.setBackgroundResource(R.drawable.answerfirst_button);
        textView2 = mainView.findViewById(R.id.choice2);
        textView2.setText(options[choices[1]]);
        textView2.setBackgroundResource(R.drawable.answerfirst_button);
        textView3 = mainView.findViewById(R.id.choice3);
        textView3.setText(options[choices[2]]);
        textView3.setBackgroundResource(R.drawable.answerfirst_button);
        textView4 = mainView.findViewById(R.id.choice4);
        textView4.setText(options[choices[3]]);
        textView4.setBackgroundResource(R.drawable.answerfirst_button);
        final TextView[] textViewArray = {textView1, textView2, textView3, textView4};
        int correctChoiceIndex = 0;
        for (int i = 0; i < 4; i++) {
            if ((options[choices[i]]).equals(correctAnswer)) {
                correctChoiceIndex = i;
            }
        }
        final TextView correctTextView = textViewArray[correctChoiceIndex];
        setCorrectTextViewForTimeUp(correctTextView);
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        int countQuestionNumber = stats.getInt(questionCountPreference, 1);
        TextView currentQuestionCount = mainView.findViewById(R.id.current_question);
        String countQuestionNumberString = String.valueOf(countQuestionNumber) + " / " + testQuestionCount;
        currentQuestionCount.setText(countQuestionNumberString);
        setWordsforTimeUp(correctAnswer, questionWord);
        // burada dogru cevap ve soru callback ile testactivity deki onchoiceselected yontemine
        // godneriliyor oradan da intent.putextra lar ile transitionactivity e gonderiliyor.

        leftAnimation(textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                resultOnclick(textView1, correctAnswer, questionWord);
                correctTextView.setBackgroundResource(R.drawable.correct_button);
//                correctTextView.getBackground().setColorFilter
//                        (ContextCompat.getColor(context, R.color.classic_green), PorterDuff.Mode.SRC_ATOP);
                correctTextView.setTextColor(ContextCompat.getColor(context, R.color.classic_white));
                textView3.setEnabled(false);
                textView2.setEnabled(false);
                textView1.setEnabled(false);
                textView4.setEnabled(false);
            }
        });

        rightAnimation(textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                resultOnclick(textView2, correctAnswer, questionWord);
                // iclerinden dogruyu bulmak icin...
                correctTextView.setBackgroundResource(R.drawable.correct_button);
//
//                correctTextView.getBackground().setColorFilter
//                        (ContextCompat.getColor(context, R.color.classic_green), PorterDuff.Mode.SRC_ATOP);
                correctTextView.setTextColor(ContextCompat.getColor(context, R.color.classic_white));
                textView3.setEnabled(false);
                textView2.setEnabled(false);
                textView1.setEnabled(false);
                textView4.setEnabled(false);

            }
        });
        leftAnimation(textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                resultOnclick(textView3, correctAnswer, questionWord);
                correctTextView.setBackgroundResource(R.drawable.correct_button);

//                correctTextView.getBackground().setColorFilter
//                        (ContextCompat.getColor(context, R.color.classic_green), PorterDuff.Mode.SRC_ATOP);
                correctTextView.setTextColor(ContextCompat.getColor(context, R.color.classic_white));
                textView3.setEnabled(false);
                textView2.setEnabled(false);
                textView1.setEnabled(false);
                textView4.setEnabled(false);

            }
        });

        rightAnimation(textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                resultOnclick(textView4, correctAnswer, questionWord);
                correctTextView.setBackgroundResource(R.drawable.correct_button);
//                correctTextView.getBackground().setColorFilter
//                        (ContextCompat.getColor(context, R.color.classic_green), PorterDuff.Mode.SRC_ATOP);
                correctTextView.setTextColor(ContextCompat.getColor(context, R.color.classic_white));
                textView3.setEnabled(false);
                textView2.setEnabled(false);
                textView1.setEnabled(false);
                textView4.setEnabled(false);

            }
        });
        return mainView;
    }

    private void leftAnimation(final TextView textView) {
        textView.setEnabled(false);
        Animation leftAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right);
        textView.startAnimation(leftAnimation);
        Handler handler = new Handler();
        Runnable delay = new Runnable() {
            @Override
            public void run() {
                textView.setEnabled(true);
            }
        };
        handler.postDelayed(delay, delayTimeBetweenActivities);
    }

    private void rightAnimation(final TextView textView) {
        textView.setEnabled(false);
        Animation rightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);
        textView.startAnimation(rightAnimation);
        Handler handler = new Handler();
        Runnable delay = new Runnable() {
            @Override
            public void run() {
                textView.setEnabled(true);
            }
        };
        handler.postDelayed(delay, delayTimeBetweenActivities);
    }

    private void changeActionBarDifficulty(View view) {
        TextView actionBarText;
        actionBarText = view.findViewById(R.id.actionbar_difficulty_level);
        ImageView voiceView = view.findViewById(R.id.volume);
        switch (dbNumber) {
            case (DATABASE_GRE_NUMBER):
                testType = getString(R.string.action_bar_gre);
                actionBarText.setText(testType);
                setTestType(testType);
                voiceView.setVisibility(View.VISIBLE);
                break;
            case (DATABASE_IELTS_NUMBER):
                testType = getString(R.string.action_bar_ielts);
                actionBarText.setText(testType);
                setTestType(testType);
                voiceView.setVisibility(View.VISIBLE);
                break;
            case (DATABASE_TOEFL_NUMBER):
                testType = getString(R.string.action_bar_toefl);
                actionBarText.setText(testType);
                setTestType(testType);
                voiceView.setVisibility(View.VISIBLE);
                break;
            case (DATABASE_TOEIC_NUMBER):
                testType = getString(R.string.action_bar_toeic);
                actionBarText.setText(testType);
                setTestType(testType);
                voiceView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    private void resultOnclick(TextView choice, String correctAnswer, String questionWord) {
        //tıklandığı zaman eğer multiplayer seçili ise iki sonraki soruya geçsin eğer soru sayısı
        // sona yaklaşmıs ise bir if ile kontrolü yapılsın + yerine - denilsin
        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \nHH:mm");
        String pattern = "yyyy-MM-dd";
        Locale currentLocale = getResources().getConfiguration().locale;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, currentLocale);
        testDate = sdf.format(calendar.getTime());
        setTestDate(testDate);
        Context context = getContext();
        String choiceString = choice.getText().toString();
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        int countQuestionNumber = 1;
        int countCorrect = 0;
        int countFalse = 0;
        int countScore = 0;
        int tempcountScore = 0;
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        tempStats = getContext().getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = stats.edit();
        SharedPreferences.Editor tempedit = tempStats.edit();
        int defaultQuestionCount = stats.getInt(questionCountPreference, countQuestionNumber);
        int defaultTrueAnswerValue = stats.getInt(correctAnswerPreference, countCorrect);
        int defaultFalseAnswerValue = stats.getInt(falseAnswerPreference, countFalse);
        int defaultScore = stats.getInt(scorePreference, countScore);
        int defaultTempTrueAnswerValue = tempStats.getInt(tempCorrectAnswerPreference, countCorrect);
        int defaultTempFalseAnswerValue = tempStats.getInt(tempFalseAnswerPreference, countFalse);
        int defaultTempScore = tempStats.getInt(tempScorePreference, tempcountScore);
        // dogru ve yanlis cevaplara once default 0 atandi sonra o atanan her dogru veya yanlis icin 1er arttirilip
        // yeni degisken count lara kaydedildi
        ++defaultQuestionCount;
        settersForWords(choiceString, correctAnswer, questionWord);
        putDataToStatistics();
        edit.putInt(questionCountPreference, defaultQuestionCount);

        // dogru ise dogru fragment ina yazdirabilmek icin
        if (correctAnswer.equals(choiceString)) {
            int correctColor = ContextCompat.getColor(getContext(), R.color.burak_correct_color);
            CustomToast customToast = new CustomToast(getContext(), 5000, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            String correctAnswerToast = getString(R.string.answer_is_correct);
            customToast.show(correctAnswerToast, correctColor, Color.TRANSPARENT);
            defaultTrueAnswerValue = defaultTrueAnswerValue + 1;
            edit.putInt(correctAnswerPreference, defaultTrueAnswerValue);
            defaultTempTrueAnswerValue = defaultTempTrueAnswerValue + 1;
            tempedit.putInt(tempCorrectAnswerPreference, defaultTempTrueAnswerValue);
            defaultScore = defaultScore + 10;
            edit.putInt(scorePreference, defaultScore);
            defaultTempScore = defaultTempScore + 10;
            tempedit.putInt(tempScorePreference, defaultTempScore);
            // once default yukarida 0/1 ataniyor, daha sonra arttirilmis hali cekilerek tekrar o rakam kullaniliyor
            tempedit.apply();
            edit.apply();
            startTestActivity();
        } else if (!correctAnswer.equals(choiceString)) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            String vibrationPreference = "vibrationPreference";
            if (stats.getBoolean(vibrationPreference, true)) {
                if (vibrator != null) {
                    vibrator.vibrate(500);
                }
            } else {
                if (vibrator != null) {
                    vibrator.cancel();
                }
            }
            choice.setBackgroundResource(R.drawable.false_button);
//            choice.getBackground().setColorFilter
//                    (ContextCompat.getColor(context, R.color.classic_red), PorterDuff.Mode.SRC_ATOP);
            choice.setTextColor(ContextCompat.getColor(context, R.color.classic_white));
            int falseColor = ContextCompat.getColor(getContext(), R.color.burak_false_color);
            CustomToast customToast = new CustomToast(getContext(), 5000, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            String wrongAnswerToast = getString(R.string.answer_is_false);
            customToast.show(wrongAnswerToast, falseColor, Color.TRANSPARENT);
            defaultFalseAnswerValue = defaultFalseAnswerValue + 1;
            ++defaultTempFalseAnswerValue;
            edit.putInt(falseAnswerPreference, defaultFalseAnswerValue);
            tempedit.putInt(tempFalseAnswerPreference, defaultTempFalseAnswerValue);
            edit.apply();
            tempedit.apply();
            startTestActivity();
        }
        determinePassedTime();
    }

    private void settersForWords(String choiceString, String correctAnswer, String questionWord) {
        this.correctAnswerforDb = correctAnswer;
        this.questionWordforDb = questionWord;
        this.userAnswerforDb = choiceString;
    }

//    private int scoreCorrection() {
//        int additionalScore;
//        if (HomeFragment.databaseNumber > 30) {
//            additionalScore = HomeFragment.databaseNumber / 10;
//        } else {
//            additionalScore = HomeFragment.databaseNumber;
//        }
//        return additionalScore;
//    }

    static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public String getCorrectAnswerforTimeUp() {
        return correctAnswerforTimeUp;
    }

    public String getquestionWordforTimeUp() {
        return questionWordforTimeUp;
    }

    public void setWordsforTimeUp(String correctAnswer, String questionWord) {
        this.correctAnswerforTimeUp = correctAnswer;
        this.questionWordforTimeUp = questionWord;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy is called");
        super.onDestroy();
        if (tts != null) {
            tts.shutdown();
        }
    }

    private void initCountdownTimer(View view) {
        Log.d(TAG, "initCountdownTimer called");
        mCountDownView = view.findViewById(R.id.countdown_timer);
        timer = new CountDownTimer(countDownTime, 1000) {
            //millisUntilFinished The amount of time until finished.
            public void onTick(long millisUntilFinished) {
//                Log.d(TAG, "millisUntilFinished " + millisUntilFinished);
                // mCountDownView.setText("seconds remaining: " + millisUntilFinished / 1000);
                if (millisUntilFinished > 2000) {
                    mCountDownView.setText(String.format("%s", String.valueOf(millisUntilFinished / 1000)));
                } else if (2000 > millisUntilFinished & millisUntilFinished > 1000) {
                    mCountDownView.setText("1");
                    Handler handler = new Handler();
                    Runnable delay = new Runnable() {
                        @Override
                        public void run() {
                            mCountDownView.setText("0");
                        }
                    };
                    handler.postDelayed(delay, 1000);

                }

                // set ile buradaki degeri global degisken remainingTime a atiyoruz
                // get ile o degeri kaydediyoruz bu veya baska bir class icerisinde get
                // i cagirarak o degeri elde ediyoru
                setMillis(millisUntilFinished);
            }

            public void onFinish() {
                textView3.setEnabled(false);
                textView2.setEnabled(false);
                textView1.setEnabled(false);
                textView4.setEnabled(false);
                mCountDownView.setText("0");
                Log.d(TAG, "onFinish called");
                SharedPreferences.Editor edit = stats.edit();
                int timeFinishedQuestionCount = stats.getInt(questionCountPreference, 1);
                timeFinishedQuestionCount = timeFinishedQuestionCount + 1;
                edit.putInt(questionCountPreference, timeFinishedQuestionCount);
                edit.apply();
                if (getContext() != null) {
                    int redColor = ContextCompat.getColor(getContext(), R.color.burak_false_color);
                    CustomToast customToast = new CustomToast(getContext(), 1000, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    customToast.show(timeIsUp, redColor, Color.TRANSPARENT);
                    getCorrectTextview().setBackgroundResource(R.drawable.correct_button);
                    getCorrectTextview().setTextColor(ContextCompat.getColor(getContext(), R.color.classic_white));
                }
                putDataToStatisticsTimeIsUp();
                startTestActivity();
            }

        };
        timer.start();
    }

    private void setMillis(Long miliSn) {
        this.remainingTime = miliSn;
    }

    private long getMilisn() {
        return remainingTime;
    }

    private TextView getCorrectTextview() {
        return correctTextViewForTimeUp;
    }

    private void setCorrectTextViewForTimeUp(TextView correctView) {
        this.correctTextViewForTimeUp = correctView;
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop called");
//        if (timer != null) {
//            timer.cancel();
//        }
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();
    }

    private void determinePassedTime() {
        long countRemainingTime = 0;
        long tempCountRemainingTime = 0;
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        tempStats = getContext().getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = stats.edit();
        SharedPreferences.Editor tempEdit = tempStats.edit();
        long defaultRemainingTime = stats.getLong(remainingTimerPreference, countRemainingTime);
        long defaultTempRemainingTime = tempStats.getLong(tempRemainingTimerPreference, tempCountRemainingTime);
        defaultRemainingTime = defaultRemainingTime + (15000 / 1000) - (getMilisn() / 1000);
        defaultTempRemainingTime = defaultTempRemainingTime + (15000 / 1000) - (getMilisn() / 1000);
        edit.putLong(remainingTimerPreference, defaultRemainingTime);
        tempEdit.putLong(tempRemainingTimerPreference, defaultTempRemainingTime);
        // commit yerine apply denendi tekrar bakilabilir hata olursa
        tempEdit.apply();
        edit.apply();
    }

    public String getUserAnswerforDb() {
        return userAnswerforDb;
    }

    public String getCorrectAnswerforDb() {
        return correctAnswerforDb;
    }

    public String getQuestionWordforDb() {
        return questionWordforDb;
    }

    private void putDataToStatisticsTimeIsUp() {
        Calendar calendar = Calendar.getInstance();
        String pattern = "yyyy-MM-dd";
        Locale currentLocale = getResources().getConfiguration().locale;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, currentLocale);
        testDate = sdf.format(calendar.getTime());
        setTestDate(testDate);
        tempDbHelper = new TempDatabaseHelper(getContext());
        tempSqlDb = tempDbHelper.getWritableDatabase();
        String resultAccuracy = " - ";
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_TYPE, getTestType());
        values.put(DataContract.StatisticsTable.COLUMN_TEST_ID,
                String.valueOf(stats.getInt(testIdPreference, defaultTestId)));
        values.put(DataContract.StatisticsTable.COLUMN_TEST_DATE, getTestDate());
        values.put(DataContract.StatisticsTable.COLUMN_QUESTION, getquestionWordforTimeUp());
        values.put(DataContract.StatisticsTable.COLUMN_USER_ANSWER, resultAccuracy);
        values.put(COLUMN_CORRECT_ANSWER, getCorrectAnswerforTimeUp());
        values.put(COLUMN_ACCURACY, resultAccuracy);
        values.put(DataContract.StatisticsTable.COLUMN_ANSWER_TIME, "15");
        tempSqlDb.insert(TEMP_TABLE_NAME, null, values);
//        tempSqlDb.close();
    }

    private void putDataToStatistics() {
        tempDbHelper = new TempDatabaseHelper(getContext());
        tempSqlDb = tempDbHelper.getWritableDatabase();
        String resultAccuracy;
        if (getCorrectAnswerforDb().equals(getUserAnswerforDb())) {
            resultAccuracy = "true";
        } else {
            resultAccuracy = "false";
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_TYPE, getTestType());
        values.put(DataContract.StatisticsTable.COLUMN_TEST_ID,
                String.valueOf(stats.getInt(testIdPreference, defaultTestId)));
        values.put(DataContract.StatisticsTable.COLUMN_TEST_DATE, getTestDate());
        values.put(DataContract.StatisticsTable.COLUMN_QUESTION, getQuestionWordforDb());
        values.put(DataContract.StatisticsTable.COLUMN_USER_ANSWER, getUserAnswerforDb());
        values.put(COLUMN_CORRECT_ANSWER, getCorrectAnswerforDb());
        values.put(COLUMN_ACCURACY, resultAccuracy);
        values.put(DataContract.StatisticsTable.COLUMN_ANSWER_TIME, (15000 / 1000) - (getMilisn() / 1000));
        tempSqlDb.insert(TEMP_TABLE_NAME, null, values);
//        tempSqlDb.close();
    }

    private void startTestActivity() {
        Log.d(TAG, "startTestActivity called");
        //her isaretlenen secenek sonrasi ya teste devam ya da degerleri alip sonlandirmak icin
        if (stats.getInt(questionCountPreference, 1) > testQuestionCount) {
            StatisticsSqlDatabaseHelper stDbHelper = new StatisticsSqlDatabaseHelper(getContext());
            SQLiteDatabase db = stDbHelper.getWritableDatabase();
            tempDbHelper = new TempDatabaseHelper(getContext());
            tempSqlDb = tempDbHelper.getWritableDatabase();
            updateAchievementsForTrueAnswers();
            String dbPath = getContext().getDatabasePath(TEMP_DATABASE_NAME).getPath();
            testIdForOverall = stats.getInt(testIdPreference, defaultTestId);
            String ALIAS_NAME = "ALIAS";
            String ATTACH_TABLE = "ATTACH DATABASE '" + dbPath + "' AS " + ALIAS_NAME;
            db.execSQL(ATTACH_TABLE);
            db.execSQL(DataContract.StatisticsTable.MERGE_TABLE);
            testIdForOverall++;
            SharedPreferences.Editor testIdEdit = stats.edit();
            overallResults();
            testIdEdit.putInt(testIdPreference, testIdForOverall);
            testIdEdit.apply();
            Handler handler = new Handler();
            Runnable delay = new Runnable() {
                @Override
                public void run() {
                    stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
                    mPrevioudTestId = stats.getInt(testIdPreference, 1) - 1;
                    if (tts != null) {
                        tts.stop();
                    }
                    Intent intent = new Intent(getActivity(), SummaryActivity.class);
                    startActivity(intent);

                }
            };

            handler.postDelayed(delay, delayTimeBetweenActivities);

        } else {
            Handler handler = new Handler();
            Runnable delay = new Runnable() {
                @Override
                public void run() {
                    if (tts != null) {
                        tts.stop();
                    }
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    startActivity(intent);
                }
            };
            handler.postDelayed(delay, delayTimeBetweenActivities);
        }
    }

    public void overallResults() {
        SharedPreferences loginStats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        int testId = loginStats.getInt(testIdPreference, 1);
        Cursor correctAnswerCursor;
        Cursor falseCursor;
        Cursor testTypeCursor;
        StatisticsSqlDatabaseHelper dbHelper = new StatisticsSqlDatabaseHelper(getContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        //bir onceki yani arttirilmamis test id ve dogru , bos veya yanlis a gore secim yapiliyor
        String tableName = DataContract.StatisticsTable.TABLE_NAME;
        String selection = DataContract.StatisticsTable.COLUMN_TEST_ID + " = ? AND "
                + COLUMN_ACCURACY + " = ?";
        String testIdString = String.valueOf(testId);
        String selectionArgsCorrect[] = {testIdString, "true"};
        correctAnswerCursor = sqLiteDatabase.query(tableName
                , null
                , selection
                , selectionArgsCorrect, null, null, null);
        int numberOfCorrectAnswers = correctAnswerCursor.getCount();
        correctAnswerCursor.close();
        String selectionArgsFalse[] = {testIdString, "false"};
        falseCursor = sqLiteDatabase.query(tableName
                , null
                , selection
                , selectionArgsFalse, null, null, null);
        int numberOfFalseAnswers = falseCursor.getCount();
        falseCursor.close();
        int numberOfEmptyAnswers = testQuestionCount - numberOfCorrectAnswers - numberOfFalseAnswers;
        String selectionTestType = DataContract.StatisticsTable.COLUMN_TEST_ID + " = ?";
        String selectionArgTestType[] = {testIdString};
        testTypeCursor = sqLiteDatabase.query(tableName
                , null
                , selectionTestType
                , selectionArgTestType, null, null, null);
        String testTypeString;
        String testDateString;
        if (testTypeCursor.getCount() == 0) {
            testTypeString = "";
            testDateString = "";
        } else {
            testTypeCursor.moveToFirst();
            testTypeString = testTypeCursor.getString(testTypeCursor
                    .getColumnIndex(COLUMN_TEST_TYPE));
            testDateString = testTypeCursor.getString(testTypeCursor
                    .getColumnIndex(DataContract.StatisticsTable.COLUMN_TEST_DATE));
        }
        testTypeCursor.close();
        int successRate = (int) (100.00 * ((numberOfCorrectAnswers * 1.00) /
                (testQuestionCount)));
        Log.d("basari", String.valueOf(successRate));
        String successRateString = String.valueOf(successRate);
        Results aResults = new Results(testIdString, testTypeString, testDateString, numberOfCorrectAnswers
                , numberOfFalseAnswers, numberOfEmptyAnswers, successRateString);
//        if (updatedList == null) {
//            initArrayList();
//        }
        if (updatedList != null) {
            Collections.reverse(updatedList);
            updatedList.add(aResults);
            Collections.reverse(updatedList);
            saveArrayListToSD(getContext(), listFilename, updatedList);
            ProfileArrayAdapter arrayAdapter = new ProfileArrayAdapter(getContext(), 0, updatedList);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void updateAchievementsForTrueAnswers() {
        //bir testin hatasiz bitirilme kontrolu
        Cursor setGRECursor;
        Cursor setTOEFLCursor;
        Cursor setTOEICCursor;
        Cursor setIELTSCursor;


        String tableName = TEMP_TABLE_NAME;
        String selection = COLUMN_ACCURACY + " = ? AND " + COLUMN_TEST_TYPE + " LIKE ?";
        String toeic = getString(R.string.action_bar_toeic);
        String toeicLike = '%' + toeic;
        String ielts = getString(R.string.action_bar_ielts);
        String ieltsLike = '%' + ielts;
        String toefl = getString(R.string.action_bar_toefl);
        String toeflLike = '%' + toefl;
        String greWords = getString(R.string.action_bar_gre);
        String greWordsLike = '%' + greWords;

        String selectionArgsToeic[] = {"true", toeicLike};
        String selectionArgsIelts[] = {"true", ieltsLike};
        String selectionArgsToefl[] = {"true", toeflLike};
        String selectionArgsGreWords[] = {"true", greWordsLike};

        setGRECursor = tempSqlDb.query(tableName, null, selection, selectionArgsGreWords
                , null, null, null);
        setTOEFLCursor = tempSqlDb.query(tableName, null, selection, selectionArgsToefl
                , null, null, null);
        setTOEICCursor = tempSqlDb.query(tableName, null, selection, selectionArgsToeic
                , null, null, null);
        setIELTSCursor = tempSqlDb.query(tableName, null, selection, selectionArgsIelts
                , null, null, null);


        int correctAnswerGreWords = setGRECursor.getCount();
        int correctAnswerTOEFL = setTOEFLCursor.getCount();
        int correctAnswerTOEIC = setTOEICCursor.getCount();
        int correctAnswerIELTS = setIELTSCursor.getCount();

        String GREIIPreference = "GREIIPreference";
        String TOEFLIIPreference = "TOEFLIIPreference";
        String TOEICIIPreference = "TOEICIIPreference";
        String IELTSIIPreference = "IELTSIIPreference";


        if (correctAnswerGreWords != 0 && correctAnswerGreWords == testQuestionCount) {
            SharedPreferences.Editor achievementBeginnerPreferenceEdit = stats.edit();
            achievementBeginnerPreferenceEdit.putBoolean(GREIIPreference, true);
            achievementBeginnerPreferenceEdit.apply();
        }

        if (correctAnswerTOEFL != 0 && correctAnswerTOEFL == testQuestionCount) {
            SharedPreferences.Editor achievementInterPreferenceEdit = stats.edit();
            achievementInterPreferenceEdit.putBoolean(TOEFLIIPreference, true);
            achievementInterPreferenceEdit.apply();
        }
        if (correctAnswerTOEIC != 0 && correctAnswerTOEIC == testQuestionCount) {
            SharedPreferences.Editor achievementAdvancedPreferenceEdit = stats.edit();
            achievementAdvancedPreferenceEdit.putBoolean(TOEICIIPreference, true);
            achievementAdvancedPreferenceEdit.apply();
        }
        if (correctAnswerIELTS != 0 && correctAnswerIELTS == testQuestionCount) {
            SharedPreferences.Editor achievementExpertPreferenceEdit = stats.edit();
            achievementExpertPreferenceEdit.putBoolean(IELTSIIPreference, true);
            achievementExpertPreferenceEdit.apply();
        }

//        }
        setGRECursor.close();
        setTOEFLCursor.close();
        setTOEICCursor.close();
        setIELTSCursor.close();
    }


    public static <Results> void saveArrayListToSD(Context mContext, String filename, ArrayList<Results> list) {
        try {
            File dir = mContext.getFilesDir();
            FileOutputStream fos = new FileOutputStream(dir + filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
//            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
