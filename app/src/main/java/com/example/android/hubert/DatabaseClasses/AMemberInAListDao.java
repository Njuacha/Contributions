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
public interface AMemberInAListDao {

    @Insert
    void insert_a_member_in_a_list(AMemberInAList AMemberInA_list);

    @Update
    void update_a_member_in_a_list(AMemberInAList AMemberInA_list);

    @Query("SELECT amount FROM AMemberInAList WHERE listId = :listId")
    int[] loadAllAmountInlist(int listId);

    @Query("SELECT amount FROM AMemberInAList WHERE memberId = :memberId")
    int[] loadAllAmtOfMember(int memberId);

    @Query("SELECT AMemberInAList.memberId, Member.name, AMemberInAList.amount FROM AMemberInAList INNER JOIN Member On AMemberInAList.memberId = Member.memberId WHERE AMemberInAList.listId = :listId")
    LiveData<List<ListBasedContribution>> loadContributionsInList(int listId);

    @Query("SELECT AMemberInAList.listId, Alist.name, AMemberInAList.amount FROM AMemberInAList INNER JOIN Alist On AMemberInAList.listId = Alist.listId WHERE AMemberInAList.memberId = :memberId")
    LiveData<List<MemberBasedContribution>> loadContributionOfMember(int memberId);

    @Query("SELECT AMemberInAList.memberId, Member.name FROM AMemberInAList INNER JOIN Member On AMemberInAList.memberId = Member.memberId WHERE AMemberInAList.listId = :listId")
    List<Member> loadMembersInList(int listId);

    @Query("DELETE FROM AMemberInAList WHERE memberId = :memberId AND listId = :listId")
    void deleteAContribution(int memberId, int listId);

}
