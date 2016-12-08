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
     * @param context
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

    public Type getType(int id) {
        Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { ID, TYPE }, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null );

        if (cursor != null)
            cursor.moveToFirst();

        Type type = new Type(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return type;
    }

    public void deleteType(Type type, Context context) {
        mDb.delete(DATABASE_TABLE, ID + "=?", new String[]{String.valueOf(type.get_id())});
    }

    public int getTypeCount() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        int count = cursor.getCount();

        return count;
    }

    public int getTypeNextId() {
        int nextId = getTypeCount() + 1;
        return nextId;
    }

    public int updateType(Type type) {
        ContentValues values = new ContentValues();

        values.put(TYPE, type.get_type());

        int rowsAffected = mDb.update(DATABASE_TABLE, values, ID + "=" + type.get_id(), null);

        return rowsAffected;
    }

    public List<Type> getAllTypes() {
        List<Type> typesList = new ArrayList<Type>();

        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                typesList.add(new Type(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
            }
            while (cursor.moveToNext());
        }

        return typesList;
    }
}
