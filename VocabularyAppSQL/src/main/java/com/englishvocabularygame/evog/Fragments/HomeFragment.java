package com.englishvocabularygame.evog.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.englishvocabularygame.evog.Data.DataContract;
import com.englishvocabularygame.evog.Data.TempDatabaseHelper;
import com.englishvocabularygame.evog.MainActivity;
import com.englishvocabularygame.evog.R;
import com.englishvocabularygame.evog.TabActivity;
import com.englishvocabularygame.evog.TestActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.TEMP_TABLE_NAME;


/**
 * Created  7.10.2016.
 */

public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, RewardedVideoAdListener, View.OnClickListener {

    private static final int DATABASE_GRE_NUMBER = 30;
    private static final int DATABASE_TOEFL_NUMBER = 15;
    private static final int DATABASE_TOEIC_NUMBER = 10;
    private static final int DATABASE_IELTS_NUMBER = 150;
    public static int databaseNumber;
    private AdView mAdView;
    public static long timeForNewLife = 14400000;
    private boolean onStartBoolean = false;
    final String userStats = "userStats";
    private CountDownTimer lifeTimer;
    private String timeBetweenActivitiesPreference = "timeBetweenActivitiesPreference";
    private SharedPreferences stats;
    private String counterWorks = "counterWorks";
    final String questionCountPreference = "questionCount";
    final String tempUserStats = "tempUserStats";
    private HomeViewHolder viewHolder;
    private int mCurscreen;
    private String checkBoxPreferenceTest1 = "checkBoxPreferenceTest1";
    private String checkBoxPreferenceTest2 = "checkBoxPreferenceTest2";
    private String checkBoxPreferenceTest3 = "checkBoxPreferenceTest3";
    private String checkBoxPreferenceTest4 = "checkBoxPreferenceTest4";
    private String timeDifferenceBetweenOldandNewString = "timeDifferenceBetweenOldandNewString";
    private String lifeOfPlayerString = "lifeOfPlayer";
    private RewardedVideoAd mRewardedVideoAd;
    final static String TAG = "HomeFragment";
    private Context context;



    public HomeFragment() {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Log.d(TAG, "onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Log.d(TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
//        Log.d(TAG, "onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Log.d(TAG, "onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.d(TAG, "onRewarded");
        int currentLifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
        SharedPreferences.Editor increaseLifeEditor = stats.edit();
        int increasedLifeofPlayer = currentLifeOfPlayer + 1;
        increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
        increaseLifeEditor.apply();
        updateUIAfterProgress();
//        checkTimeBetweenAppCloseandOpen();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Log.d(TAG, "onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
//        Log.d(TAG, "onRewardedVideoAdFailedToLoad");
    }


    private static class HomeViewHolder {
        CheckBox questionType1, questionType2, questionType3, questionType4;
        ImageButton showRewardedVideoButton;
        TextView remainingLifeTextView, remainingTimeTextView;
        LinearLayout rewardPassingTime;
        ImageButton startButton;
// singlePlayer, multiPlayer;
    }

    // buraya koyacagimiz listener ve callbackler ile DataAdapter a pasliyacaz.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        mCurscreen = R.layout.activity_main;
        Log.d(TAG, "HomeFragment started");
        onStartBoolean = true;
//        onStopBoolean = false;
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        context=getContext();
        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        if (rootView != null) {
            viewHolder = new HomeViewHolder();
            viewHolder.questionType1 = rootView.findViewById(R.id.select_question_type1);
            viewHolder.questionType1.setOnCheckedChangeListener(this);
            viewHolder.questionType2 = rootView.findViewById(R.id.select_question_type2);
            viewHolder.questionType2.setOnCheckedChangeListener(this);
            viewHolder.questionType3 = rootView.findViewById(R.id.select_question_type3);
            viewHolder.questionType3.setOnCheckedChangeListener(this);
            viewHolder.questionType4 = rootView.findViewById(R.id.select_question_type4);
            viewHolder.questionType4.setOnCheckedChangeListener(this);
            viewHolder.showRewardedVideoButton = rootView.findViewById(R.id.reward_video);
            viewHolder.showRewardedVideoButton.setOnClickListener(this);
            viewHolder.remainingLifeTextView = rootView.findViewById(R.id.remaining_life);
            viewHolder.remainingTimeTextView = rootView.findViewById(R.id.remaining_time);
            viewHolder.rewardPassingTime = rootView.findViewById(R.id.layoutTimeReward);
            viewHolder.startButton = rootView.findViewById(R.id.start_button);
            viewHolder.startButton.setOnClickListener(this);
        }
        //her onStop ve onStart sonrası telefonun zamanı alınacak
        // eğer onStart çalışmadan sadece onResume devreye girmişse o zaman onResume de alınacak
        MainActivity.checkTimeBetweenAppCloseandOpen(context);
        if (stats.getInt(lifeOfPlayerString, 3) < 3) {
            updateLifeandTime(getTimeBetweenAppCloseandOpen() + getTimeDuringTimerWorks());
        }
        updateUIAfterProgress();
        getCheckBoxPreference();
        Log.d(TAG, "counterWorks " + stats.getBoolean(counterWorks, false));
        if (rootView != null) {
            mAdView = rootView.findViewById(R.id.adView_home_banner);
        }
        if (stats.getBoolean(counterWorks, false)) {
            if (lifeTimer != null) {
                lifeTimer.cancel();
                Log.d(TAG, "lifeTimer cancelled ");

            }
            initCountdownTimer(timeForNewLife - getTimeDuringTimerWorks());
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        assert rootView != null;
        return rootView;
    }

    //hak 3 oldugunda tum sayaclari sifirlamak icin
    private void clearTimePreferences() {
        SharedPreferences.Editor clearEditor = stats.edit();
        String newCountDownTime = "newCountDownTime";
        clearEditor.remove(newCountDownTime);
        clearEditor.remove(timeBetweenActivitiesPreference);
//        clearEditor.remove(lifeOfPlayerString);
        clearEditor.remove(timeDifferenceBetweenOldandNewString);
        clearEditor.apply();
    }

    private void determineCounterWorking(boolean workingStatus) {
        SharedPreferences.Editor counterBoolean = stats.edit();
        counterBoolean.putBoolean(counterWorks, workingStatus);
        counterBoolean.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reward_video:
//                Log.d(TAG, "reklama tıklandı");
                showRewardedAd();
                break;
            case R.id.start_button:
                determineCounterWorking(true);
                int currentLifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
                if (currentLifeOfPlayer != 0) {
                    reduceLifeOfUser();
                    SharedPreferences.Editor clearEditor = stats.edit();
                    String newCountDownTime = "newCountDownTime";
                    clearEditor.remove(newCountDownTime);
                    clearEditor.remove(timeDifferenceBetweenOldandNewString);
                    clearEditor.apply();
                    MainActivity.checkTimeBetweenAppCloseandOpen(context);
                    determineTestDatabase();
                    SharedPreferences tempStats = getContext().getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
                    stats.edit().remove(questionCountPreference).apply();
                    tempStats.edit().clear().apply();
                    TempDatabaseHelper tempDbHelper = new TempDatabaseHelper(getContext());
                    TestFragment.tempSqlDb = tempDbHelper.getWritableDatabase();
                    TestFragment.tempSqlDb.delete(DataContract.StatisticsTable.TEMP_TABLE_NAME, null, null);
                    TestFragment.tempSqlDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TEMP_TABLE_NAME + "'");
                    // bir fazla ilerledigi icin 51
                    stats.edit().remove(questionCountPreference).apply();
                    SharedPreferences loginStats = getContext().getSharedPreferences(tempUserStats, Context.MODE_PRIVATE);
                    loginStats.edit().clear().apply();
                    Intent startIntent = new Intent(getActivity(), TestActivity.class);
                    startActivity(startIntent);
                } else {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.no_life), Toast.LENGTH_LONG);
                    toast.show();
                }
        }

    }

    // burasi bir onceki hak 3den kucuk olma durumunda calisacak
    private void updateLifeandTime(long timeDifferenceBetweenOldandNew) {
        SharedPreferences.Editor increaseLifeEditor = stats.edit();
        int currentLifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
        Log.d(TAG, "currentLifeOfPlayer" + currentLifeOfPlayer);
        //gecen surenin hak kazanmas suresinin katlarina gore ayarlanmasi
        //hic hak kazanmamis durum: sadece yeni sure belirlenir
        if (timeDifferenceBetweenOldandNew < timeForNewLife) {
            Log.d(TAG, "1.durum");
            increaseLifeEditor.putLong(timeBetweenActivitiesPreference, timeDifferenceBetweenOldandNew);
            increaseLifeEditor.apply();
            determineCounterWorking(true);
            Log.d(TAG, "time" + timeDifferenceBetweenOldandNew / 1000);
        }
        //1 hak kazanmis durum: mevcut hak 2 ise 1 eklenir sayac durdurulur,
        // 2den kucuk ise 1 eklenir sayac devam eder
        else if (timeDifferenceBetweenOldandNew >= timeForNewLife
                & timeDifferenceBetweenOldandNew < 2 * timeForNewLife) {
            Log.d(TAG, "2.durum");
            if (currentLifeOfPlayer < 2) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 1;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                //burada geçen zamanlar kaydedilecek
                long newTimeAfterClosing = (timeDifferenceBetweenOldandNew - timeForNewLife);
                SharedPreferences.Editor timeBetweenActivities = stats.edit();
                timeBetweenActivities.putLong(timeBetweenActivitiesPreference, newTimeAfterClosing);
                timeBetweenActivities.apply();
                determineCounterWorking(true);
//                clearTimePreferences();
//                MainActivity.checkTimeBetweenAppCloseandOpen();
            } else if (currentLifeOfPlayer == 2) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 1;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            }
        }
        //2 hak kazanmis durum: mevcut hak 1-2 ise haklar eklenir sayac durdurulur,
        // 0 ise 2 eklenir sayac devam eder
        else if (timeDifferenceBetweenOldandNew >= 2 * timeForNewLife
                & timeDifferenceBetweenOldandNew < 3 * timeForNewLife) {
            Log.d(TAG, "3.durum");
            if (currentLifeOfPlayer == 2) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 1;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            } else if (currentLifeOfPlayer == 1) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 2;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            } else if (currentLifeOfPlayer == 0) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 2;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                long newTimeAfterClosing2 = (timeDifferenceBetweenOldandNew - 2 * timeForNewLife);
                SharedPreferences.Editor timeBetweenActivities = stats.edit();
                timeBetweenActivities.putLong(timeBetweenActivitiesPreference, newTimeAfterClosing2);
                timeBetweenActivities.apply();
                determineCounterWorking(true);
//                clearTimePreferences();
//                MainActivity.checkTimeBetweenAppCloseandOpen();
            }
        }
        // 3 hak kazanmis durum: mevcut hak 0-1-2 ise haklar eklenir sayac durdurulur,
        else if (timeDifferenceBetweenOldandNew >= 3 * timeForNewLife) {
            Log.d(TAG, "4.durum");
            if (currentLifeOfPlayer == 2) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 1;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            } else if (currentLifeOfPlayer == 1) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 2;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            } else if (currentLifeOfPlayer == 0) {
                int increasedLifeofPlayer = currentLifeOfPlayer + 3;
                increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                increaseLifeEditor.apply();
                determineCounterWorking(false);
                clearTimePreferences();
            }
        }
    }

    //geri sayım sayacı başladığı andaki vakit alınır
    private long getTimeBetweenAppCloseandOpen() {
        Log.d(TAG, "getTimeBetweenAppCloseandOpen" + stats.getLong(timeDifferenceBetweenOldandNewString, 0) / 1000);
        return stats.getLong(timeDifferenceBetweenOldandNewString, 0);
    }

    private long getTimeDuringTimerWorks() {
        Log.d(TAG, "getTimeDuringTimerWorks" + stats.getLong(timeBetweenActivitiesPreference, 0) / 1000);
        return stats.getLong(timeBetweenActivitiesPreference, 0);
    }

    private void updateUIAfterProgress() {
        int currentLifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
        viewHolder.remainingLifeTextView.setText(String.valueOf(currentLifeOfPlayer));
        if (currentLifeOfPlayer == 3) {
            viewHolder.rewardPassingTime.setVisibility(View.INVISIBLE);
            viewHolder.showRewardedVideoButton.setEnabled(false);
            viewHolder.startButton.setBackgroundResource(R.drawable.selector_startbutton);
            clearTimePreferences();
        } else if (currentLifeOfPlayer == 0) {
            viewHolder.rewardPassingTime.setVisibility(View.VISIBLE);
            viewHolder.showRewardedVideoButton.setEnabled(true);
            viewHolder.startButton.setBackgroundResource(R.drawable.start_buttondisabled);
        } else {
            viewHolder.rewardPassingTime.setVisibility(View.VISIBLE);
            viewHolder.showRewardedVideoButton.setEnabled(true);
            viewHolder.startButton.setBackgroundResource(R.drawable.selector_startbutton);
        }
    }

    //app acik kaldigi surece bu sayac kullanilacak
    private void initCountdownTimer(long startTime) {
        Log.d(TAG, "initCountdownTimer called starttime " + startTime);
        lifeTimer = new CountDownTimer(startTime, 1000) {
            //millisUntilFinished The amount of time until finished.
            public void onTick(long millis) {
                long sn = millis / 1000;
                @SuppressLint("DefaultLocale") String elapsedTime = String.format("%02d:%02d:%02d", sn / 3600,(sn % 3600) / 60, (sn % 60));
                viewHolder.remainingTimeTextView.setText(elapsedTime);
//                Log.d("Activityler","tick counting" + millis);
                //geçen zamanı kaydetmek için toplam süreden çıkarılıyor
                SharedPreferences.Editor timeBetweenActivities = stats.edit();
                timeBetweenActivities.putLong(timeBetweenActivitiesPreference, timeForNewLife - millis);
                timeBetweenActivities.apply();
            }

            public void onFinish() {
                //onFinish tekrar devreye girmemesi için
                if (timeForNewLife - getTimeDuringTimerWorks() < 2000) {
                    Log.d(TAG, "on Finish called");
                    int currentLifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
//                    Log.d(TAG, "currentLifeOfPlayer" + currentLifeOfPlayer);
                    SharedPreferences.Editor increaseLifeEditor = stats.edit();
                    clearTimePreferences();
                    final int increasedLifeofPlayer = currentLifeOfPlayer + 1;
                    increaseLifeEditor.putInt(lifeOfPlayerString, increasedLifeofPlayer);
                    increaseLifeEditor.apply();
                    // hala can 3den kucuk ise sayac devam edecek
                    Log.d(TAG, "increasedLifeofPlayer " + increasedLifeofPlayer);
                    if (increasedLifeofPlayer > 2) {
                        determineCounterWorking(false);
                    } else {
                        determineCounterWorking(true);
                        MainActivity.checkTimeBetweenAppCloseandOpen(context);
                        initCountdownTimer(timeForNewLife);
                    }
//                    Log.d(TAG, "onStopBoolean "+ onStopBoolean);
                    if (mCurscreen == R.layout.activity_main) {
                        SharedPreferences.Editor timeBetweenActivities = stats.edit();
                        timeBetweenActivities.putLong(timeBetweenActivitiesPreference, 0);
                        timeBetweenActivities.apply();
                        Intent refreshViewIntent = new Intent(getContext(), TabActivity.class);
                        startActivity(refreshViewIntent);
                    }
                }
            }
        };
        lifeTimer.start();
    }


    private void showRewardedAd() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            String noNetwork = getString(R.string.video_failed);
            Toast toast = Toast.makeText(getContext(), noNetwork, Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void reduceLifeOfUser() {
        int lifeOfPlayer = stats.getInt(lifeOfPlayerString, 3);
        SharedPreferences.Editor rewardEdit = stats.edit();
        int reducedLife = lifeOfPlayer - 1;
        rewardEdit.putInt(lifeOfPlayerString, reducedLife);
        rewardEdit.apply();
    }

    private void getCheckBoxPreference() {
//        Log.d(TAG, "getCheckBoxPreference called");
        viewHolder.questionType1.setChecked(stats.getBoolean(checkBoxPreferenceTest1, true));
        viewHolder.questionType2.setChecked(stats.getBoolean(checkBoxPreferenceTest2, false));
        viewHolder.questionType3.setChecked(stats.getBoolean(checkBoxPreferenceTest3, false));
        viewHolder.questionType4.setChecked(stats.getBoolean(checkBoxPreferenceTest4, false));
    }

    private void loadRewardedVideoAd() {

//        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
//                new AdRequest.Builder().build());
        mRewardedVideoAd.loadAd(getString(R.string.reward_ads_id),
                new AdRequest.Builder().build());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor checkEditor = stats.edit();
//        Log.d(TAG, "onCheckedChanged called");
        switch (buttonView.getId()) {
            case R.id.select_question_type1:
                if (isChecked) {
                    checkEditor.putBoolean(checkBoxPreferenceTest1, true);
                    checkEditor.putBoolean(checkBoxPreferenceTest2, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest3, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest4, false);
                    checkEditor.apply();
//                    Log.d(TAG, "select_question_type1 onCheckedChanged called");
                    viewHolder.questionType1.setChecked(true);
                    viewHolder.questionType2.setChecked(false);
                    viewHolder.questionType3.setChecked(false);
                    viewHolder.questionType4.setChecked(false);
                    viewHolder.questionType1.setEnabled(false);
                    viewHolder.questionType2.setEnabled(true);
                    viewHolder.questionType3.setEnabled(true);
                    viewHolder.questionType4.setEnabled(true);
                }
                break;
            case R.id.select_question_type2:
                if (isChecked) {
                    checkEditor.putBoolean(checkBoxPreferenceTest1, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest2, true);
                    checkEditor.putBoolean(checkBoxPreferenceTest3, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest4, false);
                    checkEditor.apply();
//                    Log.d(TAG, "select_question_type2 onCheckedChanged called");
                    viewHolder.questionType1.setChecked(false);
                    viewHolder.questionType2.setChecked(true);
                    viewHolder.questionType3.setChecked(false);
                    viewHolder.questionType4.setChecked(false);
                    viewHolder.questionType1.setEnabled(true);
                    viewHolder.questionType2.setEnabled(false);
                    viewHolder.questionType3.setEnabled(true);
                    viewHolder.questionType4.setEnabled(true);
                }

                break;
            case R.id.select_question_type3:
                if (isChecked) {
                    checkEditor.putBoolean(checkBoxPreferenceTest1, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest2, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest3, true);
                    checkEditor.putBoolean(checkBoxPreferenceTest4, false);
                    checkEditor.apply();
//                    Log.d(TAG, "select_question_type3 onCheckedChanged called");
                    viewHolder.questionType1.setChecked(false);
                    viewHolder.questionType2.setChecked(false);
                    viewHolder.questionType3.setChecked(true);
                    viewHolder.questionType4.setChecked(false);
                    viewHolder.questionType1.setEnabled(true);
                    viewHolder.questionType2.setEnabled(true);
                    viewHolder.questionType3.setEnabled(false);
                    viewHolder.questionType4.setEnabled(true);
                }
                break;
            case R.id.select_question_type4:
                if (isChecked) {
                    checkEditor.putBoolean(checkBoxPreferenceTest1, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest2, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest3, false);
                    checkEditor.putBoolean(checkBoxPreferenceTest4, true);
                    checkEditor.apply();
                    viewHolder.questionType1.setChecked(false);
                    viewHolder.questionType2.setChecked(false);
                    viewHolder.questionType3.setChecked(false);
                    viewHolder.questionType4.setChecked(true);
                    viewHolder.questionType1.setEnabled(true);
                    viewHolder.questionType2.setEnabled(true);
                    viewHolder.questionType3.setEnabled(true);
                    viewHolder.questionType4.setEnabled(false);
                }
                break;
        }
    }

    private void determineTestDatabase() {
//        Log.d(TAG, "determineTestDatabase is called");
        if (viewHolder.questionType1.isChecked()) {
            databaseNumber = DATABASE_TOEFL_NUMBER;
        } else if (viewHolder.questionType2.isChecked()) {
            databaseNumber = DATABASE_IELTS_NUMBER;
        } else if (viewHolder.questionType3.isChecked()) {
            databaseNumber = DATABASE_GRE_NUMBER;
        } else if (viewHolder.questionType4.isChecked()) {
            databaseNumber = DATABASE_TOEIC_NUMBER;
        }

    }


    @Override
    public void onPause() {
        Log.d(TAG, "HomeFragment paused");
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        onStartBoolean = false;
//        onStopBoolean = true;
        Log.d(TAG, "HomeFragment stopped");
        if (stats.getBoolean(counterWorks, false)) {
            if (lifeTimer != null) {
                lifeTimer.cancel();
                Log.d(TAG, "lifeTimer cancelled ");
                SharedPreferences.Editor clearEditor = stats.edit();
                String newCountDownTime = "newCountDownTime";
                clearEditor.remove(newCountDownTime);
                clearEditor.remove(timeDifferenceBetweenOldandNewString);
                clearEditor.apply();
                MainActivity.checkTimeBetweenAppCloseandOpen(context);
            }
        }

        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "HomeFragment onResume");
        //if ondestroy and not onstart die bi şey eklenebilir
        Log.d(TAG, "onStartBoolean " + onStartBoolean);

        MainActivity.checkTimeBetweenAppCloseandOpen(context);
        if (!onStartBoolean) {
            if (stats.getInt(lifeOfPlayerString, 3) < 3) {
                updateLifeandTime(getTimeBetweenAppCloseandOpen() + getTimeDuringTimerWorks());
            }
            updateUIAfterProgress();
            if (stats.getBoolean(counterWorks, false)) {
                if (lifeTimer != null) {
                    lifeTimer.cancel();
                    Log.d(TAG, "lifeTimer cancelled ");
                }
                initCountdownTimer(timeForNewLife - getTimeDuringTimerWorks());
            }
        }
//        onStopBoolean = false;
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (TestFragment.timer != null) {
            TestFragment.timer.cancel();
//            Log.d(TAG, "timer cancelled");
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "HomeFragment onDestroy");
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}

