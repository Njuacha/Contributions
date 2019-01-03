package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Group {
    @PrimaryKey(autoGenerate = true)
    int groupId;
    String groupName;

    public Group(int groupId,String groupName){
       this.groupId = groupId;
       this.groupName = groupName;
    }

    @Ignore
    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
