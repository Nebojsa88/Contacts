package com.aad.contacts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;

import com.aad.contacts.db.model.Contact;
import com.aad.contacts.db.model.Telefon;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class ORMLightHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "contacts.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<Contact, Integer> mContactDao = null;
    private Dao<Telefon, Integer> mTelefonDao = null;

    public ORMLightHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Telefon.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Telefon.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (mContactDao == null) {
            mContactDao = getDao(Contact.class);
        }

        return mContactDao;
    }

    public Dao<Telefon, Integer> getTelefonDao() throws SQLException {
        if (mTelefonDao == null) {
            mTelefonDao = getDao(Telefon.class);
        }

        return mTelefonDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mContactDao = null;
        mTelefonDao= null;

        super.close();
    }
}
