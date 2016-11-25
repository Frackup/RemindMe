package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Money;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by a.vescera on 23/11/2016.
 */

public class DatabaseMoneyHandler {

    private static final String DATABASE_TABLE = "money";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TYPE_FK = "type";
    private static final String DETAILS = "details";
    private static final String AMOUNT = "amount";
    private static final String DATE = "date";
    private static final String CONTACT_FK = "contact";
    private static final String REMINDER_FK = "reminder";

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
    public DatabaseMoneyHandler(Context context) {
        mCtx = context;
        dateFormat = android.text.format.DateFormat.getDateFormat(context);
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseMoneyHandler open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
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

    public void createMoney(Money money) {
        ContentValues values = new ContentValues();

        values.put(TITLE, money.get_title());
        values.put(TYPE_FK, money.get_typeFkId());
        values.put(DETAILS, money.get_details());
        values.put(AMOUNT, money.get_amount());
        values.put(DATE, money.get_date().toString());
        values.put(CONTACT_FK, money.get_contactFkId());
        values.put(REMINDER_FK, money.get_reminderFkId());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Money getMoney(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, AMOUNT, DETAILS, TYPE_FK, DATE, CONTACT_FK, TYPE_FK }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Money money = new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)));

        return money;
    }

    public void deleteMoney(Money money, Context context) {
        // Remove the events and reminders from the calendar app
        money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(money.get_id())});
    }

    public int getMoneysCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int updateMoney(Money money) {
        ContentValues values = new ContentValues();

        values.put(TITLE, money.get_title());
        values.put(TYPE_FK, money.get_typeFkId());
        values.put(DETAILS, money.get_details());
        values.put(AMOUNT, money.get_amount());
        values.put(DATE, money.get_date());
        values.put(CONTACT_FK, money.get_contactFkId());
        values.put(REMINDER_FK, money.get_reminderFkId());

        int rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + money.get_id(), null);

        return rowsAffected;
    }

    public List<Money> getAllMoneys() {
        List<Money> moneys = new ArrayList<Money>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                moneys.add(new Money(cursor.getString(0), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7))););
            }
            while (cursor.moveToNext());
        }

        return moneys;
    }

    /* Vérifier la nécessité de cette fonction
    public List<Money> getNextMoneys() {
        List<Money> moneys = new ArrayList<Money>();
        Calendar calDate = Calendar.getInstance();
        int day = calDate.get(Calendar.DAY_OF_MONTH);
        int month = calDate.get(Calendar.MONTH);
        int year = calDate.get(Calendar.YEAR);

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE MONTH = " + month + " AND YEAR = " + year + " AND DAY > " + day +
                        " UNION ALL SELECT * FROM " + DATABASE_TABLE + " WHERE MONTH > " + month + " AND YEAR = " + year +
                        " UNION ALL SELECT * FROM " + DATABASE_TABLE + " WHERE YEAR > " + year
                , null);

        if (cursor.moveToFirst()) {
            do {
                moneys.add(new Money(cursor.getString(0), Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7))););
            }
            while (cursor.moveToNext());
        }

        return moneys;
    }*/
}
