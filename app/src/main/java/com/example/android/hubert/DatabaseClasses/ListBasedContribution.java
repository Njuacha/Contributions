package com.example.android.hubert.DatabaseClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hubert on 6/19/18.
 */

public class ListBasedContribution implements Parcelable {
    private final int memberId;
    private final String name;
    private final int amount;

    public ListBasedContribution(int memberId, String name, int amount){
        this.memberId = memberId;
        this.name = name;
        this.amount = amount;
    }

    protected ListBasedContribution(Parcel in) {
        memberId = in.readInt();
        name = in.readString();
        amount = in.readInt();
    }

    public static final Creator<ListBasedContribution> CREATOR = new Creator<ListBasedContribution>() {
        @Override
        public ListBasedContribution createFromParcel(Parcel in) {
            return new ListBasedContribution(in);
        }

        @Override
        public ListBasedContribution[] newArray(int size) {
            return new ListBasedContribution[size];
        }
    };

    public int getMemberId(){
        return memberId;
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
        dest.writeInt(memberId);
        dest.writeString(name);
        dest.writeInt(amount);
    }
}
