package com.englishvocabularygame.evog.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// Bu class import edilerek her seferinde orada belirlenen isimler icin uzun ifade gerekmeyecek


public class SqlDatabaseHelper extends SQLiteOpenHelper {

    /**
     * bu databasi olusturabilmek, acabilmek ve yontebilmek icin
     * yardimci object olusturmak icin constructor kullanilacak
     * bu constructor i da cagirmak icin sadece context girdisi ile
     * super i cagirilip asagida belirtilen 4 parametre istege gore belirlenecek.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     * <p>
     * context          to use to open or create the database
     * DATABASE_NAME    of the database file, or null for an in-memory database
     * null             to use for creating cursor objects, or null for the default
     * DATABASE_VERSION number of the database (starting at 1); if the database is older,
     */
    private final Context mContext;
    // Database guncellenmedigi surece bu atanan rakam kullanilabilir.
    // switch case yapisi ile hangi database isteniyorsa o isim super icine koyulacak

    private static final int DATABASE_VERSION = 8;
    private static final String SP_KEY_DB_VER = "db_ver";
    private static String DATABASE_PATH = "";
    private SQLiteDatabase myDatabase;


    public SqlDatabaseHelper(Context context, String database_name) {
        super(context, database_name, null, DATABASE_VERSION);
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to write the create table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to write the update table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
        // We should not update it as requirements of application.
        // if required we need to update
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */

    private boolean checkDataBase() {
        File dataBaseFile = new File(DATABASE_PATH + DataAdapter.databaseName);
        return dataBaseFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     */

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DataAdapter.databaseName);
        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DataAdapter.databaseName;
        //Open the empty db as the output stream
        OutputStream outputStream = new FileOutputStream(outFileName);
        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        //Close the streams
        outputStream.flush();
        outputStream.close();
        myInput.close();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
        editor.apply();
    }


    public void createDataBase() throws IOException {
        //check if the database exists
        boolean dataBaseExist = checkDataBase();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        int dbVersion = prefs.getInt(SP_KEY_DB_VER, 2);
        if (dataBaseExist && dbVersion == DATABASE_VERSION) {
            Log.d("SQLHELPER", "dataBaseExist && dbVersion == DATABASE_VERSION");
            //do nothing
        } else {
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    /**
     * This method opens the data base connection.
     * First it create the path up till data base of the device.
     * Then create connection with data base.
     */

    public void openDataBase() {
        String myPath = DATABASE_PATH + DataAdapter.databaseName;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * This Method is used to close the data base connection.
     */
    @Override
    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
        }
        super.close();
    }

    /**
     * Close DataBase
     */
    public void closeDataBase() {

        myDatabase.close();
    }

}
