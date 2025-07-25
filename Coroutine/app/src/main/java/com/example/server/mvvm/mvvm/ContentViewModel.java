package com.example.server.mvvm.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContentViewModel extends ViewModel {
    public final MutableLiveData<String> content = new MutableLiveData<>("");
    public ContentRepository repository;

    public ContentViewModel() {
        repository = new ContentRepository();
    }

    public void setContent(String text){
        content.setValue(text);
    }

    public void submit(){
        content.setValue(repository.requestResult(content.getValue()));
    }

    public void reset(){
        content.setValue(repository.getLastContent());
    }
}
