package com.example.coroutine.jetpack.room.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import retrofit2.http.DELETE;

/**
 * 🌟要点：
 * 🌈FROM PersonBean 是@Entity的实体类名，或可通过 @Entity(tableName="") 修改名字
 */
@Dao
public interface PersonDao {

    @Insert
    void insert(PersonBean... personBeans);

    @Delete
    void delete(PersonBean... personBeans);

    @Query("DELETE FROM PersonBean")
    void deleteAll();

    @Update
    void update(PersonBean... personBeans);

    @Query("SELECT * FROM PersonBean")
    List<PersonBean> getAllPerson();

    /**
     * 🌟相当于 Room 在维护一个LiveData，如果有变动就会通知观察者
     *   不需要再手动调用刷新，减轻了很多工作
     * @return
     */
    @Query("SELECT * FROM PersonBean")
    LiveData<List<PersonBean>> getLiveDataAll();

    /**
     * 拿到第一个对象
     * @return
     */
    @Query("SELECT * FROM PersonBean ORDER BY id ASC LIMIT 1")
    PersonBean getFirstPerson();

}
