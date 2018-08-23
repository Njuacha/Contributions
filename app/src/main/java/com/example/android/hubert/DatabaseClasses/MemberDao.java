package com.example.android.hubert.DatabaseClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by hubert on 6/15/18.
 */
@Dao
public interface MemberDao {
    @Query("SELECT * FROM Member")
    LiveData<List<Member>>  loadAllMembers();

    @Insert
    void insertMember(Member member);

    @Update
    void updateMember(Member member);

    @Delete
    void deleteMember(Member member);





}
