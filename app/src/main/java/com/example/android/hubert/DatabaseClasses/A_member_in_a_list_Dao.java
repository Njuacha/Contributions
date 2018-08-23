package com.example.android.hubert.DatabaseClasses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by hubert on 6/18/18.
 */
@Dao
public interface A_member_in_a_list_Dao {

    @Insert
    void insert_a_member_in_a_list(AMemberInAList AMemberInA_list);

    @Update
    void update_a_member_in_a_list(AMemberInAList AMemberInA_list);

    @Query("SELECT amount FROM AMemberInAList WHERE listId = :listId")
    int[] loadAllAmountInlist(int listId);

    @Query("SELECT AMemberInAList.memberId, Member.name, AMemberInAList.amount FROM AMemberInAList INNER JOIN Member On AMemberInAList.memberId = Member.memberId WHERE AMemberInAList.listId = :listId")
    LiveData<List<Contribution>> loadContributionsInList(int listId);

    @Query("SELECT AMemberInAList.memberId, Member.name FROM AMemberInAList INNER JOIN Member On AMemberInAList.memberId = Member.memberId WHERE AMemberInAList.listId = :listId")
    List<Member> loadMembersInList(int listId);

    @Query("DELETE FROM AMemberInAList WHERE memberId = :memberId")
    void deleteAContribution(int memberId);

}
