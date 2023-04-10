package com.englishvocabularygame.evog.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Uslular on 10.10.2016.
 * A contract class is a container for constants that define names for URIs, tables, and columns.
 * The contract class allows you to use the same constants across all the other classes in the same package.
 * This lets you change a column name in one place and have it propagate throughout your code.
 */

public class DataContract {

    public static final int DATABASE_VERSION_NEW = 20180106;
    public static final int DATABASE_VERSION = 1;
//    private static Context mContext;
    public static final String DATABASE_NAME = "newDatabase.sqlite";
    public static final String TEMP_DATABASE_NAME = "tempDatabase.sqlite";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataContract() {
    }

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.macofu.funedu.funenglishvocabularygame";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/Eng/ is a valid path for

//    public static final String UserStatictis = "userStatistics";

    /* Inner class that defines the table contents of the Eng to Tr  */
    public static final class StatisticsTable implements BaseColumns {
        public static final String TABLE_NAME = "newTable";
        public static final String TEMP_TABLE_NAME = "tempTable";
        public static final String COLUMN_QUESTION_ID = BaseColumns._ID;
        public static final String COLUMN_TEST_ID = "testId";
        public static final String COLUMN_TEST_TYPE = "testType";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_CORRECT_ANSWER = "correctAnswer";
        public static final String COLUMN_USER_ANSWER = "userAnswer";
        public static final String COLUMN_TEST_DATE = "testDate";
        public static final String COLUMN_ANSWER_TIME = "averageAnswerTime";
        public static final String COLUMN_ACCURACY = "accuracy";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEST_ID + TEXT_TYPE + COMMA_SEP
                + COLUMN_TEST_TYPE + TEXT_TYPE + COMMA_SEP
                + COLUMN_TEST_DATE + TEXT_TYPE + COMMA_SEP
                + COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP
                + COLUMN_USER_ANSWER + TEXT_TYPE + COMMA_SEP
                + COLUMN_CORRECT_ANSWER + TEXT_TYPE + COMMA_SEP
                + COLUMN_ANSWER_TIME + TEXT_TYPE + COMMA_SEP
                + COLUMN_ACCURACY + TEXT_TYPE + " )";


        public static final String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TEMP_TABLE_NAME + " ("
                + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEST_ID + TEXT_TYPE + COMMA_SEP
                + COLUMN_TEST_TYPE + TEXT_TYPE + COMMA_SEP
                + COLUMN_TEST_DATE + TEXT_TYPE + COMMA_SEP
                + COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP
                + COLUMN_USER_ANSWER + TEXT_TYPE + COMMA_SEP
                + COLUMN_CORRECT_ANSWER + TEXT_TYPE + COMMA_SEP
                + COLUMN_ANSWER_TIME + TEXT_TYPE + COMMA_SEP
                + COLUMN_ACCURACY + TEXT_TYPE + " )";


        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String DELETE_TEMP_TABLE = "DROP TABLE IF EXISTS'" + TEMP_TABLE_NAME + "'";
        public static final String DELETE_DATA_OF_TEMP_TABLE = "DELETE FROM " + TEMP_TABLE_NAME ;

//        static String dbPath = mContext.getDatabasePath(TEMP_DATABASE_NAME).getPath();

        //INSERT INTO table1 SELECT * FROM attacheddb.table1;
        public static final String MERGE_TABLE = "INSERT INTO " + TABLE_NAME
                + " ( " + COLUMN_TEST_ID  + COMMA_SEP
                + COLUMN_TEST_TYPE  + COMMA_SEP
                + COLUMN_TEST_DATE  + COMMA_SEP
                + COLUMN_QUESTION  + COMMA_SEP
                + COLUMN_USER_ANSWER + COMMA_SEP
                + COLUMN_CORRECT_ANSWER + COMMA_SEP
                + COLUMN_ANSWER_TIME + COMMA_SEP
                + COLUMN_ACCURACY + ")"
                + " SELECT " + COLUMN_TEST_ID  + COMMA_SEP
                + COLUMN_TEST_TYPE  + COMMA_SEP
                + COLUMN_TEST_DATE  + COMMA_SEP
                + COLUMN_QUESTION  + COMMA_SEP
                + COLUMN_USER_ANSWER + COMMA_SEP
                + COLUMN_CORRECT_ANSWER + COMMA_SEP
                + COLUMN_ANSWER_TIME + COMMA_SEP
                + COLUMN_ACCURACY + " FROM " + TEMP_TABLE_NAME;
//        public static final String ATTACH_TABLE = "ATTACH DATABASE '" + dbPath + "' AS " + ALIAS_NAME;
//        public static final String DETACH_TABLE = "DETACH DATABASE " + ALIAS_NAME;
    }


}
