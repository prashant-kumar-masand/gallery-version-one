package com.example.galleryversionone;

import android.app.Application;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

public class ImageViewModel extends AndroidViewModel {

    LiveData<PagedList<ImageDocument>> imageItemList;
    LiveData<PositionalDataSource<ImageDocument>> liveDataSource;
    MutableLiveData<HashMap<String, Boolean>> selectedItemList$ = new MutableLiveData<>();
    HashMap<String, Boolean> selectedItemList = new HashMap<>();


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

//    public LiveData<HashMap<String, Boolean>> getSelectedList(String name, Boolean value) {
//        return selectedItemList$;
//    }

    public void setSelectedList(String name, Boolean value) {
        selectedItemList.put(name, value);
        selectedItemList$.postValue(selectedItemList);
    }


}