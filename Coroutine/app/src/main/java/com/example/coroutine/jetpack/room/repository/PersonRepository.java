package com.example.coroutine.jetpack.room.repository;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.example.coroutine.jetpack.room.db.PersonBean;
import com.example.coroutine.jetpack.room.db.PersonDao;
import com.example.coroutine.jetpack.room.db.PersonDataBase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonRepository {

    private PersonDao mPersonDao;

    private ExecutorService mService;
    private Handler mHandler;


    public PersonRepository(Context context) {
        mPersonDao = PersonDataBase.getInstance(context).getPersonDao();
        mService = Executors.newSingleThreadExecutor();
    }

    public void onAdd(PersonBean... personBeans) {
        mService.execute(() -> {
            mPersonDao.insert(personBeans);
        });
    }

    public void onDelete() {
        mService.execute(() -> {
            mPersonDao.delete(mPersonDao.getFirstPerson());
        });
    }

    public void onModify() {
        mService.execute(()->{
            PersonBean personBean = mPersonDao.getFirstPerson();
            personBean.name = "更新";
            mPersonDao.update(personBean);
        });
    }

    public void onDeleteAll(){
        mService.execute(()->{
            mPersonDao.deleteAll();
        });

    }

    public LiveData<List<PersonBean>> getAllData() {
        return mPersonDao.getLiveDataAll();
    }
}
