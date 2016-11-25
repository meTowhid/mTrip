package towhid.icurious.mtrip.tab;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.databinding.ActivityTourDetailBinding;
import towhid.icurious.mtrip.model.mCost;
import towhid.icurious.mtrip.model.mMoment;
import towhid.icurious.mtrip.tab.fragments.CostFragment;
import towhid.icurious.mtrip.tab.fragments.MomentsFragment;
import towhid.icurious.mtrip.utils.mFireData;

public class TourDetailActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ActivityTourDetailBinding binding;
    ImageView img;
    Intent imgData = null;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tour_detail);
        key = getIntent().getStringExtra("tourKey");
        Bundle bundle = new Bundle();
        bundle.putString("tourKey", key);
        bundle.putString("budget", getIntent().getStringExtra("budget"));

        MomentsFragment momentsFragment = new MomentsFragment();
        momentsFragment.setArguments(bundle);

        CostFragment costFragment = new CostFragment();
        costFragment.setArguments(bundle);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(costFragment, "Cost");
        viewPagerAdapter.addFragments(momentsFragment, "Moments");
        binding.container.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.container);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                switch (binding.container.getCurrentItem()) {
                    case 0:
                        addCostDialog();
                        break;
                    case 1:
                        saveMomentDialog();
                        break;

                }
            }
        });
    }

    private void addCostDialog() {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_add_cost);
        d.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText et_title = (EditText) d.findViewById(R.id.et_costTitle);
        final EditText et_amount = (EditText) d.findViewById(R.id.et_costAmount);


        d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = et_title.getText().toString().trim();
                String amount = et_amount.getText().toString().trim();

                if (t.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(TourDetailActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    mFireData.getCostListRef(key).push().setValue(new mCost(t, Float.valueOf(amount)));
                    Toast.makeText(TourDetailActivity.this, "Cost added to list", Toast.LENGTH_SHORT).show();
                    d.dismiss();
                }
            }
        });
    }


    private void saveMomentDialog() {
        imgData = null;
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choose_image);

        img = (ImageView) dialog.findViewById(R.id.img);
        final TextView text = (TextView) dialog.findViewById(R.id.momentTitle);
        Button btn = (Button) dialog.findViewById(R.id.saveBtn);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.getText().toString().trim().isEmpty() && imgData != null) {
                    //do something
                    mFireData.getMomentsListRef(key).push()
                            .setValue(new mMoment(text.getText().toString().trim(), imgData.getDataString()));
                    Toast.makeText(TourDetailActivity.this, "Moment saved successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else
                    Toast.makeText(TourDetailActivity.this, "None of the items can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Picasso.with(this)
                    .load(data.getDataString())
                    .centerCrop().fit()
                    .error(R.mipmap.ic_launcher)
                    .into(img);
            imgData = data;


        }
    }
}
