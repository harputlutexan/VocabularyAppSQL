package com.englishvocabularygame.evog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.englishvocabularygame.evog.Statistics.ProfileArrayAdapter;
import com.englishvocabularygame.evog.Statistics.Results;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.englishvocabularygame.evog.Fragments.TestFragment.listFilename;
import static com.englishvocabularygame.evog.Fragments.TestFragment.updatedList;


// profile sayfasi cikacak sonrasinda eger login var ise intent ile TabActivity cikacak
public class MainActivity extends AppCompatActivity {
    Handler handler;
    Runnable mNextActivityCallback;
    String TAG = "Mainactivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_page);
        MobileAds.initialize(this, getString(R.string.addmop_id));
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        initArrayList();
        ProfileArrayAdapter arrayAdapter = new ProfileArrayAdapter(this, 0, updatedList);
        arrayAdapter.notifyDataSetChanged();
        handler = new Handler();
        mNextActivityCallback = new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(getApplicationContext(), TabActivity.class);
                startActivity(loginIntent);
                finish();
            }
        };
        handler.postDelayed(mNextActivityCallback, 2000);
    }


    @Override
    public void onBackPressed() {
    }

    // appin kapali oldugu sure
    public static void checkTimeBetweenAppCloseandOpen(Context context) {
        String newCountDownTime = "newCountDownTime";
        String timeDifferenceBetweenOldandNewString = "timeDifferenceBetweenOldandNewString";
//        String timeBetweenActivitiesPreference = "timeBetweenActivitiesPreference";
//        long previousAppOpenTime = stats.getLong(timeBetweenActivitiesPreference, timeForNewLife);
        String userStats = "userStats";
        Log.d("MainActivity","contect "  + context);
        if (context!=null) {
            SharedPreferences stats = context.getSharedPreferences(userStats, Context.MODE_PRIVATE);
            SharedPreferences.Editor timeEditor = stats.edit();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            String newTime = dateFormat.format(Calendar.getInstance().getTime());
            try {
                Date currentDate = dateFormat.parse(newTime);
                long newTimeAsInt = currentDate.getTime();
                long oldTimeInitial = stats.getLong(newCountDownTime, newTimeAsInt);
                long timeDifferenceBetweenOldandNew = newTimeAsInt - oldTimeInitial;
                Log.d("HomeFragment", "newTimeAsInt " + newTimeAsInt / 1000
                        + " oldTimeInitial " + oldTimeInitial / 1000
                        + " timeDifferenceBetweenOldandNewString " + timeDifferenceBetweenOldandNew);

                //bu alttaki artik bir oncekinin oldu olarak kaydediliyor aslinda
                timeEditor.putLong(newCountDownTime, newTimeAsInt);
                timeEditor.putLong(timeDifferenceBetweenOldandNewString, timeDifferenceBetweenOldandNew);
                timeEditor.apply();
//            Log.d(TAG, "newTimeAsInt" + newTimeAsInt);
//            Log.d(TAG, "oldTimeInitial" + oldTimeInitial);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void initArrayList() {
        updatedList = new ArrayList<>();
        updatedList = readArrayListFromSD(this, listFilename);
        if (updatedList!=null) {
            if (updatedList.size() == 0) {
                updatedList = new ArrayList<>();
            }
        }
    }


    public static ArrayList<Results> readArrayListFromSD(Context mContext, String filename) {
        try {
            File dir = mContext.getFilesDir();
            FileInputStream fileInputStream = new FileInputStream(dir + filename);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList<Results> obj = (ArrayList<Results>) ois.readObject();
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Results>();
        }
    }


}
//        Calendar calendar = Calendar.getInstance();
//        long newTime = calendar.get(Calendar.SECOND);
//        long newTimeday = calendar.get(Calendar.DAY_OF_YEAR);
//        long newTimeweek = calendar.get(Calendar.WEEK_OF_YEAR);
//        long newTimemonth = calendar.get(Calendar.MONTH);