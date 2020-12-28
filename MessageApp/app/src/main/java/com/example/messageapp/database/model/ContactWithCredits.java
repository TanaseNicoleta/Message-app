package com.example.messageapp.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ContactWithCredits implements Parcelable {

    @Embedded
    public Contact contact;
    @Relation( parentColumn = "id", entityColumn = "contactId")
    public List<Credit> credits;

    public ContactWithCredits(Contact contact, List<Credit> credits) {
        this.contact = contact;
        this.credits = credits;
    }

    @Ignore
    protected ContactWithCredits(Parcel in) {
        contact = in.readParcelable(Contact.class.getClassLoader());
        credits = in.createTypedArrayList(Credit.CREATOR);
    }

    @Ignore
    public static final Creator<ContactWithCredits> CREATOR = new Creator<ContactWithCredits>() {
        @Override
        public ContactWithCredits createFromParcel(Parcel in) {
            return new ContactWithCredits(in);
        }

        @Override
        public ContactWithCredits[] newArray(int size) {
            return new ContactWithCredits[size];
        }
    };

    @Override
    public String toString() {
        return "ContactsWithCredits{" +
                "contact=" + contact +
                ", credits=" + credits +
                '}';
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(contact, flags);
        dest.writeTypedList(credits);
    }
}
