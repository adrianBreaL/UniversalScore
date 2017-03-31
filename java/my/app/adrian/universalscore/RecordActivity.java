package my.app.adrian.universalscore;


import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import my.app.adrian.universalscore.data.SportContract;
import my.app.adrian.universalscore.data.SportContract.SportEntry;

public class RecordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    SportCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        // Find the ListView which will be populated with the record data
        ListView gameListView = (ListView) findViewById(R.id.record_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        gameListView.setEmptyView(emptyView);

        //Setup an Adapter to create a list item for each row of pet data in the Cursor.
        //There is no pet data yet (until the loader finishes) so pass in null for the cursor.
        mCursorAdapter = new SportCursorAdapter(this, null);
        gameListView.setAdapter(mCursorAdapter);

        //Setup item click listener
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Create new intent to go to {@link EditorActivity}
                //TODO: Another activity just showing record not editing
//                Intent intent = new Intent(RecordActivity.this, EditorActivity.class);
                //Form the content URI that represents the specific pet that was clicked on,
                //by appending the "id" (passed as input to this method) onto the
                //{@link PetEntry#CONTENT_URI}
                //Example: "content://com.example.android.pets/pets/2"
//                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                //Set the URI on the data field of the intent
//                intent.setData(currentPetUri);
//                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.record_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_share_list:
                // TODO: Action for the App Bar
                return true;
            case R.id.action_delete:
                // TODO: Action for the App Bar
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(RecordActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SportEntry._ID,
                SportEntry.COLUMN_A_NAME,
                SportEntry.COLUMN_B_NAME,
                SportEntry.COLUMN_A_SCORE,
                SportEntry.COLUMN_B_SCORE
        };
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,           //Parent activity context
                null,                            //Provider content URI to query
                projection,                     //Columns to include in the resulting Cursor
                null,                           //No selection clause
                null,                           //No selection clause
                null );                         //Default sort order
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Update (@link PetCursorAdapter) with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }
    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
//        if (mCurrentPetUri != null) {
//            // Call the ContentResolver to delete the pet at the given content URI.
//            // Pass in null for the selection and selection args because the mCurrentPetUri
//            // content URI already identifies the pet that we want.
//            int rowsDeleted = getContentResolver().delete(mCurrentPetUri, null, null);
//            // Show a toast message depending on whether or not the delete was successful.
//            if (rowsDeleted == 0) {
//                // If no rows were deleted, then there was an error with the delete.
//                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                // Otherwise, the delete was successful and we can display a toast.
//                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }

    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
//                deletePet();
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
