package com.example.galleryversionone;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private final Context mContext;

    private final List<ImageDocument> mImageDocuments = new ArrayList<ImageDocument>();

    /**
     * The total size of the all images. This number should be the size for all images even if
     * they are not fetched from the ContentProvider.
     */
    private int mTotalSize;


    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Resources resources = mContext.getResources();
        if(mImageDocuments.size() > position){
            Glide.with(mContext)
                    .load(mImageDocuments.get(position).mAbsolutePath)
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.mImageView);
        }else{
            holder.mImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(resources, R.drawable.image_placeholder, null));
        }
    }

    @Override
    public int getItemCount() {
        return mTotalSize;
    }

    /**
     * Add an image as part of the adapter.
     *
     * @param imageDocument the image information to be added
     */
    void add(ImageDocument imageDocument) {
        mImageDocuments.add(imageDocument);
    }

    /**
     * Set the total size of all images.
     *
     * @param totalSize the total size
     */
    void setTotalSize(int totalSize) {
        mTotalSize = totalSize;
    }

    /**
     * @return the number of images already fetched and added to this adapter.
     */
    int getFetchedItemCount() {
        return mImageDocuments.size();
    }

    /**
     * Represents information for an image.
     */
    static class ImageDocument {

        String mAbsolutePath;

        String mDisplayName;
    }
}
