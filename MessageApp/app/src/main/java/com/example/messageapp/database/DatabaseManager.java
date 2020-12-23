package com.example.messageapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.messageapp.database.dao.ContactDao;
import com.example.messageapp.database.dao.CreditDao;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.Credit;
import com.example.messageapp.util.DateConverter;

@Database(entities = {Contact.class, Credit.class}, exportSchema = false, version = 1)
@TypeConverters({DateConverter.class})
public abstract  class DatabaseManager  extends RoomDatabase {

    private static final String PROJECT_DB = "project_db";

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, PROJECT_DB)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return databaseManager;
    }

    public abstract ContactDao getContactDao();
    public abstract CreditDao getCreditDao();
}
