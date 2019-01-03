package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GroupDoa {
    @Insert
    void insertGroup(Group group);

    @Update
    void updateGroup(Group group);

    @Query("SELECT * FROM 'Group'")
    List<Group> loadAllGroups();

    @Delete
    void deleteGroup(Group group);

    @Query("SELECT COUNT('groupId') FROM 'Group'")
    int getNumberOfGroups();



}
