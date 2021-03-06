package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.vescera on 25/11/2016.
 * This class contains all the functions allowing to retrieve Contact items for different usecases.
 */

public class DatabaseContactHandler {

    private static final String DATABASE_TABLE = "contact";

    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;
    private DatabaseObjectHandler dbObjectHandler;
    private DatabaseMoneyHandler dbMoneyHandler;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DatabaseAdapter.DATABASE_NAME, null, DatabaseAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    /**
     * Constructor
     * @param context is used to retrieve the Activity context and store it for later use to handle Contact item in Database.
     */
    public DatabaseContactHandler(Context context) {
        mCtx = context;
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseContactHandler open() throws SQLException {
        mDbHelper = new DatabaseContactHandler.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();

        //Initiate the DBHandlers
        dbMoneyHandler = new DatabaseMoneyHandler(mCtx);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(mCtx);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * close the db
     * return type: void
     */
    public void close(){
        mDbHelper.close();
    }

    public void createContact(Contact contact) {
        ContentValues values = new ContentValues();

        values.put(FIRSTNAME, contact.get_firstName());
        values.put(LASTNAME, contact.get_lastName());
        values.put(PHONE, contact.get_phone());
        values.put(EMAIL, contact.get_email());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Contact getContact(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, FIRSTNAME, LASTNAME, PHONE, EMAIL }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        assert cursor!= null;
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));
        cursor.close();

        return contact;
    }

    public void deleteContact(Contact contact) {
        //Delete first the Money items linked to this contact.
        dbMoneyHandler.deleteAllContactMoney(contact.get_id());

        //Delete the Object items linked to this contact.
        dbObjectHandler.deleteAllContactObject(contact.get_id());

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(contact.get_id())});
    }

    public int getContactsCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getContactsNextId(){
        return getContactsCount() + 1;
    }

    public int updateContact(Contact contact) {
        ContentValues values = new ContentValues();

        values.put(FIRSTNAME, contact.get_firstName());
        values.put(LASTNAME, contact.get_lastName());
        values.put(PHONE, contact.get_phone());
        values.put(EMAIL, contact.get_email());

        int rowsAffected;
        rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + contact.get_id(), null);

        return rowsAffected;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactsList = new ArrayList<>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) do {
            contactsList.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)));
        }
        while (cursor.moveToNext());
        cursor.close();

        return contactsList;
    }
}
