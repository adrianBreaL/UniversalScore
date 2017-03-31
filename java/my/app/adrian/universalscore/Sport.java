package my.app.adrian.universalscore;

/**
 * This class define a new variable and their methods
 */
public class Sport {
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mSportName;
    private int mSportImage = NO_IMAGE_PROVIDED;

    public Sport(int sSportName, int sSportImage){
        mSportName = sSportName;
        mSportImage = sSportImage;
    }

    public int getmSportName(){
        return mSportName;
    }
    public int getmSportImage(){
        return mSportImage;
    }

    //    returns whether or not has Image
    public boolean hasImage(){
        return mSportImage != NO_IMAGE_PROVIDED;
    }
}
