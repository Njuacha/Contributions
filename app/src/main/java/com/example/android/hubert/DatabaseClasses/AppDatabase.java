package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by hubert on 6/15/18.
 */
@Database(entities = {Alist.class, Member.class, AMemberInAList.class, History.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract  class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Contributions";
    private static AppDatabase databaseInstance ;
    private static final Object LOCK = new Object();

    public static AppDatabase getDatabaseInstance(Context context){
        if ( databaseInstance == null){
           synchronized (LOCK){
               databaseInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,
                       AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build();
           }
        }

        return databaseInstance;
    }

    public abstract AListDao a_list_dao();
    public abstract A_member_in_a_list_Dao a_member_in_a_list_dao();
    public abstract MemberDao member_dao();
    public abstract HistoryDoa historyDoa();


}
