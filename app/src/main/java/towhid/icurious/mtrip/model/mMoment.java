package towhid.icurious.mtrip.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Towhid on 11/15/2016.
 */

public class mMoment {
    @Exclude
    public String key;
    public String name;
    public String image;

    public mMoment() {
    }

    public mMoment(String name, String image) {
        this.name = name;
        this.image = image;
    }

}
