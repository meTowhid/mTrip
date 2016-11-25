package towhid.icurious.mtrip.tab.fragments;


import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import towhid.icurious.mtrip.model.mMoment;
import towhid.icurious.mtrip.utils.mFireData;


/**
 * Created by Towhid on 11/15/2016.
 */
public class MomentClickHandler {
    private final mMoment moment;

    public MomentClickHandler(mMoment m) {
        this.moment = m;
    }

    public void onClick(View view) {
        Snackbar.make(view, moment.name + " is selected", Snackbar.LENGTH_LONG).show();
    }

    public boolean onLongClick(View view) {
        Snackbar.make(view, moment.name + " Long pressed!", Snackbar.LENGTH_LONG).show();
        new AlertDialog.Builder(view.getContext())
                .setTitle("Delete this record?")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFireData.getDatabase().getReference().getRoot()
                                .child("root/users/moments/" + moment.key).removeValue();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


        return true;
    }
}