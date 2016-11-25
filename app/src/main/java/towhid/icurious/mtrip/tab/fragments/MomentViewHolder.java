package towhid.icurious.mtrip.tab.fragments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import towhid.icurious.mtrip.databinding.ItemMomentBinding;
import towhid.icurious.mtrip.model.mMoment;


/**
 * Created by Towhid on 11/15/2016.
 */
public class MomentViewHolder extends RecyclerView.ViewHolder {

    private ItemMomentBinding binding;

    public MomentViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void bind(mMoment m) {
        binding.setMoment(m);
        binding.setHandler(new MomentClickHandler(m));
    }
}