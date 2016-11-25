package towhid.icurious.mtrip.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Towhid on 11/25/2016.
 */

public class mCost {
    @Exclude
    public String id;
    public String title;
    public float amount;

    public mCost() {
    }

    public mCost( String title, float amount) {
        this.title = title;
        this.amount = amount;
    }
}
