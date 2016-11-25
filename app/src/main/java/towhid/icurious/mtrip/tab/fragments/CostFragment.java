package towhid.icurious.mtrip.tab.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.model.mCost;
import towhid.icurious.mtrip.utils.mFireData;


/**
 * A simple {@link Fragment} subclass.
 */
public class CostFragment extends Fragment {

    private PieChart pie;
    private View v;
    private String key;
    private String budget;
    private int totalExp = 0;

    public CostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cost, container, false);
        key = getArguments().getString("tourKey");
        budget = getArguments().getString("budget");
        pie = (PieChart) v.findViewById(R.id.pie);
        initializePie();
        return v;
    }

    private void initializePie() {
        pie.setBackgroundColor(Color.parseColor("#e6e6e6"));

        pie.setDragDecelerationFrictionCoef(0.95f);

//        configure pie chart
        pie.setUsePercentValues(true);
//        Description description = new Description();
//        description.setText("Expenses Pie chart");
//        pie.setDescription(description);

//        enable hole and configure
        pie.setDrawHoleEnabled(true);
        pie.setHoleColor(Color.TRANSPARENT);
        pie.setHoleRadius(50f);
        pie.setTransparentCircleRadius(10);
//        mChart.setTransparentCircleAlpha(0);
//        mChart.setCenterTextColor(Color.BLACK);


//        enable rotation of the chart by touch
        pie.setRotationAngle(0);
//        mChart.setRotationEnabled(false);

        pie.setCenterTextSize(15);
        pie.setDrawEntryLabels(true);
        //mChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!
        pie.animateY(1000, Easing.EasingOption.EaseInOutSine);

        pie.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) return;
                Toast.makeText(getContext(), "Cost " + e.getY(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addDataSet();

        Legend l = pie.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
        l.setEnabled(false);

//        for (IDataSet<?> set : pie.getData().getDataSets()) set.setDrawValues(false);
        pie.setDrawEntryLabels(true);
    }

    private void addDataSet() {
        mFireData.getCostListRef(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PieEntry> yEntrys = new ArrayList<PieEntry>();

                for (DataSnapshot c : dataSnapshot.getChildren()) {
//                    Toast.makeText(getContext(), "" + c.toString(), Toast.LENGTH_SHORT).show();
                    mCost cost = c.getValue(mCost.class);
                    yEntrys.add(new PieEntry(cost.amount, cost.title));
                    totalExp += cost.amount;
                }
                mFireData.getTourRef(key).child("totalExp").setValue(totalExp);
                pie.setCenterText("total\n" + totalExp + "/" + budget);

                //create the data set
                PieDataSet pieDataSet = new PieDataSet(yEntrys, "Expenses list");
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setValueTextSize(12);

                //add colors to dataset
                ArrayList<Integer> colors = new ArrayList<>();
//        for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS) colors.add(c);
                for (int c : ColorTemplate.COLORFUL_COLORS) colors.add(c);
//                for (int c : ColorTemplate.LIBERTY_COLORS) colors.add(c);
//                for (int c : ColorTemplate.PASTEL_COLORS) colors.add(c);
//                colors.add(ColorTemplate.getHoloBlue());
                pieDataSet.setColors(colors);

                pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                pieDataSet.setValueLineColor(Color.WHITE);
//        pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueLinePart1Length(0.5f);
                pieDataSet.setValueLinePart2Length(0.4f);
                pieDataSet.setValueLineWidth(2);
                //pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


                //create pie data object
                PieData data = new PieData(pieDataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.GRAY);

                pie.setData(data);
                pie.highlightValue(null);
                pie.invalidate();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
