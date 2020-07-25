package com.example.galleryversionone;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class ImageListAdapter extends PagedListAdapter<ImageDocument, ImageViewHolder> {

    private Context mContext;

    public HashMap<Long, ImageDocument> selectedImages = new HashMap<>();

    protected ImageListAdapter(@NonNull DiffUtil.ItemCallback<ImageDocument> diffCallback, Context context) {
        super(diffCallback);
        mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {

        final ImageDocument imgDoc = getItem(position);
        if (selectedImages != null && selectedImages.containsKey(imgDoc.id)) {
            updateSelectedImageUI(mContext, Uri.parse(imgDoc.fileContentUri), holder.mImageView);
        } else {
            holder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext)
                    .load(Uri.parse(imgDoc.fileContentUri))
//                    .thumbnail(0.1f)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
//                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.mImageView);
        }

    }

    public void setSelectedImages(HashMap<Long, ImageDocument> list) {
        selectedImages = list;
    }

    public void updateSelectedImageUI(Context context, Uri uri, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Glide.with(context)
                .load(uri)
                .override(200, 200)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(imageView);
    }


}