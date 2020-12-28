package com.example.messageapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.messageapp.util.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    public static final String USERS = "users";
    private static FirebaseService firebaseService;
    private DatabaseReference database;

    private FirebaseService() {
        database = FirebaseDatabase.getInstance().getReference(USERS);

    }

    public static FirebaseService getInstance() {
        if(firebaseService == null) {
            synchronized(FirebaseService.class) {
                if(firebaseService == null) {
                    firebaseService = new FirebaseService();
                }
            }
        }

        return firebaseService;
    }

    public void upsert(User user) {
        if (user == null) {
            return;
        }
        if(user.getId() == null || user.getId().trim().isEmpty()) {
            String id = database.push().getKey();
            user.setId(id);
        }

        database.child(user.getId()).setValue(user);
    }


    public void delete(User user) {
        if ( user == null || user.getId() == null || user.getId().trim().isEmpty()) {
            return;
        }

        database.child(user.getId()).removeValue();
    }

    public void attachDataChangeEventListener() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()) {
                    User user =data.getValue(User.class);
                    if(user != null) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("FirebaseService", "Data is not available");
            }
        });
    }

}
