package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by a.vescera on 25/11/2016.
 * This class contains all the functions allowing to retrieve Object items for different usecases.
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
    private static final String END_DATE = "end_date";
    private static final String URGENT = "urgent";

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
     * @param context is used to retrieve the Activity context and store it for later use to handle Object item in Database.
     */
    public DatabaseObjectHandler(Context context) {
        mCtx = context;
        dateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
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
        int urgent = (object.is_urgent())? 1 : 0;

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
        if(object.get_endDate() == null) {
            values.put(END_DATE, (String) null);
        } else {
            values.put(END_DATE, dateFormat.format(object.get_endDate()));
        }
        values.put(URGENT, urgent);

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Object getObject(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, QUANTITY_LOAN, QUANTITY_BORROW, DETAILS, DATE, CATEGORY_FK_ID, TYPE_FK_ID, CONTACT_FK_ID, REMINDER_FK_ID, END_DATE, URGENT }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;

        int quantity = (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE)?
                Integer.parseInt(cursor.getString(2)) :
                Integer.parseInt(cursor.getString(3));

        Integer temp = (cursor.getString(9) == null)?
                null :
                Integer.parseInt(cursor.getString(9));

        boolean urgent =  (Integer.parseInt(cursor.getString(11)) == 1);

        try {
            Date date = dateFormat.parse(cursor.getString(5));
            Date endDate = (cursor.getString(10) == null)?
                    null :
                    dateFormat.parse(cursor.getString(10));

            Object object = new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp, endDate,
                    urgent);

            cursor.close();
            return object;
        } catch (ParseException e) {
            e.printStackTrace();
            cursor.close();
            return null;
        }
    }

    public void deleteObject(Object object) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(object.get_id())});
    }

    void deleteAllContactObject(int contactId) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, CONTACT_FK_ID + "=?", new String[]{String.valueOf(contactId)});
    }

    private int getObjectsCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getObjectsNextId() {
        return getObjectsCount() + 1;
    }

    public int updateObject(Object object) {
        ContentValues values = new ContentValues();
        int urgent = (object.is_urgent())? 1 : 0;

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
        if(object.get_endDate() == null) {
            values.put(END_DATE, (String) null);
        } else {
            values.put(END_DATE, dateFormat.format(object.get_endDate()));
        }
        values.put(URGENT, urgent);

        int rowsAffected;
        rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + object.get_id(), null);

        return rowsAffected;
    }

    public List<Object> getTypeObjects(int type, String filter) {
        List<Object> objectList = new ArrayList<>();

        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan ASC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan DESC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " ORDER BY " + filter, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));
                    Date endDate = (cursor.getString(10) == null)?
                            null :
                            dateFormat.parse(cursor.getString(10));

                    Integer temp = (cursor.getString(9) == null)?
                            null :
                            Integer.parseInt(cursor.getString(9));

                    int quantity = (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Integer.parseInt(cursor.getString(2)) :
                            Integer.parseInt(cursor.getString(3));

                    boolean urgent = (Integer.parseInt(cursor.getString(11)) == 1);

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp,
                            endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

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
        cursor.close();

        return quantity;
    }

    public int getTotalQtyByTypeAndContact(int contact, int type) {
        Cursor cursor = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                mDb.rawQuery("SELECT quantity_loan FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null) :
                mDb.rawQuery("SELECT quantity_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);

        int quantity = 0;

        if (cursor.moveToFirst()) {
            do {
                quantity += Integer.parseInt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return quantity;
    }

    public List<Object> getContactTypeObjects(int contact, int type, String filter) {
        List<Object> objectList = new ArrayList<>();
        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan ASC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan DESC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact + " ORDER BY " + filter, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));
                    Date endDate = (cursor.getString(10) == null)?
                            null :
                            dateFormat.parse(cursor.getString(10));

                    Integer temp = (cursor.getString(9) == null)?
                            null :
                            Integer.parseInt(cursor.getString(9));

                    int quantity = (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Integer.parseInt(cursor.getString(2)) :
                            Integer.parseInt(cursor.getString(3));

                    boolean urgent = (Integer.parseInt(cursor.getString(11)) == 1);

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp,
                            endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

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
        cursor.close();

        return quantityByMonth;
    }

    public void updateAllObjectsCategory(Category category) {
        Cursor cursor = mDb.rawQuery("UPDATE " + DATABASE_TABLE + " SET category = 'no_category' WHERE category = " + category, null);

        cursor.close();
    }

    public int getTotalQtyByTypeAndCategory(int category, int type) {
        Cursor cursor = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                mDb.rawQuery("SELECT quantity_loan FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND category = " + category, null) :
                mDb.rawQuery("SELECT quantity_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND category = " + category, null);

        int quantity = 0;

        if (cursor.moveToFirst()) {
            do {
                quantity += Integer.parseInt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return quantity;
    }

    public int getTotalQtyByCatTypeAndContact(int contact, int category, int type) {
        Cursor cursor = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                mDb.rawQuery("SELECT quantity_loan FROM " + DATABASE_TABLE + " WHERE category = " + category + " AND contact = " + contact + " AND type = " + type, null) :
                mDb.rawQuery("SELECT quantity_borrow FROM " + DATABASE_TABLE + " WHERE category = " + category + " AND contact = " + contact + " AND type = " + type, null);

        int quantity = 0;

        if (cursor.moveToFirst()) {
            do {
                quantity += Integer.parseInt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return quantity;
    }

    public List<Object> getCategoryTypeObjects(int category, int type, String filter) {
        List<Object> objectList = new ArrayList<>();
        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan ASC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan DESC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND category = " + category + " ORDER BY " + filter, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));
                    Date endDate = (cursor.getString(10) == null)?
                            null :
                            dateFormat.parse(cursor.getString(10));

                    Integer temp = (cursor.getString(9) == null)?
                            null :
                            Integer.parseInt(cursor.getString(9));

                    int quantity = (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Integer.parseInt(cursor.getString(2)) :
                            Integer.parseInt(cursor.getString(3));

                    boolean urgent = (Integer.parseInt(cursor.getString(11)) == 1);

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp,
                            endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return objectList;
    }

    public List<Object> getContactCatTypeObjects(int contact, int category, int type, String filter) {
        List<Object> objectList = new ArrayList<>();
        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan ASC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "quantity_loan DESC" :
                        "quantity_borrow ASC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact + " AND category = " + category + " ORDER BY " + filter, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));
                    Date endDate = (cursor.getString(10) == null)?
                            null :
                            dateFormat.parse(cursor.getString(10));

                    Integer temp = (cursor.getString(9) == null)?
                            null :
                            Integer.parseInt(cursor.getString(9));

                    int quantity = (Integer.parseInt(cursor.getString(7)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Integer.parseInt(cursor.getString(2)) :
                            Integer.parseInt(cursor.getString(3));

                    boolean urgent = (Integer.parseInt(cursor.getString(11)) == 1);

                    objectList.add(new Object(Integer.parseInt(cursor.getString(0)), cursor.getString(1), quantity, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)), temp,
                            endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return objectList;
    }
}
