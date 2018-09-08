package com.example.android.hubert.DatabaseClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by hubert on 8/19/18.
 */
@Dao
public interface HistoryDoa  {
   @Insert
    void insertContributionWithDate(History contribution);

   @Query("SELECT * FROM History where listId = :listId AND memberId = :memberId ")
   LiveData<List<History>> getHistory(int listId, int memberId);

   @Query("DELETE FROM History where listId = :listId AND memberId = :memberId")
   void delete(int listId, int memberId);

   @Update
   void updateHistory(History history);

   @Delete
   void delete(History history);



}
