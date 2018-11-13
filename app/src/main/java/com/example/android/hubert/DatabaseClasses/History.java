package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hubert on 8/19/18.
 */
@Entity
public class History implements Parcelable{


    @PrimaryKey(autoGenerate = true)
    private int id;
    private int listId;
    private int memberId;
    private String date;
    private int amount;

    public History(int id, int listId, int memberId, String date, int amount) {
        this.id = id;
        this.listId = listId;
        this.memberId = memberId;
        this.date = date;
        this.amount = amount;
    }

    @Ignore
    public History(int listId,int memberId, String date,int amount){
        this.listId = listId;
        this.memberId = memberId;
        this.date = date;
        this.amount = amount;
    }

    protected History(Parcel in) {
        id = in.readInt();
        listId = in.readInt();
        memberId = in.readInt();
        date = in.readString();
        amount = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(listId);
        dest.writeInt(memberId);
        dest.writeString(date);
        dest.writeInt(amount);
    }
}
