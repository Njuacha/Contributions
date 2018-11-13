package com.example.android.hubert.DatabaseClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hubert on 9/6/18.
 */

public class MemberBasedContribution implements Parcelable{

    private final int listId;
    private final String name;
    private final int amount;

    public MemberBasedContribution(int listId, String name, int amount) {
        this.listId = listId;
        this.name = name;
        this.amount = amount;
    }

    protected MemberBasedContribution(Parcel in) {
        listId = in.readInt();
        name = in.readString();
        amount = in.readInt();
    }

    public static final Creator<MemberBasedContribution> CREATOR = new Creator<MemberBasedContribution>() {
        @Override
        public MemberBasedContribution createFromParcel(Parcel in) {
            return new MemberBasedContribution(in);
        }

        @Override
        public MemberBasedContribution[] newArray(int size) {
            return new MemberBasedContribution[size];
        }
    };

    public int getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(listId);
        dest.writeString(name);
        dest.writeInt(amount);
    }
}
