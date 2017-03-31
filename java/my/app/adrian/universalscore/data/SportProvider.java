package my.app.adrian.universalscore.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.IllegalFormatException;

import my.app.adrian.universalscore.data.SportContract.SportEntry;

import static my.app.adrian.universalscore.R.style.match;
import static my.app.adrian.universalscore.data.SportContract.SportEntry.CONTENT_LIST_TYPE;

public class SportProvider extends ContentProvider{
    /** Tag for the log messages */
    public static final String LOG_TAG = SportProvider.class.getSimpleName();
    /** Database helper object */
    private SportDbHelper mDbHelper;
    /** URI matcher code for the content URI for the pets table */
    private static final int SPORTS = 100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int SPORT_ID = 101;
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportEntry.TABLE_NAME, SPORTS);
        sUriMatcher.addURI(SportContract.CONTENT_AUTHORITY, SportEntry.TABLE_NAME + "/#", SPORT_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new SportDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // This cursor will hold the result of the query
        Cursor cursor;
        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match){
            case SPORTS:
                // For the SPORTS code, query the sports table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the sports table.
                cursor = db.query(SportEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SPORT_ID:
                // For the SPORT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = SportEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(SportEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //Set notification URI on the Cursor,
        //so we know what content URI the Cursor was created for
        //If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case SPORTS:
                return insertSport(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertSport(Uri uri, ContentValues values){
        // Check that the values are not null
        String aname = values.getAsString(SportEntry.COLUMN_A_NAME);
        if (aname == null) {
            throw new IllegalArgumentException("Cannot get Team A name");
        }
        // Check that the values are not null
        String bname = values.getAsString(SportEntry.COLUMN_B_NAME);
        if (bname == null) {
            throw new IllegalArgumentException("Cannot get Team B name");
        }
        // If the score is provided, check that it's greater than or equal to 0
        Integer ascore = values.getAsInteger(SportEntry.COLUMN_A_SCORE);
        if (ascore != null && ascore < 0) {
            throw new IllegalArgumentException("Cannot get Team A score");
        }
        // If the score is provided, check that it's greater than or equal to 0
        Integer bscore = values.getAsInteger(SportEntry.COLUMN_B_SCORE);
        if (bscore != null && bscore < 0) {
            throw new IllegalArgumentException("Cannot get Team B score");
        }
        // Get writeable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Insert the new pet with the given values
        long id = db.insert(SportEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //Notify all listeners that the data has changed for the sport content URI
        getContext().getContentResolver().notifyChange(uri, null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SPORTS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case SPORT_ID:
                // For the SPORT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = SportEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    /**
     * Update sports in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link SportEntry#COLUMN_A_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(SportEntry.COLUMN_A_NAME)) {
            String aname = values.getAsString(SportEntry.COLUMN_A_NAME);
            if (aname == null) {
                throw new IllegalArgumentException("Cannot get Team A name");
            }
        }
        // If the {@link SportEntry#COLUMN_B_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(SportEntry.COLUMN_B_NAME)) {
            String bname = values.getAsString(SportEntry.COLUMN_B_NAME);
            if (bname == null) {
                throw new IllegalArgumentException("Cannot get Team B name");
            }
        }
        // If the {@link SportEntry#COLUMN_A_SCORE} key is present,
        // check that the score value is valid.
        if (values.containsKey(SportEntry.COLUMN_A_SCORE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer ascore = values.getAsInteger(SportEntry.COLUMN_A_SCORE);
            if (ascore != null && ascore < 0) {
                throw new IllegalArgumentException("Cannot get Team A score");
            }
        }
        // If the {@link SportEntry#COLUMN_B_SCORE} key is present,
        // check that the score value is valid.
        if (values.containsKey(SportEntry.COLUMN_B_SCORE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer bscore = values.getAsInteger(SportEntry.COLUMN_B_SCORE);
            if (bscore != null && bscore < 0) {
                throw new IllegalArgumentException("Cannot get Team A score");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {return 0;}
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(SportEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;

    }
    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SPORTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(SportEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SPORT_ID:
                // Delete a single row given by the ID in the URI
                selection = SportEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(SportEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }
    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SPORTS:
                return SportEntry.CONTENT_LIST_TYPE;
            case SPORT_ID:
                return SportEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
