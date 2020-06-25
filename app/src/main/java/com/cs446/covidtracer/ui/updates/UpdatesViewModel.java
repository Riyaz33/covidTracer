package com.cs446.covidtracer.ui.updates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpdatesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UpdatesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is updates fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}