package towhid.icurious.mtrip.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import towhid.icurious.mtrip.R;
import towhid.icurious.mtrip.databinding.ActivityMainBinding;
import towhid.icurious.mtrip.login.LoginActivity;
import towhid.icurious.mtrip.model.modelTour;
import towhid.icurious.mtrip.utils.mFireData;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private RecyclerView mList;
    private FirebaseRecyclerAdapter<modelTour, TourViewHolder> adapter;
    private ActivityMainBinding binding;
    private Intent imgData;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mList = binding.mList;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mList.setLayoutManager(mLayoutManager);
        mList.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new FirebaseRecyclerAdapter<modelTour, TourViewHolder>(
                modelTour.class,
                R.layout.item_tour,
                TourViewHolder.class,
                mFireData.getTourListRef()) {
            @Override
            protected void populateViewHolder(TourViewHolder viewHolder, modelTour model, int position) {
                model.key = adapter.getRef(position).getKey();
                viewHolder.bind(model);
            }
        };
        mList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        });
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newTourFabClicked(View view) {

        imgData = null;
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_trip);

        imageView = (ImageView) dialog.findViewById(R.id.img);
        final TextView title = (TextView) dialog.findViewById(R.id.tourTitle);
        final TextView desc = (TextView) dialog.findViewById(R.id.tourDescription);
        final TextView budget = (TextView) dialog.findViewById(R.id.tourBudget);
        Button btn = (Button) dialog.findViewById(R.id.saveBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
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
                String bud = budget.getText().toString().trim();
                if (!title.getText().toString().trim().isEmpty()) {
                    modelTour m = new modelTour(title.getText().toString().trim(),
                            desc.getText().toString().trim(),
                            bud.isEmpty() ? 0 : Double.valueOf(bud),
                            imgData == null ? "" : imgData.getDataString());

                    mFireData.getTourListRef().push().setValue(m);
                    Toast.makeText(MainActivity.this, "Tour created successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Title can't be empty!", Toast.LENGTH_SHORT).show();
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
                    .into(imageView);
            imgData = data;


        }
    }
}
