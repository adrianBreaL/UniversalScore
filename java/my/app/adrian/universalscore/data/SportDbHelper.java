package my.app.adrian.universalscore.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import my.app.adrian.universalscore.data.SportContract.SportEntry;

public class SportDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = SportDbHelper.class.getSimpleName();
    /**
     * If you change the database schema, you must increment the database version
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    /**
     * FeedReader Constructor
     * @param context
     */
    public SportDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * Creates db via execSQL
     * SQL_CREATE_ENTRIES = sql command to create table
     * @param db
     */
    public void onCreate(SQLiteDatabase db){
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_SPORT_TABLE =  "CREATE TABLE " + SportEntry.TABLE_NAME + " ("
                + SportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SportEntry.COLUMN_A_NAME + " TEXT NOT NULL, "
                + SportEntry.COLUMN_B_NAME + " TEXT NOT NULL, "
                + SportEntry.COLUMN_A_SCORE + " INTEGER NOT NULL, "
                + SportEntry.COLUMN_B_SCORE + " INTEGER NOT NULL, ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_SPORT_TABLE);
    }
    /**
     * Upgrades table by delete it and create it again
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //This database is only a cache for online data, so its upgrade policy is
        //to simply to discard the data and start over.
    }
    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
