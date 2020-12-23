package com.example.messageapp.util;

import java.io.Serializable;

public class Bank implements Serializable {

    private String denumireBanca;
    private String adresa;
    private float comision;

    public Bank(String denumireBanca, String adresa, float comision) {
        this.denumireBanca = denumireBanca;
        this.adresa = adresa;
        this.comision = comision;
    }

    public String getDenumireBanca() {
        return denumireBanca;
    }

    public void setDenumireBanca(String denumireBanca) {
        this.denumireBanca = denumireBanca;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public float getComision() {
        return comision;
    }

    public void setComision(float comision) {
        this.comision = comision;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "denumireBanca='" + denumireBanca + '\'' +
                ", adresa='" + adresa + '\'' +
                ", comision=" + comision +
                '}';
    }
}
