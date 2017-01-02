package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Money;
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
 * Created by a.vescera on 23/11/2016.
 * This class contains all the functions allowing to retrieve Money items for different usecases.
 */

public class DatabaseMoneyHandler {

    private static final String DATABASE_TABLE = "money";

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String AMOUNT_LOAN = "amount_loan";
    private static final String AMOUNT_BORROW = "amount_borrow";
    private static final String DETAILS = "details";
    private static final String DATE = "date";
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
     * @param context is used to retrieve the Activity context and store it for later use to handle Money item in Database.
     */
    public DatabaseMoneyHandler(Context context) {
        mCtx = context;
        dateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
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
        int urgent = (money.is_urgent())? 1 : 0;

        values.put(TITLE, money.get_title());
        if (money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            values.put(AMOUNT_LOAN, money.get_amount());
            values.put(AMOUNT_BORROW, 0);
        } else {
            values.put(AMOUNT_LOAN, 0);
            values.put(AMOUNT_BORROW, money.get_amount());
        }
        values.put(DETAILS, money.get_details());
        values.put(DATE, dateFormat.format(money.get_date()));
        values.put(TYPE_FK_ID, money.get_typeFkId());
        values.put(CONTACT_FK_ID, money.get_contactFkId());
        if(money.get_endDate() == null) {
            values.put(END_DATE, (String) null);
        } else {
            values.put(END_DATE, dateFormat.format(money.get_endDate()));
        }
        values.put(URGENT, urgent);

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Money getMoney(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, AMOUNT_LOAN, AMOUNT_BORROW, DETAILS, DATE, TYPE_FK_ID, CONTACT_FK_ID, REMINDER_FK_ID, END_DATE, URGENT }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        float amount;

        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;

        amount = (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE)?
                Math.round(Float.parseFloat(cursor.getString(2))*100)/100f:
                Math.round(Float.parseFloat(cursor.getString(3))*100)/100f;

        boolean urgent = (Integer.parseInt(cursor.getString(10)) == 1);

        try {
            Date date = dateFormat.parse(cursor.getString(5));
            Date endDate = (cursor.getString(9) == null)?
                    null :
                    dateFormat.parse(cursor.getString(9));

            Money money = new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), date,
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), endDate, urgent);

            cursor.close();
            return money;
        } catch (ParseException e) {
            e.printStackTrace();
            cursor.close();
            return null;
        }
    }

    public void deleteMoney(Money money) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(money.get_id())});
    }

    void deleteAllContactMoney(int contactId) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, CONTACT_FK_ID + "=?", new String[]{String.valueOf(contactId)});
    }

    private int getMoneysCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getMoneysNextId() {
        return getMoneysCount() + 1;
    }

    public int updateMoney(Money money) {
        ContentValues values = new ContentValues();
        int urgent = (money.is_urgent())? 1 : 0;

        values.put(TITLE, money.get_title());
        if (money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            values.put(AMOUNT_LOAN, money.get_amount());
            values.put(AMOUNT_BORROW, 0);
        } else {
            values.put(AMOUNT_LOAN, 0);
            values.put(AMOUNT_BORROW, money.get_amount());
        }
        values.put(DETAILS, money.get_details());
        values.put(DATE, money.get_date().toString());
        values.put(TYPE_FK_ID, money.get_typeFkId());
        values.put(CONTACT_FK_ID, money.get_contactFkId());
        if(money.get_endDate() == null) {
            values.put(END_DATE, (String) null);
        } else {
            values.put(END_DATE, dateFormat.format(money.get_endDate()));
        }
        values.put(URGENT, urgent);

        int rowsAffected;
        rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + money.get_id(), null);

        return rowsAffected;
    }

    public List<Money> getTypeMoneys(int type, String filter) {
        List<Money> moneyList = new ArrayList<>();

        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "amount_loan ASC" :
                        "amount_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "amount_loan DESC" :
                        "amount_borrow DESC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type + " ORDER BY " + filter, null);
        float amount;

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(5))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Date endDate = (cursor.getString(9)== null)?
                            null :
                            dateFormat.parse(cursor.getString(9));

                    amount = (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Math.round(Float.parseFloat(cursor.getString(2))*100)/100f:
                            Math.round(Float.parseFloat(cursor.getString(3))*100)/100f;

                    boolean urgent = (Integer.parseInt(cursor.getString(10)) == 1);

                    moneyList.add(new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return moneyList;
    }

    public float getTotalAmountByType(int type) {
        Cursor cursor;

        if(type == ActivityClass.DATABASE_LOAN_TYPE) {
            cursor = mDb.rawQuery("SELECT amount_loan FROM " + DATABASE_TABLE + " WHERE type = " + type, null);
        } else {
            cursor = mDb.rawQuery("SELECT amount_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type, null);
        }

        float amount = 0.0f;

        if (cursor.moveToFirst()) {
            do {
                amount += Float.parseFloat(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        amount = Math.round(amount*100)/100f;
        cursor.close();

        return amount;
    }

    public float getTotalAmountByTypeAndContact(int contact, int type) {
        Cursor cursor;

        cursor = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                mDb.rawQuery("SELECT amount_loan FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null) :
                mDb.rawQuery("SELECT amount_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);

        float amount = 0.0f;

        if (cursor.moveToFirst()) {
            do {
                amount += Float.parseFloat(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        amount = Math.round(amount*100)/100f;
        cursor.close();

        return amount;
    }

    public List<Money> getContactTypeMoneys(int contact, int type, String filter) {
        List<Money> moneyList = new ArrayList<>();
        float amount;

        filter = (filter == null)? "date ASC" : filter;
        switch (filter) {
            case ActivityClass.FILTER_SPEC_ASC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "amount_loan ASC" :
                        "amount_borrow ASC";
                break;
            case ActivityClass.FILTER_SPEC_DESC:
                filter = (type == ActivityClass.DATABASE_LOAN_TYPE)?
                        "amount_loan DESC" :
                        "amount_borrow DESC";
                break;
            case ActivityClass.FILTER_DATE_ASC:
                filter = "date ASC";
                break;
            case ActivityClass.FILTER_DATE_DESC:
                filter = "date DESC";
                break;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE contact = " + contact + " AND type = " + type + " ORDER BY " + filter, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Date endDate = (cursor.getString(9)== null)?
                            null :
                            dateFormat.parse(cursor.getString(9));

                    amount = (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE)?
                            Math.round(Float.parseFloat(cursor.getString(2))*100)/100f:
                            Math.round(Float.parseFloat(cursor.getString(3))*100)/100f;

                    boolean urgent = (Integer.parseInt(cursor.getString(10)) == 1);

                    moneyList.add(new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), endDate, urgent));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return moneyList;
    }

    public List<List<Float>> getLastSixMonthsMoney() {
    Cursor cursor = mDb.rawQuery("SELECT strftime('%m', date) , SUM(amount_loan), SUM(amount_borrow), date  FROM " +
                "(SELECT date, amount_loan, amount_borrow FROM " + DATABASE_TABLE + " WHERE date > date('now', '-6 months') AND date <= date('now')) GROUP BY strftime('%m', date) ORDER BY date ASC", null);

        List<List<Float>> amountByMonth = new ArrayList<>();
        List<Float> data = new ArrayList<>();

        if (cursor.moveToFirst()) do {
            data.add(Float.parseFloat(cursor.getString(0)));
            data.add(Float.parseFloat(cursor.getString(1)));
            data.add(Float.parseFloat(cursor.getString(2)));
            amountByMonth.add(data);
        }
        while (cursor.moveToNext());
        cursor.close();

        return amountByMonth;
    }
}
