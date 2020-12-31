package com.example.messageapp.database.service;

import android.content.Context;

import com.example.messageapp.asyncTask.AsyncTaskRunner;
import com.example.messageapp.asyncTask.Callback;
import com.example.messageapp.database.DatabaseManager;
import com.example.messageapp.database.dao.ContactDao;
import com.example.messageapp.database.dao.CreditDao;
import com.example.messageapp.database.model.Contact;
import com.example.messageapp.database.model.ContactWithCredits;
import com.example.messageapp.database.model.Credit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CreditService {

    private final CreditDao creditDao;
    private final AsyncTaskRunner taskRunner;

    public CreditService(Context context) {
        creditDao = DatabaseManager.getInstance(context).getCreditDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAllCredits(Callback<List<Credit>> callback) {
        Callable<List<Credit>> callable = new Callable<List<Credit>>() {
            @Override
            public List<Credit> call() {
                return creditDao.getAllCredits();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

//    public void getCreditsAndNumberOfCredits(Callback<Map<String,Integer>> callback) {
//        Callable<Map<String,Integer>> callable = new Callable<Map<String,Integer>>() {
//            @Override
//            public Map<String,Integer>call() {
//                return creditDao.getCreditsAndNumber();
//            }
//        };
//        taskRunner.executeAsync(callable, callback);
//    }



    public void getAllCreditsForOneContact(Callback<List<Credit>> callback, final long contactId) {
        Callable<List<Credit>> callable = new Callable<List<Credit>>() {
            @Override
            public List<Credit> call() {
                return creditDao.getAllCreditsForOneContact(contactId);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Credit> callback, final Credit credit, final Contact contact) {
        Callable<Credit> callable = new Callable<Credit>() {
            @Override
            public Credit call() {
                if (credit == null || contact==null) {
                    return null;
                }
                credit.setContactId(contact.getId());
                long id = creditDao.insert(credit);
                if (id == -1) {
                    return null;
                }
                credit.setId(id);
                return credit;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }


    public void update(Callback<Credit> callback, final Credit credit) {
        Callable<Credit> callable = new Callable<Credit>() {
            @Override
            public Credit call() {
                if (credit == null) {
                    return null;
                }
                int count = creditDao.update(credit);
                if (count < 1) {
                    return null;
                }
                return credit;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(final Callback<Integer> callback, final Credit credit) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (credit == null) {
                    return -1;
                }
                return creditDao.delete(credit);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
