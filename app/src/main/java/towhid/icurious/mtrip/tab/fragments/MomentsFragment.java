package towhid.icurious.mtrip.tab.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.databinding.FragmentMomentBinding;
import towhid.icurious.mtrip.model.mMoment;
import towhid.icurious.mtrip.utils.mFireData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentsFragment extends Fragment {

    private FragmentMomentBinding binding;
    private RecyclerView mList;
    private FirebaseRecyclerAdapter<mMoment, MomentViewHolder> adapter;
    private String key;

    public MomentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_moment, container, false);
        key = getArguments().getString("tourKey");

        mList = binding.list;
        mList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mList.setLayoutManager(mLayoutManager);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new FirebaseRecyclerAdapter<mMoment, MomentViewHolder>(
                mMoment.class,
                R.layout.item_moment,
                MomentViewHolder.class,
                mFireData.getMomentsListRef(key)) {
            @Override
            protected void populateViewHolder(MomentViewHolder viewHolder, mMoment model, int position) {
                model.key = adapter.getRef(position).getKey();
                viewHolder.bind(model);
            }
        };
        mList.setAdapter(adapter);
    }
}
