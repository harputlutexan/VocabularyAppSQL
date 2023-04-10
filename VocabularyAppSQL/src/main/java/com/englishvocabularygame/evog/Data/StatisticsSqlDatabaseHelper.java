package com.englishvocabularygame.evog.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created on 9.12.2016.
 */

public class StatisticsSqlDatabaseHelper extends SQLiteOpenHelper {

    public StatisticsSqlDatabaseHelper (Context context) {
        super(context, DataContract.DATABASE_NAME, null, DataContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataContract.StatisticsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DataContract.StatisticsTable.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }


}
