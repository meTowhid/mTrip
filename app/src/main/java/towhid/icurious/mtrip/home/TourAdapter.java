package towhid.icurious.mtrip.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.model.modelTour;

/**
 * Created by Towhid on 11/22/2016.
 */

public class TourAdapter extends RecyclerView.Adapter<TourViewHolder> {
    private final List<modelTour> tours;

    public TourAdapter(List<modelTour> tours) {
        this.tours = tours;
    }

    @Override
    public TourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View statusContainer = inflater.inflate(R.layout.item_tour, parent, false);
        return new TourViewHolder(statusContainer);
    }

    @Override
    public void onBindViewHolder(TourViewHolder holder, int position) {
        modelTour t = tours.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }
}