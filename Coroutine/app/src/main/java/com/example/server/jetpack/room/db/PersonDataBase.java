package com.example.server.jetpack.room.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * 🍯exportSchema = true 记录升级的日志，需要在Gradle里设置 room.schema 路径
 */
@Database(entities = PersonBean.class,version = 3,exportSchema = true)
public abstract class PersonDataBase extends RoomDatabase {

    private static PersonDataBase mInstance;

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE PersonBean ADD COLUMN sex INTEGER NOT NULL DEFAULT 1");
        }
    };


    /**
     * 修改字段类型，从Int 修改为 String
     * 🍠创建临时表-》复制-〉删除旧表-》重命名新表
     */
    static final Migration MIGRATION_2_3 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS temp_person (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, age INTEGER NOT NULL, sex TEXT DEFAULT 'M')");
            db.execSQL("INSERT INTO temp_person(name,age,sex) SELECT name,age,sex FROM student");
            db.execSQL("DROP TABLE student");
            db.execSQL("ALTER TABLE temp_student RENAME TO student");
        }
    };

    public static synchronized PersonDataBase getInstance(Context context){
        if( mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDataBase.class,
                    "person_db.db").
                    /**
                     * 🌟异常处理，true删除所有数据库，重新创建新表；false只删除Room管理的数据库
                     */
                    fallbackToDestructiveMigration(true).
                    /**
                     * 🌟版本升级处理
                     */
                    addMigrations(MIGRATION_1_2,MIGRATION_2_3).
                    /**
                     * 🌟启动时添加默认表
                     */
                    createFromAsset("person.db").
                    build();
        }
        return mInstance;
    }

    /**
     * 🌞Room自动实现
     * @return
     */
    public abstract PersonDao getPersonDao();
}
