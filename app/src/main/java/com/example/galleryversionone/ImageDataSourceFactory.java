package com.example.galleryversionone;

import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

public class ImageDataSourceFactory extends DataSource.Factory {

    ContentResolver contentResolver;

    private MutableLiveData<PositionalDataSource<ImageDocument>> liveDataSource = new MutableLiveData<>();

    public ImageDataSourceFactory(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @NonNull
    @Override
    public DataSource create() {
        ImageDataSource imageDataSource = new ImageDataSource(contentResolver);
        liveDataSource.postValue(imageDataSource);
        return imageDataSource;
    }

    public MutableLiveData<PositionalDataSource<ImageDocument>> getLiveDataSource(){
        return liveDataSource;
    }
}
