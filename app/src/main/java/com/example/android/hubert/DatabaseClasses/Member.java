package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hubert on 6/15/18.
 */
@Entity
public class Member implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int memberId;
    private String name;
    private int groupId;

    public int getGroupId() {
        return groupId;
    }

    public Member(int memberId, String name, int groupId) {
        this.memberId = memberId;
        this.name = name;
        this.groupId = groupId;
    }

    @Ignore
    public Member(String name, int groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    Member(Parcel in) {
        memberId = in.readInt();
        name = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public String toString(){
        return name;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof Member)){
            return false;
        }
        Member obj2 = (Member)obj;
        return (obj2.getMemberId())==(getMemberId());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(memberId);
        dest.writeString(name);
    }
}
