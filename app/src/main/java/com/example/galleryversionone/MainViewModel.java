package com.example.galleryversionone;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    MutableLiveData<HashMap<Long, ImageDocument>> selectedItemList$ = new MutableLiveData<>();
    HashMap<Long, ImageDocument> selectedItemList = new HashMap<>();

    public void setSelectedList(Long id, ImageDocument value) {
        if (selectedItemList.containsKey(id)) {
            selectedItemList.remove(id);
        } else {
            selectedItemList.put(id, value);
        }
        selectedItemList$.setValue(selectedItemList);
    }

    public MutableLiveData<HashMap<Long, ImageDocument>> getSelectedItemList() {
        return selectedItemList$;
    }

}
