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

/**
 * Created by a.vescera on 23/11/2016.
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
        //dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        dateFormat = new SimpleDateFormat("yyy-MM-dd");
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
        values.put(REMINDER_FK_ID, money.get_reminderFkId());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    public Money getMoney(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TITLE, AMOUNT_LOAN, AMOUNT_BORROW, DETAILS, DATE, TYPE_FK_ID, CONTACT_FK_ID, REMINDER_FK_ID }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        float amount;

        if (cursor != null)
            cursor.moveToFirst();

        if (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE) {
            amount = Float.parseFloat(cursor.getString(2));
        } else {
            amount = Float.parseFloat(cursor.getString(3));
        }

        Integer temp;
        if (cursor.getString(8) == null) {
            temp = null;
        } else {
            temp = Integer.parseInt(cursor.getString(8));
        }

        try {
            Money money = new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), dateFormat.parse(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), temp);

            return money;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteMoney(Money money, Context context) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(money.get_id())});
    }

    public void deleteAllContactMoney(int contactId, Context context) {
        // Remove the events and reminders from the calendar app
        //A activer une fois les reminders ajoutés
        //money.removeEvent(context);

        mDb.delete(DATABASE_TABLE, CONTACT_FK_ID + "=?", new String[]{String.valueOf(contactId)});
    }

    public int getMoneysCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int getMoneysNextId() {
        int nextId = getMoneysCount() + 1;
        return nextId;
    }

    public int updateMoney(Money money) {
        ContentValues values = new ContentValues();

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
        values.put(REMINDER_FK_ID, money.get_reminderFkId());

        int rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + money.get_id(), null);

        return rowsAffected;
    }

    public List<Money> getAllMoneys() {
        List<Money> moneyList = new ArrayList<Money>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        float amount;

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(8) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(8));
                    }

                    if (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        amount = Float.parseFloat(cursor.getString(2));
                    } else {
                        amount = Float.parseFloat(cursor.getString(3));
                    }

                    moneyList.add(new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), date,
                        Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return moneyList;
    }

    public List<Money> getTypeMoneys(int type) {
        List<Money> moneyList = new ArrayList<Money>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE type = " + type, null);
        float amount;

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(5))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(8) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(8));
                    }

                    if (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        amount = Float.parseFloat(cursor.getString(2));
                    } else {
                        amount = Float.parseFloat(cursor.getString(3));
                    }

                    moneyList.add(new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(3), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

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

        return amount;
    }

    public float getTotalAmountByTypeAndContact(int contact, int type) {
        Cursor cursor;

        if(type == ActivityClass.DATABASE_LOAN_TYPE) {
            cursor = mDb.rawQuery("SELECT amount_loan FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);
        } else {
            cursor = mDb.rawQuery("SELECT amount_borrow FROM " + DATABASE_TABLE + " WHERE type = " + type + " AND contact = " + contact, null);
        }

        float amount = 0.0f;

        if (cursor.moveToFirst()) {
            do {
                amount += Float.parseFloat(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        amount = Math.round(amount*100)/100f;

        return amount;
    }

    public List<Money> getContactTypeMoneys(int contact, int type) {
        List<Money> moneyList = new ArrayList<Money>();
        float amount;

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE contact = " + contact + " AND type = " + type, null);

        if (cursor.moveToFirst()) {
            do {
                //Try catch put there to handle the ParseException when putting directly "dateFormat.parse(cursor.getString(4))" into the moneys.add
                try{
                    Date date = dateFormat.parse(cursor.getString(5));

                    Integer temp;

                    if (cursor.getString(8) == null) {
                        temp = null;
                    } else {
                        temp = Integer.parseInt(cursor.getString(8));
                    }

                    if (Integer.parseInt(cursor.getString(6)) == ActivityClass.DATABASE_LOAN_TYPE) {
                        amount = Float.parseFloat(cursor.getString(2));
                    } else {
                        amount = Float.parseFloat(cursor.getString(3));
                    }

                    moneyList.add(new Money(Integer.parseInt(cursor.getString(0)), cursor.getString(1), amount, cursor.getString(4), date,
                            Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (cursor.moveToNext());
        }

        return moneyList;
    }

    public List<List<Float>> getLastSixMonthsMoney() {
    Cursor cursor = mDb.rawQuery("SELECT strftime('%m', date) , SUM(amount_loan), SUM(amount_borrow), date  FROM " +
                "(SELECT date, amount_loan, amount_borrow FROM " + DATABASE_TABLE + " WHERE date > date('now', '-6 months') AND date <= date('now')) GROUP BY strftime('%m', date) ORDER BY date ASC", null);

        List<List<Float>> amountByMonth = new ArrayList<>();
        List<Float> data = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                data.add(Float.parseFloat(cursor.getString(0)));
                data.add(Float.parseFloat(cursor.getString(1)));
                data.add(Float.parseFloat(cursor.getString(2)));
                amountByMonth.add(data);
            }
            while (cursor.moveToNext());
        }

        return amountByMonth;
    }
}
