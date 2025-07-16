package com.example.coroutine.jetpack.room.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coroutine.jetpack.room.db.PersonBean;

import java.util.List;

/**
 * 🌟使用ViewModel的核心就是可以通过LiveData通知数据库的变化
 *   从而控制页面
 */
public class PersonViewModel extends AndroidViewModel {

    private PersonRepository repository;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
    }

    public void add(PersonBean... personBeans){
        repository.onAdd(personBeans);
    }

    public void delete(){
        repository.onDelete();
    }

    public void modify(){
        repository.onModify();
    }

    public void clearAll(){
        repository.onDeleteAll();
    }

    public LiveData<List<PersonBean>> getAllData(){
        return repository.getAllData();
    }
}
