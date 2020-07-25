package com.example.galleryversionone;

import android.net.Uri;
import java.io.Serializable;

public class ImageDocument implements Serializable {
    Long id;
    String fileDisplayName;
    String fileContentUri;
    Long fileSize;
    String fileMimeType;
    String fileDateAdded;
    String fileDateTaken;
    String fileBucketName;

    public ImageDocument(Long id, String fileDisplayName, String fileContentUri,
                         Long fileSize, String fileMimeType, String fileDateAdded,
                         String fileDateTaken, String fileBucketName) {
        this.id = id;
        this.fileDisplayName = fileDisplayName;
        this.fileContentUri = fileContentUri;
        this.fileSize = fileSize;
        this.fileMimeType = fileMimeType;
        this.fileDateAdded = fileDateAdded;
        this.fileDateTaken = fileDateTaken;
        this.fileBucketName = fileBucketName;

    }
}
