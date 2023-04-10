package com.englishvocabularygame.evog.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.englishvocabularygame.evog.Fragments.HomeFragment;

import java.io.IOException;
import java.util.Random;

//helper da olusturulan database i create, open ve close yapacak metotlar ve query ler tanimlanir.
// eger database de bir degisiklik oldu ise programi tekrar kaldirip yuklemek gerekiyor
public class DataAdapter {

    private static final String TAG = "DataAdapter";
    private SQLiteDatabase mDb;
    private SqlDatabaseHelper myDatabaseHelper;
    private static final int DATABASE_GRE_NUMBER = 30;
    private static final int DATABASE_TOEFL_NUMBER = 15;
    private static final int DATABASE_TOEIC_NUMBER = 10;
    private static final int DATABASE_IELTS_NUMBER = 150;
    static String databaseName;
    private static final String DATABASE_GRE_STRING = "gre.sqlite";
    private static final String DATABASE_TOEFL_STRING= "toefl.sqlite";
    private static final String DATABASE_TOEIC_STRING = "toeic.sqlite";
    private static final String DATABASE_IELTS_STRING = "ielts.sqlite";
    private String args;
    private Cursor mCursorQuestionandAnswer;
    private String[] selectionArgs;


    public DataAdapter(Context context) {
        int dbNumber = HomeFragment.databaseNumber;
        switch (dbNumber) {
            case (DATABASE_GRE_NUMBER):
                databaseName = DATABASE_GRE_STRING;
                break;
            case (DATABASE_TOEFL_NUMBER):
                databaseName = DATABASE_TOEFL_STRING;
                break;
            case (DATABASE_TOEIC_NUMBER):
                databaseName = DATABASE_TOEIC_STRING;
                break;
            case (DATABASE_IELTS_NUMBER):
                databaseName = DATABASE_IELTS_STRING;
                break;
            default:
                databaseName = DATABASE_TOEFL_STRING;
        }
        myDatabaseHelper = new SqlDatabaseHelper(context, databaseName);
    }

    public void useCreatedDatabase() throws SQLException {
        try {
            myDatabaseHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
    }

    public void useOpenedDatabase() throws SQLException {
        try {
            myDatabaseHelper.openDataBase();
            myDatabaseHelper.close();
            mDb = myDatabaseHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void close() {
        myDatabaseHelper.closeDataBase();
    }

    public Cursor getWords() {
        String selection = "_id = ?";
        // eger multiplayer secilmis ise soru numarasi olarak multiPlayerSelected atanir
        try {
            Cursor cursor = mDb.rawQuery("SELECT _id FROM database", null);
            int cnt = cursor.getCount();
            cursor.close();
            Random r = new Random();
            int selectRow = (r.nextInt(cnt - 1) + 1);
            args = Integer.toString(selectRow);
            selectionArgs = new String[]{args};
            mCursorQuestionandAnswer = mDb.query("database",
                    null, selection, selectionArgs, null, null, null);
            if (mCursorQuestionandAnswer != null) {
                mCursorQuestionandAnswer.moveToNext();
            }
            return mCursorQuestionandAnswer;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }

    }

    public int getRandomRow() {
            Log.d("MultiP","getRandomRow called");
        try {
            Cursor cursor = mDb.rawQuery("SELECT _id FROM database", null);
            int cnt = cursor.getCount();
            Log.d("MultiP","count"+cnt);
            cursor.close();
            Random r = new Random();
            //son 10 sorun numarasina denk gelme durumuna karsin
            return (r.nextInt(cnt - 11) + 1);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }

    }

    public Cursor getWordsForMultiple(int selectRow) {
        String selection = "_id = ?";
        // eger multiplayer secilmis ise soru numarasi olarak multiPlayerSelected atanir
        try {
            args = Integer.toString(selectRow);
            selectionArgs = new String[]{args};
            mCursorQuestionandAnswer = mDb.query("database",
                    null, selection, selectionArgs, null, null, null);
            if (mCursorQuestionandAnswer != null) {
                mCursorQuestionandAnswer.moveToNext();
            }
            return mCursorQuestionandAnswer;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }

    }


//    public Cursor getWordsSequential(int questionNumber) {
//        try {
//            String selection = "_id = ?";
//            String args = Integer.toString(questionNumber);
//            String selectionArgs[] = {args};
//            Cursor mCursorQuestionandAnswer = mDb.query("database",
//                    null, selection, selectionArgs, null, null, null);
//            mCursorQuestionandAnswer.moveToFirst();
//            return mCursorQuestionandAnswer;
//        } catch (SQLException mSQLException) {
//            Log.e(TAG, "getTestData >>" + mSQLException.toString());
//            throw mSQLException;
//        }
//
//    }
//
//    public int getMaxQuestionNumber() {
//        Cursor cursor = mDb.rawQuery("SELECT _id FROM database", null);
//        int cnt = cursor.getCount();
//        cursor.close();
//        Random r = new Random();
//        int selectRow = (r.nextInt(cnt - 1) + 1);
//        return selectRow;
//    }

}
