package towhid.icurious.mtrip.home;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import towhid.icurious.mtrip.databinding.ItemTourBinding;
import towhid.icurious.mtrip.model.modelTour;

/**
 * Created by Towhid on 11/22/2016.
 */


public class TourViewHolder extends RecyclerView.ViewHolder {

    private ItemTourBinding binding;

    public TourViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void bind(modelTour tour) {
        binding.setTour(tour);
        binding.setHandler(new TourClickHandler(tour));
    }


}
