package my.app.adrian.universalscore;

import android.content.ContentUris;
import android.content.ContentValues;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import my.app.adrian.universalscore.data.SportContract;
import my.app.adrian.universalscore.data.SportContract.SportEntry;
import my.app.adrian.universalscore.data.SportProvider;

import static my.app.adrian.universalscore.R.id.txt_out;
import static my.app.adrian.universalscore.data.SportContract.BASE_CONTENT_URI;
import static my.app.adrian.universalscore.data.SportContract.SportEntry.TABLE_NAME;


/**
 * SportActivity, depending on the sport selected
 * inflates the proper layout and keeps the score
 */
public class SportActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    //mCurrentSport: gets sport id in order to inflate proper layout
    int mCurrentSport;
    boolean mChangedEditA=false,mChangedEditB=false ;
    private int teamA, teamB, iSet, numA, numB, A_set, B_set, gameA, gameB;
    private int balls, strike, out, i;
    //3 sets for Badminton
    private int[] setBA = {R.id.setA1, R.id.setA2, R.id.setA3};
    private int[] setBB = {R.id.setB1, R.id.setB2, R.id.setB3};
    //5 sets for Volleyball and Tennis
    private int[] setA = {R.id.setA1, R.id.setA2, R.id.setA3, R.id.setA4, R.id.setA5};
    private int[] setB = {R.id.setB1, R.id.setB2, R.id.setB3, R.id.setB4, R.id.setB5};
    //9 runs for VBaseball
    private int[] setABase = {R.id.setA1, R.id.setA2, R.id.setA3, R.id.setA4, R.id.setA5,
            R.id.setA6, R.id.setA7, R.id.setA8, R.id.setA9};
    private int[] setBBase = {R.id.setB1, R.id.setB2, R.id.setB3, R.id.setB4, R.id.setB5,
            R.id.setB6, R.id.setB7, R.id.setB8, R.id.setB9};
    //Declare 2 String for the getText() in case of change
    String aTitle, bTitle;
    //Tag for logs
    private final String LOG_TAG = SportActivity.class.getSimpleName();

    /** EditText field to enter the team A name */
    private EditText mAEditText;

    /** EditText field to enter the team B breed */
    private EditText mBEditText;

    /** EditText field to enter the team A score */
    private TextView mAscore;

    /** EditText field to enter the team B score */
    private TextView mBscore;

    private static final int EXISTING_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Examine the intent that was used to launch this activity
        mCurrentSport = getIntent().getIntExtra("name", R.id.soccer);

        switch (mCurrentSport){
            case R.string.basket:
                //Set the proper title to the SportActivity
                setTitle(R.string.basket);
                //and inflate the proper layout
                setContentView(R.layout.activity_basketball);
                break;
            case R.string.hand:
                //Set the proper title to the SportActivity
                setTitle(R.string.hand);
                //and inflate the proper layout
                setContentView(R.layout.activity_handball);
                break;
            case R.string.tenis:
                //Set the proper title to the SportActivity
                setTitle(R.string.tenis);
                //and inflate the proper layout
                setContentView(R.layout.activity_tennis);
                break;
            case R.string.badminton:
                //Set the proper title to the SportActivity
                setTitle(R.string.badminton);
                //and inflate the proper layout
                setContentView(R.layout.activity_badminton);
                break;
            case R.string.volley:
                //Set the proper title to the SportActivity
                setTitle(R.string.volley);
                //and inflate the proper layout
                setContentView(R.layout.activity_volleyball);
                break;
            case R.string.rugby:
                //Set the proper title to the SportActivity
                setTitle(R.string.rugby);
                //and inflate the proper layout
                setContentView(R.layout.activity_rugby);
                break;
            case R.string.futbol:
                //Set the proper title to the SportActivity
                setTitle(R.string.futbol);
                //and inflate the proper layout
                setContentView(R.layout.activity_football);
                break;
            case R.string.water:
                //Set the proper title to the SportActivity
                setTitle(R.string.water);
                //and inflate the proper layout
                setContentView(R.layout.activity_waterpolo);
                break;
            case R.string.base:
                //Set the proper title to the SportActivity
                setTitle(R.string.base);
                //and inflate the proper layout
                setContentView(R.layout.activity_baseball);
                break;
            default:
                //Set the proper title to the SportActivity
                setTitle(R.string.soccer);
                //and inflate the proper layout
                setContentView(R.layout.activity_soccer);
        }
        // Find all relevant views that we will need to read user input from
        mAEditText = (EditText) findViewById(R.id.editA);
        mBEditText = (EditText) findViewById(R.id.editB);
        mAscore = (TextView) findViewById(R.id.teama);
        mBscore = (TextView) findViewById(R.id.teamb);

        //Add background thread for db actions
        getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);

        if (mCurrentSport == R.string.base || mCurrentSport == R.string.volley || mCurrentSport == R.string.badminton
                || mCurrentSport == R.string.tenis){
            //Checks if any EditText has been changed
            final EditText editTxtA = (EditText) findViewById(R.id.editA);
            final EditText editTxtB = (EditText) findViewById(R.id.editB);
            //Set a listener in both Edit Text
            //EditText for Team A
            editTxtA.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    TextView txt = (TextView) findViewById(R.id.titleA);
                    aTitle = editTxtA.getText().toString();
                    txt.setText(aTitle);
                    mChangedEditA=true;
                }
            });
            //EditText for Team B
            editTxtB.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    TextView txt = (TextView) findViewById(R.id.titleB);
                    bTitle = editTxtB.getText().toString();
                    txt.setText(bTitle);
                    mChangedEditB=true;
                }
            });
        }
    }
    /**
     * Inflates App Bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/main_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.sport_menu, menu);
        return true;
    }

    /**
     * Defines action in App Bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_share:
                // TODO: Define action share with SM
                return true;
            case R.id.action_save:
                //Save score to database
                saveSport();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(SportActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //   @pointToScoreA: Add points to team A
    public void pointToScoreA(View view){
        switch (view.getId()){
            case R.id.pts_6a:
                teamA = teamA + 6;
                break;
            case R.id.pts_5a:
                teamA = teamA + 5;
                break;
            case R.id.pts_3a:
                teamA = teamA + 3;
                break;
            case R.id.pts_2a:
                teamA = teamA + 2;
                break;
            default:
                teamA = teamA + 1;
        }
        display(teamA,R.id.teama);
    }
    //   @pointToScoreB: Add points to team B
    public void pointToScoreB(View view){
        switch (view.getId()){
            case R.id.pts_6b:
                teamB = teamB + 6;
                break;
            case R.id.pts_5b:
                teamB = teamB + 5;
                break;
            case R.id.pts_3b:
                teamB = teamB + 3;
                break;
            case R.id.pts_2b:
                teamB = teamB + 2;
                break;
            default:
                teamB = teamB + 1;
        }
        display(teamB, R.id.teamb);
    }
    //    @BadScoreA: points within one game
    public void BadScoreA(View view){
        if (teamA == 29) {
            display(30, setBA[iSet]);
            numA = numA + 1;
            teamB = 0;
            iSet = iSet + 1;
            teamA = 0;
        }else if(teamA >= 20 && (teamA - teamB) >= 1){
            display(teamA+1,setBA[iSet]);
            teamA = 0; numA = numA + 1;
            teamB = 0; iSet = iSet + 1;
        }else{
            teamA = teamA + 1;
            display(teamA,setBA[iSet]);
            display(teamB, setBB[iSet]);
        }
        //Check if teamA has won
        if(numA == 2){
            sendNotify(mChangedEditA, aTitle, "A");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }

    }
    //    @BadScoreB: points within one game
    public void BadScoreB(View view){
        if(teamB == 29){
            display(30,setBB[iSet]);
            numB = numB + 1;
            iSet = iSet + 1;
            teamA = 0; teamB = 0;
        }else if(teamB >= 20 && (teamB - teamA) >= 1){
            display(teamB+1,setBB[iSet]);
            teamA = 0; iSet = iSet + 1;
            teamB = 0; numB = numB + 1;
        }else{
            teamB = teamB + 1;
            display(teamA,setBA[iSet]);
            display(teamB, setBB[iSet]);
        }
        //Check if teamB has won
        if(numB == 2){
            sendNotify(mChangedEditB, bTitle, "B");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }
    }
    //    @VollScoreA: points within one game
    public void VollScoreA(View view){
        if (teamA >= 14 && (teamA - teamB) >= 1 && iSet == 4){
            display(teamA+1,setA[iSet]);
            numA = numA + 1;
        } else if(teamA >= 24 && (teamA - teamB) >= 1){
            display(teamA+1,setA[iSet]);
            teamA = 0; numA = numA + 1;
            teamB = 0; iSet = iSet + 1;
        }else{
            teamA = teamA + 1;
            display(teamA,setA[iSet]);
            display(teamB, setB[iSet]);
        }
        //Check if teamA has won
        if(numA >= 3 && (numA - numB)>= 2){
            sendNotify(mChangedEditA, aTitle, "A");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }
    }
    //    @VollScoreB: points within one game
    public void VollScoreB(View view){
        if(teamB >= 14 && iSet == 4 && (teamB - teamA) >= 1 ){
            display(teamB+1,setB[iSet]);
            numB = numB + 1;
        }else if(teamB >= 24 && (teamB - teamA) >= 1){
            display(teamB+1,setB[iSet]);
            teamA = 0; iSet = iSet + 1;
            teamB = 0; numB = numB + 1;
        }else{
            teamB = teamB + 1;
            display(teamA,setA[iSet]);
            display(teamB, setB[iSet]);
        }
        //Check if teamB has won
        if(numB >= 3 && (numB - numA)>= 2){
            sendNotify(mChangedEditB, bTitle, "B");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }
    }
    //    @TenScoreA: points within one game (0, 15, 30, 40, A)
    public void TenScoreA(View view) {
        if (teamA == 0) {
            teamA = 15;
            display(teamA, R.id.teama);
        } else if (teamA == 15) {
            teamA = 30;
            display(teamA, R.id.teama);
        } else if (teamA == 30) {
            teamA = 40;
            display(teamA, R.id.teama);
        } else if (teamA == 40 && teamB < 40) {
            teamA = 0; gameA = gameA + 1;
            teamB = 0;
            display(gameA, setA[iSet]);
            display(teamA, R.id.teama);
            display(teamB, R.id.teamb);
        } else if (teamA == 40 && teamB == 40) {
            teamA = 50;
            display(teamA, R.id.teama);
        } else if (teamA == 50 && teamB == 40) {
            teamA = 0; gameA = gameA + 1;
            teamB = 0;
            display(gameA,setA[iSet]);
            display(teamA, R.id.teama);
            display(teamB, R.id.teamb);
        } else if (teamA == 40 && teamB == 50) {
            teamB = 40;
            display(teamB, R.id.teamb);
        }
        if (gameA == 6 && gameB <5){
            iSet = iSet + 1;
            gameA = 0; gameB = 0;
            A_set = A_set + 1;
        }
        if (gameA == 7){
            iSet = iSet + 1;
            gameA = 0; gameB = 0;
            A_set = A_set + 1;
        }
        if(A_set >= 3){
            sendNotify(mChangedEditA, aTitle, "A");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }
    }
    //    @TenScoreB: points within one game (0, 15, 30, 40, A)
    public void TenScoreB(View view) {
        if (teamB == 0) {
            teamB = 15;
            display(teamB, R.id.teamb);
        } else if (teamB == 15) {
            teamB = 30;
            display(teamB, R.id.teamb);
        } else if (teamB == 30) {
            teamB = 40;
            display(teamB, R.id.teamb);
        } else if (teamB == 40 && teamA < 40) {
            teamA = 0; gameB = gameB + 1;
            teamB = 0;
            display(gameB, setB[iSet]);
            display(teamA, R.id.teama);
            display(teamB, R.id.teamb);
        } else if (teamA == 40 && teamB == 40) {
            teamB = 50;
            display(teamB, R.id.teamb);
        } else if (teamB == 50 && teamA == 40) {
            teamA = 0; gameB = gameB + 1;
            teamB = 0;
            display(gameB,setB[iSet]);
            display(teamA, R.id.teama);
            display(teamB, R.id.teamb);
        } else if (teamB == 40 && teamA == 50) {
            teamA = 40;
            display(teamA, R.id.teama);
        }
        if (gameB == 6 && gameA <5){
            iSet = iSet + 1;
            gameA = 0; gameB = 0;
            B_set = B_set + 1;
        }
        if (gameB == 7){
            iSet = iSet + 1;
            gameA = 0; gameB = 0;
            B_set = B_set + 1;
        }
        if(B_set >= 3){
            sendNotify(mChangedEditB, bTitle, "B");
            //If one team wins is not possible to keep adding points unles RESET
            makeinVisible();
        }
    }
    //    @ballCounter: balls counter for baseball match
    public void ballCounter(View view){
        if (balls == 3){
            Toast.makeText(SportActivity.this, "BALL #4", Toast.LENGTH_SHORT).show();
            balls = 0; strike = 0;
            display(balls, R.id.txt_balls);
            display(strike, R.id.txt_strike);
        } else {
            balls = balls + 1;
            display(balls, R.id.txt_balls);
        }
    }
    //    @strikeCounter: strike counter for baseball match
    public void strikeCounter(View view){
        if (strike == 2) {
            Toast.makeText(SportActivity.this, "STRIKE #3", Toast.LENGTH_SHORT).show();
            strike = 0; balls = 0;
            out = out + 1;
            if (out == 2){
                TextView txt = (TextView) findViewById(R.id.txt_out);
                outCounter(txt);
            }
            display(balls, R.id.txt_balls);
            display(strike, R.id.txt_strike);
            display(out, R.id.txt_out);
        } else {
            strike = strike + 1;
            display(strike, R.id.txt_strike);
        }
    }
    //    @outCounter: out counter for baseball match
    public void outCounter(View view){
        if (out == 2) {
            i = i + 1; balls = 0;
            strike = 0; out = 0;
            teamA = 0; teamB = 0;
            Toast.makeText(SportActivity.this, "3 OUTS!", Toast.LENGTH_SHORT).show();
            if ((i % 2) == 0){iSet = iSet + 1;}
            display(balls, R.id.txt_balls);
            display(strike, R.id.txt_strike);
            display(out, txt_out);
        }else{
            out = out + 1;
            display(out, txt_out);
        }
    }
    //    @runCounter: run counter for baseball match
    public void runCounter(View view){
        if(view.getId()== R.id.runA){
            teamA = teamA + 1;
            display(teamA, setABase[iSet]);
        }else{
            teamB = teamB + 1;
            display(teamB, setBBase[iSet]);
        }

    }
    /**
     * @isVisible: checks visibility and changes it
     * when clicked Reset button
     */
    private void isVisible() {
        Button a = (Button) findViewById(R.id.bttA);
        Button b = (Button) findViewById(R.id.bttB);
        if (b.getVisibility() == View.GONE) {
            a.setVisibility(View.VISIBLE);
            b.setVisibility(View.VISIBLE);
        }
    }
    /**
     * @makeinVisible: changes visibility when
     */
    private void makeinVisible() {
        Button a = (Button) findViewById(R.id.bttA);
        Button b = (Button) findViewById(R.id.bttB);
        a.setVisibility(View.GONE);
        b.setVisibility(View.GONE);
    }
    /**
     * Creates a Toast announcing the team winner
     * @param mChange
     */
    private void sendNotify(boolean mChange, String team, String AB) {
        if (!mChange){
            Toast.makeText(this,  "Team "+ AB +" wins!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, team + " wins!", Toast.LENGTH_SHORT).show();
        }
    }
    //    @display: Display on screen total score
    private void display(int team,int idTeam){
        if (team == 50) {
            TextView score = (TextView) findViewById(idTeam);
            score.setText(getString(R.string.adv));
        } else {
            TextView score = (TextView) findViewById(idTeam);
            score.setText("" + team);
        }
    }
    //    @reset: restarts the game
    public void reset(View view){
        switch (view.getId()) {
            case R.id.reset_bset:
                setReset(setBA.length, setBA, setBB);
                isVisible();
                break;
            case R.id.reset_base:
                setReset(setABase.length, setABase, setBBase);
                display(balls, R.id.txt_balls);
                display(strike, R.id.txt_strike);
                display(out, txt_out);
                break;
            case R.id.reset_vset:
                setReset(setA.length, setA, setB);
                isVisible();
                break;
            case R.id.reset_tset:
                setReset(setA.length, setA, setB);
                TextView tA = (TextView) findViewById(R.id.teama);
                TextView tB = (TextView) findViewById(R.id.teamb);
                tA.setText(getString(R.string.cero));
                tB.setText(getString(R.string.cero));
                isVisible();
                break;
            default:
                teamA = 0; teamB = 0;
                TextView pA = (TextView) findViewById(R.id.teama);
                TextView pB = (TextView) findViewById(R.id.teamb);
                pA.setText(""+ teamA);
                pB.setText(""+ teamB);
        }
    }
    //    @setReset: reset special case
    private void setReset(int length, int[] Aset, int[] Bset) {
        teamA = 0; iSet = 0; numA = 0;
        teamB = 0; numB = 0; balls = 0;
        teamB = 0; strike = 0; out = 0;
        for(int i = 0; i < length; i++){
            TextView tA = (TextView) findViewById(Aset[i]);
            TextView tB = (TextView) findViewById(Bset[i]);
            tA.setText(getString(R.string.tset));
            tB.setText(getString(R.string.tset));
        }
    }
    /**
     * Get user input from editor and save new sport into database
     */
    public void saveSport(){
        // Create a ContentValues object where column names are the keys,
        // and sport attributes are the values.
        ContentValues values = new ContentValues();
        if (TextUtils.isEmpty( mAEditText.getText().toString())){
            values.put(SportEntry.COLUMN_A_NAME, getString(R.string.teamA));
        }else{
            values.put(SportEntry.COLUMN_A_NAME, mAEditText.getText().toString().trim());
        }
        if (TextUtils.isEmpty( mBEditText.getText().toString())){
            values.put(SportEntry.COLUMN_B_NAME, getString(R.string.teamb));
        }else{
            values.put(SportEntry.COLUMN_B_NAME, mBEditText.getText().toString().trim());
        }
        values.put(SportEntry.COLUMN_A_SCORE, Integer.parseInt(mAscore.getText().toString().trim()));
        values.put(SportEntry.COLUMN_B_SCORE, Integer.parseInt(mBscore.getText().toString().trim()));
        //if there is no changes and score is 0 - 0. Don't save else save.
        if (TextUtils.isEmpty(mAEditText.getText().toString().trim()) && TextUtils.isEmpty(mBEditText.getText().toString().trim()) &&
                Integer.parseInt(mAscore.getText().toString().trim())==0
                && Integer.parseInt(mBscore.getText().toString().trim())==0) {return;}
        else{
            // Insert a new sport into the provider, returning the content URI for the new sport.
            Uri newUri = getContentResolver().insert(SportEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.toast_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.toast_save),
                        Toast.LENGTH_SHORT).show();
                //go to RecordActivity
                Intent i = new Intent(SportActivity.this, RecordActivity.class);
                startActivity(i);
            }
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all sport attributes, define a projection that contains
        // all columns from the sports table
        Uri currentPetUri = Uri.withAppendedPath(SportContract.BASE_CONTENT_URI, SportEntry.TABLE_NAME);
        String[] projection = {
                SportEntry._ID,
                SportEntry.COLUMN_A_NAME,
                SportEntry.COLUMN_B_NAME,
                SportEntry.COLUMN_A_SCORE,
                SportEntry.COLUMN_B_SCORE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentPetUri,          // Query the content URI for the current sport
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        // If the score hasn't changed, continue with handling back button press
        if (TextUtils.isEmpty(mAEditText.getText().toString().trim()) && TextUtils.isEmpty(mBEditText.getText().toString().trim()) &&
                Integer.parseInt(mAscore.getText().toString().trim())==0 && Integer.parseInt(mBscore.getText().toString().trim())==0) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
}
