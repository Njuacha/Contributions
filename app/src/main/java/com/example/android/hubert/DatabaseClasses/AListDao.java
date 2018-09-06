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
    void insert_a_list(Alist aList);

    @Update
    void update_a_list(Alist aList);

    @Query("SELECT * FROM Alist ")
    LiveData<List<Alist>> load_all_list_names();

    @Query("SELECT * FROM Alist WHERE listId = :listId")
    Alist load_a_list(int listId);

    @Query("DELETE FROM Alist where listId = :listId")
    void delete_a_list(int listId);
}
