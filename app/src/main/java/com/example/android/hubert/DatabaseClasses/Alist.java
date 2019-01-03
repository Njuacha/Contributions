package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by hubert on 6/15/18.
 */
@Entity( foreignKeys = {@ForeignKey(entity = Group.class, parentColumns = "groupId", childColumns = "groupId", onDelete = CASCADE)}
         ,indices = {@Index(value = "groupId")})
public class Alist implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private  int listId;
    private String name;
    private Date date;
    private int groupId;

    public int getGroupId() {
        return groupId;
    }

    public Alist(int listId, String name, Date date, int groupId) {
        this.listId = listId;
        this.name = name;
        this.date = date;
        this.groupId = groupId;
    }

    Alist(Parcel in) {
        listId = in.readInt();
        name = in.readString();
        date = DateConverter.toDate(in.readLong());
    }

    public static final Creator<Alist> CREATOR = new Creator<Alist>() {
        @Override
        public Alist createFromParcel(Parcel in) {
            return new Alist(in);
        }

        @Override
        public Alist[] newArray(int size) {
            return new Alist[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Ignore
    public Alist(String name, Date date, int groupId) {
        this.name = name;
        this.date = date;
        this.groupId = groupId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(listId);
        dest.writeString(name);
        dest.writeLong(DateConverter.toTimestamp(date));
    }
}


