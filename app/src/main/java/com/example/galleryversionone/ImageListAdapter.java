package com.example.galleryversionone;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class ImageListAdapter extends PagedListAdapter<ImageDocument, ImageViewHolder> {

    private Context mContext;

    public HashMap<String, Boolean> selectedImages = new HashMap<String, Boolean>();

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
        if(selectedImages != null && selectedImages.containsKey(imgDoc.mAbsolutePath.getPath())){
            updateSelectedImageUI(mContext, imgDoc.mAbsolutePath, holder.mImageView);
        }else{
            holder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext)
                    .load(imgDoc.mAbsolutePath)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.mImageView);
        }

    }

    public void setSelectedImages(HashMap<String, Boolean> list){
        selectedImages = list;
    }

    public void updateSelectedImageUI(Context context, Uri uri, ImageView imageView){
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