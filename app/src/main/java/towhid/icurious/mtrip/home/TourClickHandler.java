package towhid.icurious.mtrip.home;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

import towhid.icurious.mtrip.model.modelTour;
import towhid.icurious.mtrip.tab.TourDetailActivity;

/**
 * Created by Towhid on 11/22/2016.
 */

public class TourClickHandler {
    private final modelTour tour;

    public TourClickHandler(modelTour tour) {
        this.tour = tour;
    }

    public void onClick(View view) {
        Snackbar.make(view, tour.title + " is selected", Snackbar.LENGTH_LONG).show();
        Intent i = new Intent(view.getContext(), TourDetailActivity.class);
        i.putExtra("tourKey", tour.key);
        i.putExtra("budget", String.valueOf(tour.budget));
        view.getContext().startActivity(i);

    }
}

