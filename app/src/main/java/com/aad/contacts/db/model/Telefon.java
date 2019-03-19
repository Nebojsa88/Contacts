package com.aad.contacts.db.model;

import android.graphics.Movie;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Telefon.TABLE_NAME_USERS)
public class Telefon {

    public static final String TABLE_NAME_USERS = "telefoni";
    public static final String FIELD_TELEFON_ID     = "id";
    public static final String FIELD_TELEFON_KUCNI   = "kucni";
    public static final String FIELD_TELEFON_MOBILNI  = "mobilni";
    public static final String FIELD_TELEFON_POSLOVNI  = "poslovni";
    public static final String FIELD_TELEFON  = "brTelefona";
    public static final String FIELD_TELEFON_CONTACT  = "contact";


    @DatabaseField(columnName = FIELD_TELEFON_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_TELEFON)
    private String brTelefona;

    @DatabaseField(columnName = FIELD_TELEFON_KUCNI)
    private String mKucni;

    @DatabaseField(columnName = FIELD_TELEFON_MOBILNI)
    private String mMobilni;

    @DatabaseField(columnName = FIELD_TELEFON_POSLOVNI)
    private String mPoslovni;

    @DatabaseField(columnName = FIELD_TELEFON_CONTACT, foreign = true, foreignAutoRefresh = true)
    private Contact contact;

    public Telefon() {

    }

    public Telefon(int mId, String brTelefona, String mKucni, String mMobilni, String mPoslovni, Contact contact) {
        this.mId = mId;
        this.brTelefona = brTelefona;
        this.mKucni = mKucni;
        this.mMobilni = mMobilni;
        this.mPoslovni = mPoslovni;
        this.contact = contact;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmKucni() {
        return mKucni;
    }

    public void setmKucni(String mKucni) {
        this.mKucni = mKucni;
    }

    public String getmMobilni() {
        return mMobilni;
    }

    public void setmMobilni(String mMobilni) {
        this.mMobilni = mMobilni;
    }

    public String getmPoslovni() {
        return mPoslovni;
    }

    public void setmPoslovni(String mPoslovni) {
        this.mPoslovni = mPoslovni;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getBrTelefona() {
        return brTelefona;
    }

    public void setBrTelefona(String brTelefona) {
        this.brTelefona = brTelefona;
    }

    @Override
    public String toString() {
        return "Telefon{" +
                "mKucni='" + mKucni + '\'' +
                ", mMobilni='" + mMobilni + '\'' +
                ", mPoslovni='" + mPoslovni + '\'' +
                '}';
    }
}
