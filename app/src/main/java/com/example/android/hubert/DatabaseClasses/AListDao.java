package com.example.android.hubert.DatabaseClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by hubert on 6/15/18.
 */
@Dao
public interface AListDao {
    @Insert
    void insertAList(Alist aList);

    @Update
    void updateAList(Alist aList);

    @Query("SELECT * FROM Alist ")
    LiveData<List<Alist>> loadAllListNames();

    @Query("DELETE FROM Alist where listId = :listId")
    void deleteAList(int listId);
}
