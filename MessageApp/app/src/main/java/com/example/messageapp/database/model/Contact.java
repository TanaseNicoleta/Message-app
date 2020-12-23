package com.example.messageapp.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.messageapp.util.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "contacte")
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name="nume")
    private String nume;

    @ColumnInfo(name="prenume")
    private String prenume;

    @ColumnInfo(name="telefon")
    private String telefon;

    @ColumnInfo(name="dataNasterii")
    private Date dataNasterii;

    @ColumnInfo(name = "gen")
    private String gen;

    @ColumnInfo(name="sumaCont")
    private float sumaCont;

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public Contact(long id, String nume, String prenume, String telefon, Date dataNasterii, String gen, float sumaCont) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.dataNasterii = dataNasterii;
        this.gen=gen;
        this.sumaCont = sumaCont;
    }

    @Ignore
    public Contact(String nume, String prenume, String telefon, Date dataNasterii,String gen, float sumaCont) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
        this.dataNasterii = dataNasterii;
        this.gen=gen;
        this.sumaCont = sumaCont;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setDataNasterii(Date dataNasterii) {
        this.dataNasterii = dataNasterii;
    }

    public void setSumaCont(float sumaCont) {
        this.sumaCont = sumaCont;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public Date getDataNasterii() {
        return dataNasterii;
    }

    public float getSumaCont() {
        return sumaCont;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", dataNasterii=" + dataNasterii +
                ", gen='" + gen + '\'' +
                ", sumaCont=" + sumaCont +
                '}';
    }
}
