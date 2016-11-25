package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Classes.Object;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by a.vescera on 25/11/2016.
 */

public class DatabaseObjectHandler {

    private static final String DATABASE_TABLE = "object";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String NUMBER = "number";
    private static final String DETAILS = "details";
    private static final String DATE = "date";
    private static final String CATEGORY_FK_ID = "category";
    private static final String TYPE_FK_ID = "type";
    private static final String CONTACT_FK_ID = "contact";
    private static final String REMINDER_FK_ID = "reminder";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;
    private final DateFormat dateFormat;

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
     * @param context
     */
    public DatabaseObjectHandler(Context context) {
        mCtx = context;
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseObjectHandler open() throws SQLException {
        mDbHelper = new DatabaseObjectHandler.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db
     * return type: void
     */
    public void close(){
        mDbHelper.close();
    }

    public void createObject(Object object) {
        ContentValues values = new ContentValues();

        values.put(TITLE, object.get_title());
        values.put(NUMBER, object.get_number());
        values.put(DETAILS, object.get_details());
        values.put(DATE, object.get_date().toString());
        values.put(CATEGORY_FK_ID, object.get_categoryFkId());
        values.put(TYPE_FK_ID, object.get_typeFkId());
        values.put(CONTACT_FK_ID, object.get_contactFkId());
        values.put(REMINDER_FK_ID, object.get_reminderFkId());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Object getObject(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, NUMBER, DETAILS, DATE, CATEGORY_FK_ID, TYPE_FK_ID, CONTACT_FK_ID, REMINDER_FK_ID }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        try {
            Object object = new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), dateFormat.parse(cursor.getString(4)),
                    Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));

            return object;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteObject(Object object, Context context) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(object.get_id())});
    }

    public int getObjectsCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int updateObject(Object object) {
        ContentValues values = new ContentValues();

        values.put(TITLE, object.get_title());
        values.put(NUMBER, object.get_number());
        values.put(DETAILS, object.get_details());
        values.put(DATE, object.get_date().toString());
        values.put(CATEGORY_FK_ID, object.get_categoryFkId());
        values.put(TYPE_FK_ID, object.get_typeFkId());
        values.put(CONTACT_FK_ID, object.get_contactFkId());
        values.put(REMINDER_FK_ID, object.get_reminderFkId());

        int rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + object.get_id(), null);

        return rowsAffected;
    }

    public List<Object> getAllObjects() {
        List<Object> objectList = new ArrayList<Object>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date;
                    date = dateFormat.parse(cursor.getString(4));

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), date,
                            Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return objectList;
    }
}
