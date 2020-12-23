package com.example.messageapp.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "credite",foreignKeys = @ForeignKey(entity = Contact.class,
        parentColumns = "id",
        childColumns = "contactId",
        onDelete = CASCADE, onUpdate = CASCADE))
public class Credit implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="denumireCredit")
    private String denumireCredit;

    @ColumnInfo(name="sumaImprumutata")
    private float sumaImprumutata;

    @ColumnInfo(name="dobanda")
    private float dobanda;

    @ColumnInfo(name="durataAni")
    private int durataAni;

    @ColumnInfo(name="contactId")
    private long contactId;

    public Credit(long id, String denumireCredit, float sumaImprumutata, float dobanda, int durataAni, long contactId) {
        this.id = id;
        this.denumireCredit = denumireCredit;
        this.sumaImprumutata = sumaImprumutata;
        this.dobanda = dobanda;
        this.durataAni = durataAni;
        this.contactId=contactId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    @Ignore
    public Credit(String denumireCredit, float sumaImprumutata, float dobanda, int durataAni) {
        this.denumireCredit = denumireCredit;
        this.sumaImprumutata = sumaImprumutata;
        this.dobanda = dobanda;
        this.durataAni = durataAni;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDenumireCredit() {
        return denumireCredit;
    }

    public void setDenumireCredit(String denumireCredit) {
        this.denumireCredit = denumireCredit;
    }

    public float getSumaImprumutata() {
        return sumaImprumutata;
    }

    public void setSumaImprumutata(float sumaImprumutata) {
        this.sumaImprumutata = sumaImprumutata;
    }

    public float getDobanda() {
        return dobanda;
    }

    public void setDobanda(float dobanda) {
        this.dobanda = dobanda;
    }

    public int getDurataAni() {
        return durataAni;
    }

    public void setDurataAni(int durataAni) {
        this.durataAni = durataAni;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "denumireCredit='" + denumireCredit + '\'' +
                ", sumaImprumutata=" + sumaImprumutata +
                ", dobanda=" + dobanda +
                ", durataAni=" + durataAni +
                '}';
    }
}
