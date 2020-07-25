package com.example.galleryversionone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;

public class ImageDataSource extends PositionalDataSource<ImageDocument> {

    ContentResolver contentResolver;

    public ImageDataSource(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ImageDocument> callback) {
        callback.onResult(getContacts(params.requestedLoadSize, params.requestedStartPosition), 0);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ImageDocument> callback) {
        callback.onResult(getContacts(params.loadSize, params.startPosition));
    }

    private List<ImageDocument> getContacts(Integer limit, Integer offset) {
        Cursor cursor = contentResolver.query(ImageContract.CONTENT_URI,
                ImageContract.PROJECTION_ALL,
                null,
                null,
                ImageContract.PROJECTION_ALL[4] + " DESC LIMIT " + limit + " OFFSET " + offset);

//        cursor.moveToFirst();
        List<ImageDocument> contacts = new ArrayList<ImageDocument>();
        Long id;
        String name;
        Uri contentUri;
        Long size;
        String mimeType;
        String dateAdded;
        String dateTaken;
        String bucketName;

        while (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[0]));
            name = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[1]));
            contentUri = ContentUris.withAppendedId(
                    ImageContract.CONTENT_URI,
                    id
            );
            size = cursor.getLong(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[2]));
            mimeType = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[3]));
            dateAdded = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[4]));
            dateTaken = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[5]));
            bucketName = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[6]));
            System.out.println(contentUri);
            contacts.add(new ImageDocument(id, name, contentUri.toString(), size, mimeType, dateAdded, dateTaken, bucketName));
        }
        cursor.close();
        return contacts;
    }
}
