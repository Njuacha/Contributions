package com.example.android.hubert.DatabaseClasses;

/**
 * Created by hubert on 6/19/18.
 */

public class Contribution {
    private final int memberId;
    private final String name;
    private final int amount;

    public Contribution(int memberId, String name, int amount){
        this.memberId = memberId;
        this.name = name;
        this.amount = amount;
    }

    public int getMemberId(){
        return memberId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
