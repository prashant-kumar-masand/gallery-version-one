package com.example.galleryversionone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
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
                        ImageContract.PROJECTION_ALL[2]+" DESC LIMIT " + limit + " OFFSET " + offset);

//        cursor.moveToFirst();
        List<ImageDocument> contacts= new ArrayList<ImageDocument>();
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[0]));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.PROJECTION_ALL[1]));

            Uri contentUri = ContentUris.withAppendedId(
                    ImageContract.CONTENT_URI,
                    id
            );
            System.out.println(contentUri);
            contacts.add(new ImageDocument(id, name, contentUri));
        }
        cursor.close();
        return contacts;
    }
}
