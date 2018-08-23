package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by hubert on 6/15/18.
 */
@Entity
public class Alist implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private  int id;
    public String name;
    Date date;

    public Alist(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    protected Alist(Parcel in) {
        id = in.readInt();
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
    public Alist(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(DateConverter.toTimestamp(date));
    }
}


