package com.example.avescera.remindme.DBHandlers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by a.vescera on 23/11/2016.
 */

public class DatabaseAdapter {

    public static final String DATABASE_NAME = "Remindme";

    public static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACT = "contact",
        KEY_CONTACT_ID = "id",
        KEY_CONTACT_FNAME = "firstname",
        KEY_CONTACT_LNAME = "lastname",
        KEY_CONTACT_PHONE = "phone",
        KEY_CONTACT_EMAIL = "email";

    private static final String TABLE_MONEY = "money",
            KEY_MONEY_ID = "id",
            KEY_MONEY_TITLE = "title",
            KEY_MONEY_AMOUNT = "amount",
            KEY_MONEY_DETAILS = "details",
            KEY_MONEY_DATE = "date",
            KEY_MONEY_TYPE_FK_ID = "type",
            KEY_MONEY_CONTACT_FK_ID = "contact",
            KEY_MONEY_REMINDER_FK_ID = "reminder";

    private static final String TABLE_OBJECT = "object",
            KEY_OBJECT_ID = "id",
            KEY_OBJECT_TITLE = "title",
            KEY_OBJECT_QUANTITY = "quantity",
            KEY_OBJECT_DETAILS = "details",
            KEY_OBJECT_DATE = "date",
            KEY_OBJECT_CATEGORY_FK_ID = "category",
            KEY_OBJECT_TYPE_FK_ID = "type",
            KEY_OBJECT_CONTACT_FK_ID = "contact",
            KEY_OBJECT_REMINDER_FK_ID = "reminder";

    private static final String TABLE_TYPE = "type",
        KEY_TYPE_ID = "id",
        KEY_TYPE_TYPE = "type";

    private static final String TABLE_CATEGORY = "category",
        KEY_CATEGORY_ID = "id",
        KEY_CATEGORY_CATEGORY = "category";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private static final String TAG = "NotesDatabaseAdapter";

    /**
     * Constructor
     * @param ctx
     */
    public DatabaseAdapter(Context ctx)
    {
        context = ctx;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + TABLE_CONTACT + "(" + KEY_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTACT_FNAME + " TEXT," +
                    KEY_CONTACT_LNAME + " TEXT," + KEY_CONTACT_PHONE + " TEXT," + KEY_CONTACT_EMAIL + " TEXT)");

            db.execSQL("CREATE TABLE " + TABLE_MONEY + "(" + KEY_MONEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MONEY_TITLE + " TEXT," +
                    KEY_MONEY_AMOUNT + " REAL," + KEY_MONEY_DETAILS + " TEXT," + KEY_MONEY_DATE + " TEXT," + KEY_MONEY_TYPE_FK_ID + " INTEGER," +
                    KEY_MONEY_CONTACT_FK_ID + " INTEGER," + KEY_MONEY_REMINDER_FK_ID + " INTEGER)");

            db.execSQL("CREATE TABLE " + TABLE_OBJECT + "(" + KEY_OBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_OBJECT_TITLE + " TEXT," +
                    KEY_OBJECT_QUANTITY + " TEXT," + KEY_OBJECT_DETAILS + " TEXT," + KEY_OBJECT_DATE + " TEXT," + KEY_OBJECT_CATEGORY_FK_ID + " INTEGER," +
                    KEY_OBJECT_TYPE_FK_ID + " INTEGER," + KEY_OBJECT_CONTACT_FK_ID + " INTEGER," + KEY_OBJECT_REMINDER_FK_ID + " INTEGER)");

            db.execSQL("CREATE TABLE " + TABLE_TYPE + "(" + KEY_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TYPE_TYPE + " TEXT)");

            db.execSQL("CREATE TABLE " + TABLE_CATEGORY + "(" + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CATEGORY_CATEGORY + " TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseAdapter
     */
    public DatabaseAdapter open() throws SQLException
    {
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db
     * return type: void
     */
    public void close()
    {
        DBHelper.close();
    }
}
