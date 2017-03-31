package my.app.adrian.universalscore.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class SportContract {
    private SportContract(){}
    //Content Authority similar to AndroidManifest
    public static final String CONTENT_AUTHORITY = "my.app.adrian.universalscore";
    //Generates the Uri needed
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class SportEntry implements BaseColumns{

        //Table name
        public final static String TABLE_NAME = "scores";
        //Generates final Uri for each Table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        //Columns names
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_A_NAME = "Ateam";
        public final static String COLUMN_B_NAME = "Bteam";
        public final static String COLUMN_A_SCORE = "Ascore";
        public final static String COLUMN_B_SCORE = "Bscore";
    }
}
