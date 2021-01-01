package com.example.messageapp.database.service;

import android.content.Context;
import android.os.AsyncTask;

import com.example.messageapp.asyncTask.AsyncTaskRunner;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.DatabaseManager;
import com.example.messageapp.database.dao.ContactDao;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.ContactWithCredits;
import com.example.messageapp.database.model.Credit;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

public class ContactService {

    private final ContactDao contactDao;
    private final AsyncTaskRunner taskRunner;

    public ContactService(Context context) {
        contactDao = DatabaseManager.getInstance(context).getContactDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void insert (Callback<ContactWithCredits>callback, final ContactWithCredits contactWithCredits){
        Callable<ContactWithCredits> callable = new Callable<ContactWithCredits>() {
            @Override
            public ContactWithCredits call() {
                if(contactWithCredits==null){
                    return null;
                }
                long idContact=contactDao.insert(contactWithCredits.contact);
                if(idContact==-1){
                    return null;
                }
                for (Credit credit : contactWithCredits.credits) {
                  credit.setContactId(idContact);
                }
                contactDao.insertCredits(contactWithCredits.credits);
                return contactWithCredits;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }


    public void getAllContacts(Callback<List<Contact>> callback) {
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return contactDao.getAllContacts();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getSarbatoriti(Callback<List<Contact>> callback) {
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return contactDao.getSarbatoriti();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getContacteImprumutateCuPeste6000lei(Callback<List<Contact>> callback) {
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return contactDao.getContacteImprumutateCuPeste6000lei();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
    public void getContacteImprumutateMaiPutinDe3000lei(Callback<List<Contact>> callback) {
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return contactDao.getContacteImprumutateMaiPutinDe3000lei();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
    public void getContacteImprumutateCuSumaTotalaIntre3000si6000(Callback<List<Contact>> callback) {
        Callable<List<Contact>> callable = new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() {
                return contactDao.getContacteImprumutateCuSumaTotalaIntre3000si6000();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getInfoAboutOneContact(Callback<Contact> callback, final Contact contact){
        Callable<Contact> callable = new Callable<Contact>() {
            @Override
            public Contact call() {
                if (contact == null) {
                    return null;
                }
                contactDao.getInfoAboutOneContact(contact.getId());
                return contact;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Contact> callback, final Contact contact) {
        Callable<Contact> callable = new Callable<Contact>() {
            @Override
            public Contact call() {
                if (contact == null) {
                    return null;
                }
                long id = contactDao.insert(contact);
                if (id == -1) {
                    return null;
                }
                contact.setId(id);
                return contact;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Contact> callback, final Contact contact) {
        Callable<Contact> callable = new Callable<Contact>() {
            @Override
            public Contact call() {
                if (contact == null) {
                    return null;
                }
                int count = contactDao.update(contact);
                if (count < 1) {
                    return null;
                }
                return contact;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Contact contact) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (contact == null) {
                    return -1;
                }
                return contactDao.delete(contact);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
