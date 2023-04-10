package com.englishvocabularygame.evog.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.englishvocabularygame.evog.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import java.util.Locale;

import static android.app.Activity.RESULT_OK;

//import com.google.example.games.basegameutils.BaseGameUtils;

/**
 * Created on 7.10.2016.
 */

public class ProfileFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , View.OnClickListener {

    private boolean mResolvingConnectionFailure = false;
    SharedPreferences loginStats;
    final String userStats = "userStats";
    final String correctAnswerPreference = "correctAnswer";
    final String falseAnswerPreference = "falseAnswer";
    final String remainingTimerPreference = "remainingTimerPreference";
    final String scorePreference = "scoreCount";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked = false;
    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;
    private static final String TAG = "ProfileFragment";
    private AdView mAdView;
    public static long mUserLeaderboardScore;
    private TextView highestScoreView;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        Log.d(TAG, "onCreate()");
        // buranin basina kesinlikle check internet status koymak lazim eger baglanti var ise bunu yapmali
        if (getContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) getContext().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                highestScoreView = rootView.findViewById(R.id.highest_score);

                if (networkInfo != null && networkInfo.isConnected()) {
                    mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .enableAutoManage(getActivity(), this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                            .build();
                }
            }
        }
        setUserStats(rootView);
        loginStats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        mAdView = rootView.findViewById(R.id.adView_topBanner_transitionPage);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        goLeaderboard(rootView);
        SignInButton signInButton = rootView.findViewById(R.id.sign_in_button2);
        signInButton.setOnClickListener(this);
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
        if (mGoogleApiClient == null) {
            showSignIn();
        } else if (!mGoogleApiClient.isConnected()) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        int score = loginStats.getInt(scorePreference, 0);
        if (mUserLeaderboardScore != 0 && mUserLeaderboardScore > score) {
            highestScoreView.setText(String.valueOf(mUserLeaderboardScore));
            SharedPreferences stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = stats.edit();
            edit.putInt(scorePreference, (int) mUserLeaderboardScore);
            edit.apply();

        } else {
            highestScoreView.setText(String.valueOf(score));
        }
        if (mAdView != null) {
            mAdView.resume();
        }
//        if (mGoogleApiClient !=null && !mGoogleApiClient.isConnected()) {
//            Handler handler = new Handler();
//            Runnable delay = new Runnable() {
//                @Override
//                public void run() {
//                    showSignIn();
//                }
//            };
//            handler.postDelayed(delay, 1000);
//            Log.i(TAG, "OnResume, Connected back!");
//        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        if (mGoogleApiClient == null) {
            showSignIn();
        } else if (!mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
            showSignIn();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected() called. Sign in successful!");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.d(TAG, "mUserLeaderboardScore " + mUserLeaderboardScore
                    + "score " + loginStats.getInt(scorePreference, 0));
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(mGoogleApiClient
                    , getString(R.string.leader_board_id)
                    , LeaderboardVariant.TIME_SPAN_ALL_TIME
                    , LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback
                    (new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
                        @Override
                        public void onResult(@NonNull Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
                            Log.d(TAG, "loadPlayerScoreResult.getScore() " + loadPlayerScoreResult.getScore());
                            int score = loginStats.getInt(scorePreference, 0);
                            if (loadPlayerScoreResult.getScore() != null) {
                                mUserLeaderboardScore = loadPlayerScoreResult.getScore().getRawScore();
                                Log.d("MultiP", "score " + mUserLeaderboardScore);
                                Log.d(TAG, "There is a connection");
                                if (mUserLeaderboardScore < score) {
                                    Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leader_board_id), score);
                                }
                            }
                            else{
                                Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leader_board_id), score);
                            }
                        }
                    });
        }
        showSignOut();
    }

    private void setUserStats(View view) {

        loginStats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
        int currentCorrectScore = loginStats.getInt(correctAnswerPreference, 0);
        int currentFalseScore = loginStats.getInt(falseAnswerPreference, 0);
        long totalTime = loginStats.getLong(remainingTimerPreference, 0);
        TextView averageTimeView = view.findViewById(R.id.average_time);
        if ((currentCorrectScore + currentFalseScore) != 0) {
            double averageTime = (totalTime) / ((double) (currentCorrectScore + currentFalseScore));
            String averageTimeWithSecond = String.format(Locale.getDefault(), "%.2f", averageTime)
                    + getString(R.string.second);
            averageTimeView.setText(averageTimeWithSecond);
        }
        if ((currentCorrectScore + currentFalseScore) == 0) {
            double averageTime = 0;
            String averageTimeWithSecond2 = String.valueOf(averageTime) + getString(R.string.second);
            averageTimeView.setText(averageTimeWithSecond2);
        }
        TextView highestScoreView = view.findViewById(R.id.highest_score);
        int score = loginStats.getInt(scorePreference, 0);
        int level;
        if (mUserLeaderboardScore != 0 && mUserLeaderboardScore > score) {
            level = ((int) mUserLeaderboardScore / 1000) + 1;
            highestScoreView.setText(String.valueOf(mUserLeaderboardScore));
            SharedPreferences stats = getContext().getSharedPreferences(userStats, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = stats.edit();
            edit.putInt(scorePreference, (int) mUserLeaderboardScore);
            edit.apply();

        } else {
            highestScoreView.setText(String.valueOf(score));
            level = (score / 1000) + 1;
        }

        TextView levelView = view.findViewById(R.id.level);
        if (level <= 5) {
            String seniority = getString(R.string.rookie);
            String seniorityWithLevel = String.valueOf(level) + " " + seniority;
            levelView.setText(seniorityWithLevel);
        } else if (5 < level && level <= 10) {
            String seniority = getString(R.string.skilled);
            String seniorityWithLevel = String.valueOf(level) + " " + seniority;
            levelView.setText(seniorityWithLevel);
        } else if (10 < level && level <= 15) {
            String seniority = getString(R.string.experienced);
            String seniorityWithLevel = String.valueOf(level) + " " + seniority;
            levelView.setText(seniorityWithLevel);
        } else if (15 < level && level <= 20) {
            String seniority = getString(R.string.master);
            String seniorityWithLevel = String.valueOf(level) + " " + seniority;
            levelView.setText(seniorityWithLevel);
        }
    }

    private void showGoogleImage() {
        Log.d(TAG, "showGoogleImage()");
        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String name = player.getDisplayName();
        Log.d(TAG, name + "is received");
        @SuppressWarnings("ConstantConditions")
        TextView gamesSignedId = getView().findViewById(R.id.google_signout_status);
        String welcomemessage = getString(R.string.signed_in_google_play) + "\n" + name;
        gamesSignedId.setText(welcomemessage);
        ImageManager imageManager = ImageManager.create(getContext());
        ImageView imageView = getView().findViewById(R.id.google_games_image);
        Uri imageuri = player.getIconImageUri();
        Log.d(TAG, "imageUri is received:" + String.valueOf(imageuri));
        imageManager.loadImage(imageView, imageuri, R.drawable.user_picture);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = 250;
        params.height = 250;
        imageView.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button2:
                // start the sign-in flow
                // showSignOut();
                Log.d(TAG, "Sign-in button clicked");
                mSignInClicked = true;
                ConnectivityManager cm = (ConnectivityManager) getContext().
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .enableAutoManage(getActivity(), this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                                .build();
                        mGoogleApiClient.connect();
                    }
                } else {
                    String noNetwork = getString(R.string.sign_in_failed);
                    Toast toast = Toast.makeText(getContext(), noNetwork, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    // Shows the "sign in" bar (explanation and button).
    @SuppressWarnings("ConstantConditions")
    private void showSignIn() {
        Log.d(TAG, "Showing sign in bar");
        getView().findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
        getView().findViewById(R.id.leaderboard_bar).setVisibility(View.GONE);
    }

    // Shows the "sign out" bar (explanation and button).
    @SuppressWarnings("ConstantConditions")
    private void showSignOut() {
        Log.d(TAG, "Showing sign out bar");
        getView().findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        getView().findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.leaderboard_bar).setVisibility(View.VISIBLE);
        if (mGoogleApiClient.isConnected() && Games.Players.getCurrentPlayer(mGoogleApiClient) != null) {
            showGoogleImage();
        }
    }

    public void goLeaderboard(View view) {
        view.findViewById(R.id.fourth_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
                    showSignIn();
                } else if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Log.d(TAG, "There is a connection");
                    Intent gamesIntent = Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient
                            , getString(R.string.leader_board_id)
                            , LeaderboardVariant.TIME_SPAN_ALL_TIME
                            , LeaderboardVariant.COLLECTION_PUBLIC);
                    startActivityForResult(gamesIntent, 1111);
                } else if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                    showSignIn();
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        showSignIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            String noNetwork = getString(R.string.signin_other_error);
            Toast toast = Toast.makeText(getContext(), noNetwork, Toast.LENGTH_LONG);
            toast.show();
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
            if (responseCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
//                showSignOut();
            } else {
//                BaseGameUtils.showActivityResultError(getActivity(),
//                        requestCode, responseCode, R.string.sign_in_failed);
            }

        }
    }

}
