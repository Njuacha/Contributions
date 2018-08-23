package com.example.android.hubert.DatabaseClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Created by hubert on 6/18/18.
 */
@Entity(primaryKeys = {"memberId","listId"},
        foreignKeys = {@ForeignKey(entity = Member.class, parentColumns = "memberId",childColumns = "memberId",onDelete=CASCADE),
                       @ForeignKey(entity = Alist.class, parentColumns = "id",childColumns = "listId",onDelete=CASCADE)
        },
        indices = {@Index(value = { "listId"}), @Index(value = {"memberId"})}
        )

public class AMemberInAList {

    private int memberId;
    private int listId;
    private int amount;

    public AMemberInAList(int memberId, int listId, int amount){
        this.memberId = memberId;
        this.listId = listId;
        this.amount = amount;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
