package com.example.android.hubert.DatabaseClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hubert on 6/19/18.
 */

public class Contribution implements Parcelable {
    private final int memberId;
    private final String name;
    private final int amount;

    public Contribution(int memberId, String name, int amount){
        this.memberId = memberId;
        this.name = name;
        this.amount = amount;
    }

    protected Contribution(Parcel in) {
        memberId = in.readInt();
        name = in.readString();
        amount = in.readInt();
    }

    public static final Creator<Contribution> CREATOR = new Creator<Contribution>() {
        @Override
        public Contribution createFromParcel(Parcel in) {
            return new Contribution(in);
        }

        @Override
        public Contribution[] newArray(int size) {
            return new Contribution[size];
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
