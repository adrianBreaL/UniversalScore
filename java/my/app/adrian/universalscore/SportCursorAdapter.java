package my.app.adrian.universalscore;


import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SportCursorAdapter extends CursorAdapter{

    /**
     * Constructs a new {@link SportCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public SportCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_sport, parent, false);
    }
    /**
     * This method binds the game data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//Find individual views that we want to modify in the list item layout
//        TextView nameTextView = (TextView) view.findViewById(R.id.name);
//        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
//
//        //Find the columns of pet attributes that we're interested in
//        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
//        int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
//
//        // If the pet breed is empty string or null, then use some default text
//        // that says "Unknown breed", so the TextView isn't blank.
//        if (TextUtils.isEmpty(cursor.getString(breedColumnIndex))) {
//            String petBreed = context.getString(R.string.unknown_breed);
//            summaryTextView.setText(petBreed);
//        }
//
//        //Update the TextView with the attributes for the current pet
//        nameTextView.setText(cursor.getString(nameColumnIndex));
//        summaryTextView.setText(cursor.getString(breedColumnIndex));
    }
}
