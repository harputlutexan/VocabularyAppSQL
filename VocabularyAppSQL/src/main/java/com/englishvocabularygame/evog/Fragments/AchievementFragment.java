package com.englishvocabularygame.evog.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.englishvocabularygame.evog.CustomToast;
import com.englishvocabularygame.evog.Data.StatisticsSqlDatabaseHelper;
import com.englishvocabularygame.evog.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import static android.app.Activity.RESULT_OK;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.COLUMN_ACCURACY;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.COLUMN_TEST_TYPE;
import static com.englishvocabularygame.evog.Data.DataContract.StatisticsTable.TABLE_NAME;

//import com.google.example.games.basegameutils.BaseGameUtils;

/**
 * Created  on 7.10.2016.
 */

public class AchievementFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = true;
    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;
    private static final String TAG = "AchievementFragment";
    //   RequestCode is an arbitrary integer used as the request code
    private static final int ACHIVEMENT_REQUEST_CODE = 2222;
    private SQLiteDatabase newSQLDatabase;
    //    private ViewGroup container;
    private View v;
    AchievementsViewHolder achViewHolder;


    public AchievementFragment() {
    }

    private static class AchievementsViewHolder {
        ImageView TOEICIach, TOEICIIach, TOEICIIIach, TOEICIVach, TOEICVach, TOEICVIach,
                TOEICIcheckMark, TOEICIIcheckMark, TOEICIIIcheckMark, TOEICIVcheckMark,
                TOEICVcheckMark, TOEICVIcheckMark,
                TOEFLIach, TOEFLIIach, TOEFLIIIach, TOEFLIVach, TOEFLVach, TOEFLVIach,
                TOEFLIcheckMark, TOEFLIIcheckMark, TOEFLIIIcheckMark, TOEFLIVcheckMark,
                TOEFLVcheckMark, TOEFLVIcheckMark,
                GREIach, GREIIach, GREIIIach, GREIVach, GREVach, GREVIach,
                GREIcheckMark, GREIIcheckMark, GREIIIcheckMark, GREIVcheckMark,
                GREVcheckMark, GREVIcheckMark,
                IELTSIach, IELTSIIach, IELTSIIIach, IELTSIVach, IELTSVach, IELTSVIach,
                IELTSIcheckMark, IELTSIIcheckMark, IELTSIIIcheckMark,
                IELTSIVcheckMark, IELTSVcheckMark, IELTSVIcheckMark;
        LinearLayout TOEICIProgressBar, TOEICIIProgressBar, TOEICIIIProgressBar,
                TOEICIVProgressBar, TOEICVProgressBar, TOEICVIProgressBar,
                GREIProgressBar, GREIIProgressBar, GREIIIProgressBar,
                GREIVProgressBar, GREVProgressBar, GREVIProgressBar,
                TOEFLIProgressBar, TOEFLIIProgressBar, TOEFLIIIProgressBar,
                TOEFLIVProgressBar, TOEFLVProgressBar, TOEFLVIProgressBar,
                IELTSIProgressBar,
                IELTSIIProgressBar, IELTSIIIProgressBar, IELTSIVProgressBar, IELTSVProgressBar, IELTSVIProgressBar;
        TextView TOEICIProgressRatio, TOEICIIProgressRatio, TOEICIIIProgressRatio,
                TOEICIVProgressRatio, TOEICVProgressRatio, TOEICVIProgressRatio,
                GREIProgressRatio, GREIIProgressRatio, GREIIIProgressRatio,
                GREIVProgressRatio, GREVProgressRatio, GREVIProgressRatio,
                TOEFLIProgressRatio, TOEFLIIProgressRatio, TOEFLIIIProgressRatio,
                TOEFLIVProgressRatio, TOEFLVProgressRatio, TOEFLVIProgressRatio,
                IELTSIProgressRatio, IELTSIIProgressRatio, IELTSIIIProgressRatio,
                IELTSIVProgressRatio, IELTSVProgressRatio, IELTSVIProgressRatio;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.achievements_fragment, container, false);
        Log.d(TAG, "onCreate()");
        // buranin basina kesinlikle check internet status koymak lazim eger baglanti var ise bunu yapmali
        ConnectivityManager cm = (ConnectivityManager) getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm!=null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                    .enableAutoManage(getActivity(), this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Games.API)
                        .addScope(Games.SCOPE_GAMES)
                        .build();
            }
        }
//        mAdView = (AdView) rootView.findViewById(R.id.adView_topBanner_transitionPage);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        achViewHolder = new AchievementsViewHolder();

        achViewHolder.IELTSIach = rootView.findViewById(R.id.IELTS_i_ach_image);
        achViewHolder.IELTSIIach = rootView.findViewById(R.id.IELTS_ii_ach_image);
        achViewHolder.IELTSIIIach = rootView.findViewById(R.id.IELTS_iii_ach_image);
        achViewHolder.IELTSIVach = rootView.findViewById(R.id.IELTS_iv_ach_image);
        achViewHolder.IELTSVach = rootView.findViewById(R.id.IELTS_v_ach_image);
        achViewHolder.IELTSVIach = rootView.findViewById(R.id.IELTS_vi_ach_image);

        achViewHolder.TOEICIach = rootView.findViewById(R.id.TOEIC_i_ach_image);
        achViewHolder.TOEICIIach = rootView.findViewById(R.id.TOEIC_ii_ach_image);
        achViewHolder.TOEICIIIach = rootView.findViewById(R.id.TOEIC_iii_ach_image);
        achViewHolder.TOEICIVach = rootView.findViewById(R.id.TOEIC_iv_ach_image);
        achViewHolder.TOEICVach = rootView.findViewById(R.id.TOEIC_v_ach_image);
        achViewHolder.TOEICVIach = rootView.findViewById(R.id.TOEIC_vi_ach_image);

        achViewHolder.TOEFLIach = rootView.findViewById(R.id.TOEFL_i_ach_image);
        achViewHolder.TOEFLIIach = rootView.findViewById(R.id.TOEFL_ii_ach_image);
        achViewHolder.TOEFLIIIach = rootView.findViewById(R.id.TOEFL_iii_ach_image);
        achViewHolder.TOEFLIVach = rootView.findViewById(R.id.TOEFL_iv_ach_image);
        achViewHolder.TOEFLVach = rootView.findViewById(R.id.TOEFL_v_ach_image);
        achViewHolder.TOEFLVIach = rootView.findViewById(R.id.TOEFL_vi_ach_image);

        achViewHolder.GREIach = rootView.findViewById(R.id.GRE_i_ach_image);
        achViewHolder.GREIIach = rootView.findViewById(R.id.GRE_ii_ach_image);
        achViewHolder.GREIIIach = rootView.findViewById(R.id.GRE_iii_ach_image);
        achViewHolder.GREIVach = rootView.findViewById(R.id.GRE_iv_ach_image);
        achViewHolder.GREVach = rootView.findViewById(R.id.GRE_v_ach_image);
        achViewHolder.GREVIach = rootView.findViewById(R.id.GRE_vi_ach_image);

        achViewHolder.IELTSIcheckMark = rootView.findViewById(R.id.IELTS_i_chk_img);
        achViewHolder.IELTSIIcheckMark = rootView.findViewById(R.id.IELTS_ii_chk_img);
        achViewHolder.IELTSIIIcheckMark = rootView.findViewById(R.id.IELTS_iii_chk_img);
        achViewHolder.IELTSIVcheckMark = rootView.findViewById(R.id.IELTS_iv_chk_img);
        achViewHolder.IELTSVcheckMark = rootView.findViewById(R.id.IELTS_v_chk_img);
        achViewHolder.IELTSVIcheckMark = rootView.findViewById(R.id.IELTS_vi_chk_img);

        achViewHolder.TOEICIcheckMark = rootView.findViewById(R.id.TOEIC_i_chk_img);
        achViewHolder.TOEICIIcheckMark = rootView.findViewById(R.id.TOEIC_ii_chk_img);
        achViewHolder.TOEICIIIcheckMark = rootView.findViewById(R.id.TOEIC_iii_chk_img);
        achViewHolder.TOEICIVcheckMark = rootView.findViewById(R.id.TOEIC_iv_chk_img);
        achViewHolder.TOEICVcheckMark = rootView.findViewById(R.id.TOEIC_v_chk_img);
        achViewHolder.TOEICVIcheckMark = rootView.findViewById(R.id.TOEIC_vi_chk_img);

        achViewHolder.GREIcheckMark = rootView.findViewById(R.id.GRE_i_chk_img);
        achViewHolder.GREIIcheckMark = rootView.findViewById(R.id.GRE_ii_chk_img);
        achViewHolder.GREIIIcheckMark = rootView.findViewById(R.id.GRE_iii_chk_img);
        achViewHolder.GREIVcheckMark = rootView.findViewById(R.id.GRE_iv_chk_img);
        achViewHolder.GREVcheckMark = rootView.findViewById(R.id.GRE_v_chk_img);
        achViewHolder.GREVIcheckMark = rootView.findViewById(R.id.GRE_vi_chk_img);

        achViewHolder.TOEFLIcheckMark = rootView.findViewById(R.id.TOEFL_i_chk_img);
        achViewHolder.TOEFLIIcheckMark = rootView.findViewById(R.id.TOEFL_ii_chk_img);
        achViewHolder.TOEFLIIIcheckMark = rootView.findViewById(R.id.TOEFL_iii_chk_img);
        achViewHolder.TOEFLIVcheckMark = rootView.findViewById(R.id.TOEFL_iv_chk_img);
        achViewHolder.TOEFLVcheckMark = rootView.findViewById(R.id.TOEFL_v_chk_img);
        achViewHolder.TOEFLVIcheckMark = rootView.findViewById(R.id.TOEFL_vi_chk_img);

        achViewHolder.IELTSIProgressBar = rootView.findViewById(R.id.IELTS_i_prgrss_bar);
        achViewHolder.IELTSIIProgressBar = rootView.findViewById(R.id.IELTS_ii_prgrss_bar);
        achViewHolder.IELTSIIIProgressBar = rootView.findViewById(R.id.IELTS_iii_prgrss_bar);
        achViewHolder.IELTSIVProgressBar = rootView.findViewById(R.id.IELTS_iv_prgrss_bar);
        achViewHolder.IELTSVProgressBar = rootView.findViewById(R.id.IELTS_v_prgrss_bar);
        achViewHolder.IELTSVIProgressBar = rootView.findViewById(R.id.IELTS_vi_prgrss_bar);

        achViewHolder.TOEICIProgressBar = rootView.findViewById(R.id.TOEIC_i_prgrss_bar);
        achViewHolder.TOEICIIProgressBar = rootView.findViewById(R.id.TOEIC_ii_prgrss_bar);
        achViewHolder.TOEICIIIProgressBar = rootView.findViewById(R.id.TOEIC_iii_prgrss_bar);
        achViewHolder.TOEICIVProgressBar = rootView.findViewById(R.id.TOEIC_iv_prgrss_bar);
        achViewHolder.TOEICVProgressBar = rootView.findViewById(R.id.TOEIC_v_prgrss_bar);
        achViewHolder.TOEICVIProgressBar = rootView.findViewById(R.id.TOEIC_vi_prgrss_bar);

        achViewHolder.TOEFLIProgressBar = rootView.findViewById(R.id.TOEFL_i_prgrss_bar);
        achViewHolder.TOEFLIIProgressBar = rootView.findViewById(R.id.TOEFL_ii_prgrss_bar);
        achViewHolder.TOEFLIIIProgressBar = rootView.findViewById(R.id.TOEFL_iii_prgrss_bar);
        achViewHolder.TOEFLIVProgressBar = rootView.findViewById(R.id.TOEFL_iv_prgrss_bar);
        achViewHolder.TOEFLVProgressBar = rootView.findViewById(R.id.TOEFL_v_prgrss_bar);
        achViewHolder.TOEFLVIProgressBar = rootView.findViewById(R.id.TOEFL_vi_prgrss_bar);

        achViewHolder.GREIProgressBar = rootView.findViewById(R.id.GRE_i_prgrss_bar);
        achViewHolder.GREIIProgressBar = rootView.findViewById(R.id.GRE_ii_prgrss_bar);
        achViewHolder.GREIIIProgressBar = rootView.findViewById(R.id.GRE_iii_prgrss_bar);
        achViewHolder.GREIVProgressBar = rootView.findViewById(R.id.GRE_iv_prgrss_bar);
        achViewHolder.GREVProgressBar = rootView.findViewById(R.id.GRE_v_prgrss_bar);
        achViewHolder.GREVIProgressBar = rootView.findViewById(R.id.GRE_vi_prgrss_bar);

        achViewHolder.IELTSIProgressRatio = rootView.findViewById(R.id.IELTS_i_prgrss_ratio);
        achViewHolder.IELTSIIProgressRatio = rootView.findViewById(R.id.IELTS_ii_prgrss_ratio);
        achViewHolder.IELTSIIIProgressRatio = rootView.findViewById(R.id.IELTS_iii_prgrss_ratio);
        achViewHolder.IELTSIVProgressRatio = rootView.findViewById(R.id.IELTS_iv_prgrss_ratio);
        achViewHolder.IELTSVProgressRatio = rootView.findViewById(R.id.IELTS_v_prgrss_ratio);
        achViewHolder.IELTSVIProgressRatio = rootView.findViewById(R.id.IELTS_vi_prgrss_ratio);

        achViewHolder.TOEICIProgressRatio = rootView.findViewById(R.id.TOEIC_i_prgrss_ratio);
        achViewHolder.TOEICIIProgressRatio = rootView.findViewById(R.id.TOEIC_ii_prgrss_ratio);
        achViewHolder.TOEICIIIProgressRatio = rootView.findViewById(R.id.TOEIC_iii_prgrss_ratio);
        achViewHolder.TOEICIVProgressRatio = rootView.findViewById(R.id.TOEIC_iv_prgrss_ratio);
        achViewHolder.TOEICVProgressRatio = rootView.findViewById(R.id.TOEIC_v_prgrss_ratio);
        achViewHolder.TOEICVIProgressRatio = rootView.findViewById(R.id.TOEIC_vi_prgrss_ratio);

        achViewHolder.TOEFLIProgressRatio = rootView.findViewById(R.id.TOEFL_i_prgrss_ratio);
        achViewHolder.TOEFLIIProgressRatio = rootView.findViewById(R.id.TOEFL_ii_prgrss_ratio);
        achViewHolder.TOEFLIIIProgressRatio = rootView.findViewById(R.id.TOEFL_iii_prgrss_ratio);
        achViewHolder.TOEFLIVProgressRatio = rootView.findViewById(R.id.TOEFL_iv_prgrss_ratio);
        achViewHolder.TOEFLVProgressRatio = rootView.findViewById(R.id.TOEFL_v_prgrss_ratio);
        achViewHolder.TOEFLVIProgressRatio = rootView.findViewById(R.id.TOEFL_vi_prgrss_ratio);

        achViewHolder.GREIProgressRatio = rootView.findViewById(R.id.GRE_i_prgrss_ratio);
        achViewHolder.GREIIProgressRatio = rootView.findViewById(R.id.GRE_ii_prgrss_ratio);
        achViewHolder.GREIIIProgressRatio = rootView.findViewById(R.id.GRE_iii_prgrss_ratio);
        achViewHolder.GREIVProgressRatio = rootView.findViewById(R.id.GRE_iv_prgrss_ratio);
        achViewHolder.GREVProgressRatio = rootView.findViewById(R.id.GRE_v_prgrss_ratio);
        achViewHolder.GREVIProgressRatio = rootView.findViewById(R.id.GRE_vi_prgrss_ratio);



//        updateAchievements();
        goAchievements(rootView);
        updateAchievements();
        return rootView;
    }
    //onStart because that is the state when the activity becomes "visible" to the user,
    // but the user cant "interact" with it yet may be cause it's overlapped with some other small dialog.
    // This ability to interact with the user is the one that differentiates onStart and onResume.
    // Think of it as a person behind a glass door. You can see the person but you can't interact
    // (talk/listen/shake hands) with him. OnResume is like the door opener after which you can begin the interaction.

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected() called. Sign in successful!");
        String baglimi = String.valueOf(mGoogleApiClient.isConnected());
        Log.i("baglanti", baglimi);
        updateAchievements();
    }

    public void updateAchievements() {
        StatisticsSqlDatabaseHelper newDatabaseHelper = new StatisticsSqlDatabaseHelper(getContext());
        newSQLDatabase = newDatabaseHelper.getWritableDatabase();
        ViewGroup container = ((Activity) getContext())
                .findViewById(android.R.id.content);
        v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.custom_toast, container);
        updateAchievementsCompletedSetsAndNoWrong();
        updateAchievementsForTrueAnswers();
//                }

    }

    public void goAchievements(View view) {
//        final View popupView = view.findViewById(R.id.gps_popup);
        view.findViewById(R.id.google_games_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked to Achievements");
                if (mGoogleApiClient == null) {
                    noConnectionGoProfile();
                } else if (mGoogleApiClient.isConnected()) {
                    Log.d(TAG, "There is a connection");
                    Intent gamesAchievementIntent = Games.Achievements.getAchievementsIntent(mGoogleApiClient);
                    startActivityForResult(gamesAchievementIntent, ACHIVEMENT_REQUEST_CODE);
                } else if (!mGoogleApiClient.isConnected()) {
                    noConnectionGoProfile();
                }
            }
        });

    }

    private void noConnectionGoProfile() {
        int redColor = ContextCompat.getColor(getContext(), R.color.classic_black);
        int backgroundColor = ContextCompat.getColor(getContext(), R.color.burak_background);
        CustomToast customToast = new CustomToast(getContext(), 2000, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        String noGamesConnection = getString(R.string.no_connection_go_profile);
        customToast.show(noGamesConnection, redColor, backgroundColor);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");

            return;
        }
        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;

//            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(getActivity(),
//                    mGoogleApiClient, connectionResult,
//                    RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        mGoogleApiClient.connect();
//        showSignIn();

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {

        if (responseCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
            mGoogleApiClient.disconnect();
        }
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + data);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
//            mGoogleApiClient.connect();

//            showSignOut();
            // Bring up an error dialog to alert the user that sign-in
            // failed. The R.string.signin_failure should reference an error
            // string in your strings.xml file that tells the user they
            // could not be signed in, such as "Unable to sign in."
            if (responseCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
//                showSignOut();
            }
//            else {
////                BaseGameUtils.showActivityResultError(getActivity(),
////                        requestCode, responseCode, R.string.sign_in_failed);
//            }

        }
    }
    //select count (*) from newTable where testId = 2 and accuracy = 'true';

    // burada genel database kullanilacak achievement lar icin metotlar olusturuluyor
    // ve unlock achievementlar yapiliyor
    private void updateAchievementsCompletedSetsAndNoWrong() {
        // dogru yanlis farketmez cozulen soru sayisi
        Log.d(TAG, "Clicked to updateAchievementsCompletedSetsAndNoWrong");
        Cursor setNumberTOEFLCursor;
        Cursor setNumberGRECursor;
        Cursor setTOEICCursor;
        Cursor setIELTSCursor;

        String tableName = TABLE_NAME;
        String selection = COLUMN_TEST_TYPE + " LIKE ?";
        String TOEFLWords = getString(R.string.action_bar_toefl);
        String TOEFLWordsLike = '%' + TOEFLWords;
        String TOEICWords = getString(R.string.action_bar_toeic);
        String TOEICWordsLike = '%' + TOEICWords;
        String GREWords = getString(R.string.action_bar_gre);
        String GREWordsLike = '%' + GREWords;
        String IELTSWords = getString(R.string.action_bar_ielts);
        String IELTSWordsLike = '%' + IELTSWords;

        String selectionArgsTOEFL[] = {TOEFLWordsLike};
        String selectionArgsTOEIC[] = {TOEICWordsLike};
        String selectionArgsGRE[] = {GREWordsLike};
        String selectionArgsIELTS[] = {IELTSWordsLike};

        setNumberTOEFLCursor = newSQLDatabase.query(tableName, null, selection, selectionArgsTOEFL
                , null, null, null);
        setNumberGRECursor = newSQLDatabase.query(tableName, null, selection, selectionArgsGRE
                , null, null, null);
        setTOEICCursor= newSQLDatabase.query(tableName, null, selection, selectionArgsTOEIC
                , null, null, null);
        setIELTSCursor = newSQLDatabase.query(tableName, null, selection, selectionArgsIELTS
                , null, null, null);

        int setNumberTOEFL = setNumberTOEFLCursor.getCount();
        int setNumberGRE = setNumberGRECursor.getCount();
        int setNumberTOEIC = setTOEICCursor.getCount();
        int setNumberIELTS = setIELTSCursor.getCount();

        //4.basari 100 soru coz


        if (setNumberTOEFL >= 10 * 10) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_iv_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLIVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLIVach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLIVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLIVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvIV = (setNumberTOEFL * 100) / (10 * 10);
            String ratioAdvString = "% " + String.valueOf(ratioAdvIV);
            achViewHolder.TOEFLIVProgressRatio.setText(ratioAdvString);
        }
        if (setNumberGRE >= 10 * 10) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_iv_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREIVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREIVach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREIVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREIVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvIV = (setNumberGRE * 100) / (10 * 10);
            String ratioAdvString = "% " + String.valueOf(ratioAdvIV);
            achViewHolder.GREIVProgressRatio.setText(ratioAdvString);
        }

        if (setNumberIELTS >= 10 * 10) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_iv_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSIVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSIVach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSIVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSIVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvIV = (setNumberIELTS * 100) / (10 * 10);
            String ratioAdvString = "% " + String.valueOf(ratioAdvIV);
            achViewHolder.IELTSIVProgressRatio.setText(ratioAdvString);
        }

        if (setNumberTOEIC >= 10 * 10) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_iv_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICIVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICIVach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICIVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICIVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvIV = (setNumberTOEIC * 100) / (10 * 10);
            String ratioAdvString = "% " + String.valueOf(ratioAdvIV);
            achViewHolder.TOEICIVProgressRatio.setText(ratioAdvString);
        }

        //5.basari 200 soru coz
        if (setNumberTOEFL >= 10 * 20) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_v_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLVach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvV = (setNumberTOEFL * 100) / (10 * 20);
            String ratioAdvVString = "% " + String.valueOf(ratioAdvV);
            achViewHolder.TOEFLVProgressRatio.setText(ratioAdvVString);
        }
        if (setNumberGRE >= 10 * 20) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_v_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREVach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvV = (setNumberGRE * 100) / (10 * 20);
            String ratioAdvVString = "% " + String.valueOf(ratioAdvV);
            achViewHolder.GREVProgressRatio.setText(ratioAdvVString);
        }
        if (setNumberTOEIC >= 10 * 20) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_v_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICVach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvV = (setNumberTOEIC * 100) / (10 * 20);
            String ratioAdvVString = "% " + String.valueOf(ratioAdvV);
            achViewHolder.TOEICVProgressRatio.setText(ratioAdvVString);
        }
        if (setNumberIELTS >= 10 * 20) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_v_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSVProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSVach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSVcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSVProgressBar.setVisibility(View.GONE);
            }
        } else {
            int ratioAdvV = (setNumberIELTS * 100) / (10 * 20);
            String ratioAdvVString = "% " + String.valueOf(ratioAdvV);
            achViewHolder.IELTSVProgressRatio.setText(ratioAdvVString);
        }


        String userStats = "userStats";
        SharedPreferences stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        String testQuestionNumberPreference = "testQuestionNumberPreference";
        int testQuestion = stats.getInt(testQuestionNumberPreference, 10);

        //1 test tamamla
        if (setNumberTOEFL != 0 && setNumberTOEFL >= testQuestion) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_i_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.TOEFLIProgressRatio.setText("% 0");
            }
        }

        if (setNumberTOEIC != 0 && setNumberTOEIC >= testQuestion) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_i_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.TOEICIProgressRatio.setText("% 0");
            }
        }
        if (setNumberIELTS != 0 && setNumberIELTS >= testQuestion) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_i_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSIach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.IELTSIProgressRatio.setText("% 0");
            }
        }
        if (setNumberGRE != 0 && setNumberGRE >= testQuestion) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_i_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREIach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.GREIProgressRatio.setText("% 0");
            }
        }

        String TOEICIIPreference = "TOEICIIPreference";
        String TOEFLIIPreference = "TOEFLIIPreference";
        String IELTSIIPreference = "IELTSIIPreference";
        String GREIPreference = "GREIPreference";


        stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        Boolean noWrongAnswerTOEFL = stats.getBoolean(TOEFLIIPreference, false);
        Boolean noWrongAnswerIELTS = stats.getBoolean(IELTSIIPreference, false);
        Boolean noWrongAnswerTOEIC = stats.getBoolean(TOEICIIPreference, false);
        Boolean noWrongAnswerGRE = stats.getBoolean(GREIPreference, false);


        // hatasiz test basarisi, testfragment da kaydedilen degerler cagiriliyor

        if (noWrongAnswerIELTS) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_ii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSIIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.IELTSIIProgressRatio.setText("% 0 ");
            }
        }
        if (noWrongAnswerGRE) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_ii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREIIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.GREIIProgressRatio.setText("% 0 ");
            }
        }

        if (noWrongAnswerTOEIC) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_ii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICIIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.TOEICIIProgressRatio.setText("% 0 ");
            }
        }

        if (noWrongAnswerTOEFL) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_ii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLIIProgressBar.setVisibility(View.GONE);
            } else {
                achViewHolder.TOEFLIIProgressRatio.setText("% 0 ");
            }
        }


        setNumberGRECursor.close();
        setNumberTOEFLCursor.close();
        setIELTSCursor.close();
        setTOEICCursor.close();
    }

    private void updateAchievementsForTrueAnswers() {
        Log.d(TAG, "Clicked to updateAchievementsForTrueAnswers");

        Cursor setNumberTOEFLCursor;
        Cursor setNumberGRECursor;
        Cursor setTOEICCursor;
        Cursor setIELTSCursor;

        String tableName = TABLE_NAME;
        String selection = COLUMN_ACCURACY + " = ? AND " + COLUMN_TEST_TYPE + " LIKE ?";
        String IELTS = getString(R.string.action_bar_ielts);
        String IELTSLike = '%' + IELTS;
        String TOEFL = getString(R.string.action_bar_toefl);
        String TOEFLLike = '%' + TOEFL;
        String greWords = getString(R.string.action_bar_gre);
        String greWordsLike = '%' + greWords;
        String TOEICWords = getString(R.string.action_bar_toeic);
        String TOEICWordsLike = '%' + TOEICWords;

        String selectionArgsTOEIC[] = {"true", TOEICWordsLike};
        String selectionArgsIELTS[] = {"true", IELTSLike};
        String selectionArgsTOEFL[] = {"true", TOEFLLike};
        String selectionArgsGRE[] = {"true", greWordsLike};


        setNumberTOEFLCursor = newSQLDatabase.query(tableName, null, selection, selectionArgsTOEFL
                , null, null, null);
        setNumberGRECursor = newSQLDatabase.query(tableName, null, selection, selectionArgsGRE
                , null, null, null);
        setTOEICCursor = newSQLDatabase.query(tableName, null, selection, selectionArgsTOEIC
                , null, null, null);
        setIELTSCursor = newSQLDatabase.query(tableName, null, selection, selectionArgsIELTS
                , null, null, null);

        int setNumberTOEFL = setNumberTOEFLCursor.getCount();
        int setNumberGRE = setNumberGRECursor.getCount();
        int setNumberTOEIC = setTOEICCursor.getCount();
        int setNumberIELTS = setIELTSCursor.getCount();


        int firstCorrectAnswerAim = 50;
        if (setNumberGRE >= (firstCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_iii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREIIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREIIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREIIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREIIIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberGRE < (firstCorrectAnswerAim)) {
            int ratioImagesIII = (setNumberGRE * 100) / (firstCorrectAnswerAim);
            String ratioImagesIIIString = "% " + String.valueOf(ratioImagesIII);
            achViewHolder.GREIIIProgressRatio.setText(ratioImagesIIIString);
        }

        if (setNumberIELTS >= (firstCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_iii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSIIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSIIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSIIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSIIIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberIELTS < (firstCorrectAnswerAim)) {
            int ratioImagesIII = (setNumberIELTS * 100) / (firstCorrectAnswerAim);
            String ratioImagesIIIString = "% " + String.valueOf(ratioImagesIII);
            achViewHolder.IELTSIIIProgressRatio.setText(ratioImagesIIIString);
        }
        if (setNumberTOEIC >= (firstCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_iii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICIIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICIIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICIIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICIIIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberTOEIC < (firstCorrectAnswerAim)) {
            int ratioCountriesIII = (setNumberTOEIC * 100) / (firstCorrectAnswerAim);
            String ratioCountriesIIIString = "% " + String.valueOf(ratioCountriesIII);
            achViewHolder.TOEICIIIProgressRatio.setText(ratioCountriesIIIString);
        }

        if (setNumberTOEFL >= (firstCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_iii_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLIIIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLIIIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLIIIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLIIIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberTOEFL < (firstCorrectAnswerAim)) {
            int ratioAdvV = (setNumberTOEFL * 100) / (firstCorrectAnswerAim);
            String ratioAdvVString = "% " + String.valueOf(ratioAdvV);
            achViewHolder.TOEFLIIIProgressRatio.setText(ratioAdvVString);
        }


        int secondCorrectAnswerAim = 100;
        if (setNumberTOEIC >= (secondCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEIC_vi_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEICVIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEICVIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEICVIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEICVIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberTOEIC < (secondCorrectAnswerAim)) {
            int ratioCountriesVI = (setNumberTOEIC * 100) / (secondCorrectAnswerAim);
            String ratioCountriesVIString = "% " + String.valueOf(ratioCountriesVI);
            achViewHolder.TOEICVIProgressRatio.setText(ratioCountriesVIString);
        }


        if (setNumberIELTS >= (secondCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_IELTS_vi_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.IELTSVIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.IELTSVIach.setImageResource(R.drawable.award_blue);
                achViewHolder.IELTSVIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.IELTSVIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberIELTS < (secondCorrectAnswerAim)) {
            int ratioImagesVI = (setNumberIELTS * 100) / (secondCorrectAnswerAim);
            String ratioImagesVIString = "% " + String.valueOf(ratioImagesVI);
            achViewHolder.IELTSVIProgressRatio.setText(ratioImagesVIString);
        }



        if (setNumberTOEFL >= (secondCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_TOEFL_vi_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.TOEFLVIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.TOEFLVIach.setImageResource(R.drawable.award_blue);
                achViewHolder.TOEFLVIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.TOEFLVIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberTOEFL < (secondCorrectAnswerAim)) {
            int ratioAdvVI = (setNumberTOEFL * 100) / (secondCorrectAnswerAim);
            String ratioAdvVIString = "% " + String.valueOf(ratioAdvVI);
            achViewHolder.TOEFLVIProgressRatio.setText(ratioAdvVIString);
        }

        if (setNumberGRE >= (secondCorrectAnswerAim)) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Games.Achievements.unlock(mGoogleApiClient, getString(R.string.achievement_GRE_vi_code));
                Games.setViewForPopups(mGoogleApiClient, v);
            }
            if (achViewHolder.GREVIProgressBar.getVisibility() == View.VISIBLE) {
                achViewHolder.GREVIach.setImageResource(R.drawable.award_blue);
                achViewHolder.GREVIcheckMark.setVisibility(View.VISIBLE);
                achViewHolder.GREVIProgressBar.setVisibility(View.GONE);
            }
        } else if (setNumberGRE < (secondCorrectAnswerAim)) {
            int ratioAdvVI = (setNumberGRE * 100) / (secondCorrectAnswerAim);
            String ratioAdvVIString = "% " + String.valueOf(ratioAdvVI);
            achViewHolder.GREVIProgressRatio.setText(ratioAdvVIString);
        }

        setIELTSCursor.close();
        setNumberGRECursor.close();
        setNumberTOEFLCursor.close();
        setTOEICCursor.close();
    }
}
