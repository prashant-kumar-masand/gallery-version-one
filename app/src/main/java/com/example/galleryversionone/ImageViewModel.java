package com.example.galleryversionone;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

public class ImageViewModel extends AndroidViewModel {

    LiveData<PagedList<ImageDocument>> imageItemList;
    LiveData<PositionalDataSource<ImageDocument>> liveDataSource;


    public ImageViewModel(@NonNull Application application) {
        super(application);

        ImageDataSourceFactory imageDataSourceFactory = new ImageDataSourceFactory(getApplication().getContentResolver());
        liveDataSource = imageDataSourceFactory.getLiveDataSource();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(50)
                .build();
        imageItemList = (new LivePagedListBuilder(imageDataSourceFactory, config)).build();
    }


}