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

    @Query("select * from contacte where substr(dataNasterii,1,5) = strftime('%d/%m', 'now')")
    List<Contact> getSarbatoriti();

    //Contactele care s-au imprumutat cu peste 6000 lei in toate creditele
    @Query("select * from contacte c join credite cr on c.id=cr.contactId group by c.id having sum(sumaImprumutata)>6000")
    List<Contact> getContacteImprumutateCuPeste6000lei();

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
