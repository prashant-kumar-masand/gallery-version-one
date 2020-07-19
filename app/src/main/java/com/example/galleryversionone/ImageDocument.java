package com.example.galleryversionone;

import android.net.Uri;

public class ImageDocument {
    Long id;
    String mDisplayName;
    Uri mAbsolutePath;

    public ImageDocument(Long id, String mDisplayName, Uri mAbsolutePath) {
        this.id = id;
        this.mDisplayName = mDisplayName;
        this.mAbsolutePath = mAbsolutePath;
    }
}
