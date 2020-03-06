package com.example.smartlock;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Map;

public class MyViewModel extends ViewModel {
    public MutableLiveData<Map<String,String>> liveData = null;

    public MutableLiveData<Map<String, String>> getLiveData() {
        if(liveData==null){
            this.liveData=new MutableLiveData<>();
        }
        return liveData;
    }
}
