package com.example.galleryversionone;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendFilesViewModel extends ViewModel {

    MutableLiveData<List<Bitmap>> thumbnailList = new MutableLiveData<List<Bitmap>>(new ArrayList<Bitmap>());
    MutableLiveData<Map<Integer, Integer>> thumbnailImageMap = new MutableLiveData<Map<Integer, Integer>>(new HashMap<Integer, Integer>());

    public void addToThumbnailList(Bitmap thumbnailBitmap){
        thumbnailList.getValue().add(thumbnailBitmap);
        thumbnailList.postValue(thumbnailList.getValue());
    }

    public LiveData<List<Bitmap>> getThumbnail(){
        return thumbnailList;
    }

    public void updateThumbnailList(int pos, Bitmap bitmap){
        thumbnailList.getValue().set(pos, bitmap);
        thumbnailList.postValue(thumbnailList.getValue());
    }

    public void updateThumbnailImageMap(Integer key, Integer value){
        thumbnailImageMap.getValue().put(key, value);
        thumbnailImageMap.postValue(thumbnailImageMap.getValue());
    }

    public LiveData<Map<Integer, Integer>> getMapper(){
        return thumbnailImageMap;
    }
}