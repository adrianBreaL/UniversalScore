package my.app.adrian.universalscore;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * SportAdapter works at MainActivity
 * Gets info from ArrayList<Sport> and put this into UI
 */
public class SportAdapter extends ArrayAdapter<Sport> {

    //Get Name of class to locate log
    private final String LOG_TAG = SportAdapter.class.getSimpleName();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_sport, parent, false);
        }
        // Get the {@link Word} object located at this position in the list
        Sport currentSport = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameSport = (TextView) listItemView.findViewById(R.id.sport_name);
        // Get the sport name from the current Sport object and
        // set this text on the name TextView
        try{
            nameSport.setText(getContext().getString(currentSport.getmSportName()));
        }catch (NullPointerException e){
            Log.e(LOG_TAG, "Empty name in method getmSportName");
        }

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        ImageView sportImg = (ImageView) listItemView.findViewById(R.id.sport_image);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        if(currentSport.hasImage()){
            sportImg.setImageResource(currentSport.getmSportImage());
            sportImg.setVisibility(View.VISIBLE);
        }else{
            sportImg.setVisibility(View.GONE);
        }
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
    // Constructor
    public SportAdapter(Activity context, ArrayList<Sport> sports){
        super(context, 0, sports);
    }
}
