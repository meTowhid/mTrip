package towhid.icurious.mtrip.utils;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Towhid on 11/22/2016.
 */

public class mFireData extends Application {

    private static FirebaseDatabase mDatabase;
    private static String uid;


    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return mDatabase;
    }

    public static DatabaseReference getMomentsListRef(String tourKey) {
        return getTourListRef().child(tourKey + "/momentList");
    }

    public static DatabaseReference getCostListRef(String costKey) {
        return getTourListRef().child(costKey + "/costList");
    }

    public static DatabaseReference getTourRef(String costKey) {
        return getTourListRef().child(costKey);
    }

    public static DatabaseReference getTourListRef() {
        return getDatabase().getReference().getRoot().child("users/" + uid + "/tourList");
    }

}
