package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by hubert on 8/19/18.
 */
@Dao
public interface HistoryDoa  {
   @Insert
    void insertContributionWithDate(History contribution);

   @Query("SELECT * FROM History where listId = :listId AND memberId = :memberId ")
   List<History> getHistory(int listId, int memberId);



}
