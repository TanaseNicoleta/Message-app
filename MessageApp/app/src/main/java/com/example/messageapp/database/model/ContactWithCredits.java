package com.example.messageapp.database.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ContactWithCredits implements Serializable {

    @Embedded
    public Contact contact;
    @Relation( parentColumn = "id", entityColumn = "contactId")
    public List<Credit> credits;

    public ContactWithCredits(Contact contact, List<Credit> credits) {
        this.contact = contact;
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "ContactsWithCredits{" +
                "contact=" + contact +
                ", credits=" + credits +
                '}';
    }
}
