package towhid.icurious.mtrip.tab.fragments;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Towhid on 11/15/2016.
 */
public final class ImageBinder {

    private ImageBinder() {
        //NO-OP
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Picasso.with(context).load(url).centerCrop().fit().into(imageView);
    }
}