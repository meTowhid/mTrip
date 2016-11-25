package towhid.icurious.mtrip.home;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import towhid.icurious.mtrip.R;

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
//        if (!url.trim().isEmpty())
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView);
    }
}