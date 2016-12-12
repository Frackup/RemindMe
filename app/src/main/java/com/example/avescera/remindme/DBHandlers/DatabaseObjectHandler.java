package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by a.vescera on 25/11/2016.
 */

public class DatabaseObjectHandler {

    private static final String DATABASE_TABLE = "object";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String QUANTITY_LOAN = "quantity_loan";
    private static final String QUANTITY_BORROW = "quantity_borrow";
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
        dateFormat = new SimpleDateFormat("yyy-MM-dd");
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
        if (object.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            values.put(QUANTITY_LOAN, object.get_quantity());
            values.put(QUANTITY_BORROW, 0);
        } else {
            values.put(QUANTITY_LOAN, 0);
            values.put(QUANTITY_BORROW, object.get_quantity());
        }
        values.put(DETAILS, object.get_details());
        values.put(DATE, dateFormat.format(object.get_date()));
        values.put(CATEGORY_FK_ID, object.get_categoryFkId());
        values.put(TYPE_FK_ID, object.get_typeFkId());
        values.put(CONTACT_FK_ID, object.get_contactFkId());
        values.put(REMINDER_FK_ID, object.get_reminderFkId());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Object getObject(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, QUANTITY_LOAN, QUANTITY_BORROW, DETAILS, DATE, CATEGORY_FK_ID, TYPE_FK_ID, CONTACT_FK_ID, REMINDER_FK_ID }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );
        int quantity;

        if (cursor != null)
            cursor.moveToFirst();

        if (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE) {
            quantity = Integer.parseInt(cursor.getString(2));
        } else {
            quantity = Integer.parseInt(cursor.getString(3));
        }

        try {
            Object object = new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), dateFormat.parse(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)));

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

    public void deleteAllContactObject(int contactId, Context context) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, CONTACT_FK_ID + "=?", new String[]{String.valueOf(contactId)});
    }

    public int getObjectsCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int getObjectsNextId() {
        int nextId = getObjectsCount() + 1;
        return nextId;
    }

    public int updateObject(Object object) {
        ContentValues values = new ContentValues();

        values.put(TITLE, object.get_title());
        if (object.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            values.put(QUANTITY_LOAN, object.get_quantity());
            values.put(QUANTITY_BORROW, 0);
        } else {
            values.put(QUANTITY_BORROW, object.get_quantity());
            values.put(QUANTITY_LOAN, 0);
        }
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
        int quantity;

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(9) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(9));
                    }

                    if (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        quantity = Integer.parseInt(cursor.getString(2));
                    } else {
                        quantity = Integer.parseInt(cursor.getString(3));
                    }

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return objectList;
    }

    public List<Object> getTypeObjects(int type) {
        List<Object> objectList = new ArrayList<Object>();
        int quantity;

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(9) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(9));
                    }

                    if (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        quantity = Integer.parseInt(cursor.getString(2));
                    } else {
                        quantity = Integer.parseInt(cursor.getString(3));
                    }

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return objectList;
    }

    public int getTotalQtyByType(int type) {
        Cursor cursor;

        if(type == ActivityClass.DATABASE_LOAN_TYPE) {
            cursor = mDb.rawQuery("SELECT quantity_loan FROM " + DATABASE_TABLE + " WHERE type = " + type, null);
        } else {
            cursor = mDb.rawQuery("SELECT quantity_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type, null);
        }

        int quantity = 0;

        if (cursor.moveToFirst()) {
            do {
                quantity += Integer.parseInt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        return quantity;
    }

    public int getTotalQtyByTypeAndContact(int contact, int type) {
        Cursor cursor;

        if(type == ActivityClass.DATABASE_LOAN_TYPE) {
            cursor = mDb.rawQuery("SELECT quantity_loan FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);
        } else {
            cursor = mDb.rawQuery("SELECT quantity_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);
        }

        int quantity = 0;

        if (cursor.moveToFirst()) {
            do {
                quantity += Integer.parseInt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        return quantity;
    }

    public List<Object> getContactTypeObjects(int contact, int type) {
        List<Object> objectList = new ArrayList<Object>();
        int quantity;

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(9) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(9));
                    }

                    if (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        quantity = Integer.parseInt(cursor.getString(2));
                    } else {
                        quantity = Integer.parseInt(cursor.getString(3));
                    }

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return objectList;
    }

    public List<List<Float>> getLastSixMonthsObject() {
        Cursor cursor = mDb.rawQuery("SELECT strftime('%m', date) , SUM(quantity_loan), SUM(quantity_borrow), date  FROM " +
                "(SELECT date, quantity_loan, quantity_borrow FROM " + DATABASE_TABLE + " WHERE date > date('now', '-6 months') AND date < date('now')) GROUP BY strftime('%m', date) ORDER BY date", null);

        List<List<Float>> quantityByMonth = new ArrayList<>();
        List<Float> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(Float.parseFloat(cursor.getString(0)));
                data.add(Float.parseFloat(cursor.getString(1)));
                data.add(Float.parseFloat(cursor.getString(2)));
                quantityByMonth.add(data);
            }
            while (cursor.moveToNext());
        }

        return quantityByMonth;
    }
}
