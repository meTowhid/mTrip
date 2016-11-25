package towhid.icurious.mtrip.model;

import com.google.firebase.database.Exclude;

/**
 * Created by Towhid on 11/22/2016.
 */

public class modelTour {
    @Exclude
    public String key;
    public double budget;
    public String title, description, image;

    public modelTour() {
    }

    public modelTour(String title, String description, double budget, String image) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.image = image;
    }
}
