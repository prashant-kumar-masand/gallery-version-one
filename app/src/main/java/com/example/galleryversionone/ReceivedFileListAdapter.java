package com.example.galleryversionone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ReceivedFileListAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private Context mContext;
    private List<Bitmap> thumbnailList = new ArrayList<>();
    private Map<Integer, Integer> imagesMapper = new HashMap<Integer, Integer>();

    public ReceivedFileListAdapter(Context context) {
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

        holder.mImageView.setScaleType(ImageView.ScaleType.FIT_START);
        holder.mImageView.setBackground(new BitmapDrawable(mContext.getResources(), thumbnailList.get(position)));

        if(imagesMapper.containsKey(position)){

            Glide.with(mContext)
                    .load(thumbnailList.get(position))
                    .override(100, imagesMapper.get(position))
//                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .transform(new CenterCrop(),new BlurTransformation(25, 3))
                    .into(holder.mImageView);

        }else{

            Glide.with(mContext)
                    .load(thumbnailList.get(position))
//                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .transform(new CenterCrop(),new BlurTransformation(25, 3))
                    .into(holder.mImageView);
        }

    }

    @Override
    public int getItemCount() {
        return thumbnailList.size();
    }

    public void updateThumbnailList(List<Bitmap> bitmapList){
        thumbnailList = bitmapList;
    }

    public void updateThumbnailMapper(Map<Integer, Integer> mapper){
        imagesMapper = mapper;
    }

}
