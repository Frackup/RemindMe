package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Reminder;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.vescera on 20/12/2016.
 * This class contains all the functions allowing to retrieve Contact items for different usecases.
 */

public class DatabaseReminderHandler {

    private static final String DATABASE_TABLE = "reminder";

    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String ACTIVE = "active";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String DURATION = "duration";

    private DatabaseReminderHandler.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

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
    public DatabaseReminderHandler(Context context) {
        mCtx = context;
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseReminderHandler open() throws SQLException {
        mDbHelper = new DatabaseReminderHandler.DatabaseHelper(mCtx);
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

    public void createReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        int active =  (reminder.is_active())? 1 : 0;

        values.put(DESCRIPTION, reminder.get_description());
        values.put(ACTIVE, active);
        values.put(HOUR, reminder.get_hour());
        values.put(MINUTE, reminder.get_minute());
        values.put(DURATION, reminder.get_duration());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Reminder getReminder(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, DESCRIPTION, ACTIVE, HOUR, MINUTE, DURATION }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        boolean active = (Integer.parseInt(cursor.getString(2)) == 1);

        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), active, Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

        cursor.close();
        return reminder;
    }

    public Reminder getReminder(String description) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, DESCRIPTION, ACTIVE, HOUR, MINUTE, DURATION }, DESCRIPTION + "=?", new String[] { description }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        boolean active = (Integer.parseInt(cursor.getString(2)) ==  1);

        Reminder reminder = new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), active, Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));

        cursor.close();
        return reminder;
    }

    private int getRemindersCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        cursor.close();
        return count;
    }

    public int getRemindersNextId(){
        return getRemindersCount() + 1;
    }

    public int getActiveReminders() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE active = 1", null);
        int count = cursor.getCount();

        cursor.close();
        return count;
    }

    public List<Reminder> getActiveListReminders() {
        List<Reminder> reminders = new ArrayList<>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE  + " WHERE active = 1", null);

        if (cursor.moveToFirst()) {
            do {
                boolean active = (Integer.parseInt(cursor.getString(2)) == 1);

                reminders.add(new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), active, Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5))));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return reminders;
    }

    public int updateReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        int active = (reminder.is_active())? 1 : 0;

        values.put(DESCRIPTION, reminder.get_description());
        values.put(ACTIVE, active);
        values.put(HOUR, reminder.get_hour());
        values.put(MINUTE, reminder.get_minute());
        values.put(DURATION, reminder.get_duration());

        int rowsAffected;
        rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + reminder.get_id(), null);

        return rowsAffected;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                boolean active = (Integer.parseInt(cursor.getString(2)) == 1);
                reminders.add(new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), active, Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5))));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return reminders;
    }

    public int getCountTgtDateActiveReminders(){
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE (description = '" + ActivityClass.TGT_DATE_REMINDER_1 +
                "' OR DESCRIPTION = '" + ActivityClass.TGT_DATE_REMINDER_2 + "') AND active = 1", null);
        int total = cursor.getCount();
        cursor.close();

        return total;
    }
}
