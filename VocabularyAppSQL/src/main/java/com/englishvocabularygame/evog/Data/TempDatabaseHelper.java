package com.englishvocabularygame.evog.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created on 9.12.2016.
 */

public class TempDatabaseHelper extends SQLiteOpenHelper {

    public TempDatabaseHelper(Context context) {
        super(context, DataContract.TEMP_DATABASE_NAME, null, DataContract.DATABASE_VERSION_NEW);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataContract.StatisticsTable.CREATE_TEMP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DataContract.StatisticsTable.DELETE_TEMP_TABLE);
        onCreate(sqLiteDatabase);
    }


}
