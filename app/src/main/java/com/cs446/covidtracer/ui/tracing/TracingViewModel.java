package com.cs446.covidtracer.ui.tracing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TracingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TracingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tracing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}