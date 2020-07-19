package com.example.galleryversionone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

public class ImageListAdapter extends PagedListAdapter<ImageDocument, ImageViewHolder> {

    private Context mContext;

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
        System.out.println(imgDoc.mAbsolutePath);

//        InputStream inputStream = null;
//        try {
//            inputStream = mContext.getContentResolver().openInputStream(getItem(position).mAbsolutePath);
//            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//            holder.mImageView.setImageBitmap(bmp);
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Glide.with(mContext)
                .load(imgDoc.mAbsolutePath)
//                .thumbnail(0.33f)
//                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.mImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, imgDoc.mDisplayName, Toast.LENGTH_SHORT).show();
            }
        });
    }



}