package com.example.messageapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;

import java.util.List;
@Dao
public interface ContactDao {

    @Query("select * from contacte")
    List<Contact> getAllContacts();

    @Query("select * from contacte where id=:id")
    Contact getInfoAboutOneContact(long id);

    @Update
    int update(Contact contact);

    @Delete
    int delete(Contact contact);

    @Transaction
    @Insert
    long insert(Contact contact);

    @Insert
    void insertCredits(List<Credit>credits);
}
