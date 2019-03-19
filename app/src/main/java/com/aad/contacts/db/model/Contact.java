package com.aad.contacts.db.model;

import android.graphics.Movie;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Contact.TABLE_NAME_USERS)
public class Contact {

    public static final String TABLE_NAME_USERS = "contact";
    public static final String FIELD_KONTAKT_ID     = "id";
    public static final String TABLE_KONTAKT_NAME = "name";
    public static final String TABLE_KONTAKT_LAST_NAME= "last_name";
    public static final String TABLE_KONTAKT_ADDRES= "addres";
    public static final String TABLE_KONTAKT_TELEFON = "telefon";

    @DatabaseField(columnName = FIELD_KONTAKT_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_KONTAKT_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_KONTAKT_LAST_NAME)
    private String mLastName;

    @DatabaseField(columnName = TABLE_KONTAKT_ADDRES)
    private String mAddres;

    @ForeignCollectionField(columnName = Contact.TABLE_KONTAKT_TELEFON, eager = true)
    private ForeignCollection<Telefon> telefoni;

    public Contact() {
    }

    public Contact(int mId, String mName, String mLastName, String mAddres, ForeignCollection<Telefon> telefoni) {
        this.mId = mId;
        this.mName = mName;
        this.mLastName = mLastName;
        this.mAddres = mAddres;
        this.telefoni = telefoni;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmAddres() {
        return mAddres;
    }

    public void setmAddres(String mAddres) {
        this.mAddres = mAddres;
    }

    public ForeignCollection<Telefon> getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(ForeignCollection<Telefon> telefoni) {
        this.telefoni = telefoni;
    }

    @Override
    public String toString() {
        return mName + " " +  mLastName;
    }
}
