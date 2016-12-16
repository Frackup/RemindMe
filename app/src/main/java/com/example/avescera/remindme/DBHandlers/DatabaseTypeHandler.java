package com.example.avescera.remindme.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.avescera.remindme.Classes.Type;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frackup on 25/11/2016.
 * This class contains all the functions allowing to retrieve Type items for different usecases.
 */

public class DatabaseTypeHandler {
    private static final String DATABASE_TABLE = "type";

    private static final String ID = "id";
    private static final String TYPE = "type";

    private DatabaseHelper mDbHelper;
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
     * @param context is used to retrieve the Activity context and store it for later use to handle Type item in Database.
     */
    public DatabaseTypeHandler(Context context) {
        mCtx = context;
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DatabaseTaskHandler
     */
    public DatabaseTypeHandler open() throws SQLException {
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

    public void createType(Type type) {
        ContentValues values = new ContentValues();

        values.put(TYPE, type.get_type());

        mDb.insert(DATABASE_TABLE, null, values);
    }

    private int getTypeCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getTypeNextId() {
        return getTypeCount() + 1;
    }

    public List<Type> getAllTypes() {
        List<Type> typesList = new ArrayList<>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                typesList.add(new Type(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return typesList;
    }
}
