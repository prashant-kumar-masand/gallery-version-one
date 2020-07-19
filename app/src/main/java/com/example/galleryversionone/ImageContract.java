package com.example.galleryversionone;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

public class ImageContract {

    static final Uri CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    static final String[] PROJECTION_ALL = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };
}
