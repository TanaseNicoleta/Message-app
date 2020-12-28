package com.example.messageapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.messageapp.database.model.Credit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public interface CreditDao {

    @Query("select * from credite")
    List<Credit> getAllCredits();

    @Query("select * from credite where contactId=:contactId")
    List<Credit> getAllCreditsForOneContact(long contactId);

//    @Query("select denumireCredit , count(denumireCredit) nr from credite group by denumireCredit")
//    Map<String,Integer> getCreditsAndNumber();

    @Insert
    long insert(Credit credit);

    @Update
    int update(Credit credit);

    @Delete
    int delete(Credit credit);
}
