package my.app.adrian.universalscore;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Display list of option and opens possibility
 * to get to the score
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(" ");

        //Declaration arraylist
        final ArrayList<Sport> sportArray = new ArrayList<>();
        sportArray.add(new Sport(R.string.soccer, R.drawable.soccer));
        sportArray.add(new Sport(R.string.basket, R.drawable.basket));
        sportArray.add(new Sport(R.string.hand, R.drawable.handball));
        sportArray.add(new Sport(R.string.tenis, R.drawable.tennis));
        sportArray.add(new Sport(R.string.badminton, R.drawable.badminton));
        sportArray.add(new Sport(R.string.volley, R.drawable.volley));
        sportArray.add(new Sport(R.string.rugby, R.drawable.rugby));
        sportArray.add(new Sport(R.string.futbol, R.drawable.football));
        sportArray.add(new Sport(R.string.base, R.drawable.base));
        sportArray.add(new Sport(R.string.water, R.drawable.water));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        SportAdapter adapter = new SportAdapter(this, sportArray);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);
        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        //Setup item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the {@link Sport} object at the given position the user clicked on
                Sport sport = sportArray.get(position);
                //Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, SportActivity.class);
                Toast.makeText(MainActivity.this, "New "+ getString(sport.getmSportName()) +" Game", Toast.LENGTH_SHORT).show();
                //Set the id on the data field of the intent
                intent.putExtra("name",sport.getmSportName());
                startActivity(intent);
            }
        });


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
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            // Respond to a click on the "Records" menu option
            case R.id.action_list:
                goRecordActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Leaves MainActivity and opens RecordActivity
     */
    public void goRecordActivity() {
        Intent i = new Intent(MainActivity.this, RecordActivity.class);
        Toast.makeText(this, getString(R.string.toast_record_list), Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
