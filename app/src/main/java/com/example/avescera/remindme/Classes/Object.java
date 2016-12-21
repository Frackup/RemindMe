package com.example.avescera.remindme.Classes;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.avescera.remindme.DBHandlers.DatabaseReminderHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by a.vescera on 24/11/2016.
 * This class details the item Object, exchanged between users.
 */

public class Object implements Serializable {

    private int _id;
    private String _title;
    private int _quantity;
    private String _details;
    private Date _date;
    private int _categoryFkId;
    private int _typeFkId;
    private int _contactFkId;
    private Integer _reminderFkId;
    private Date _endDate;
    private boolean _urgent;

    private DatabaseReminderHandler dbRemHandler;
    private static final String DEBUG_TAG = "CalendarActivity";
    private static final String CALENDAR_URI_BASE = "content://com.android.calendar/";

    public Object(int id, String title, int quantity, String details, Date date, int categoryFkId, int typeFkId, int contactFkId, Integer reminderFkId, Date endDate, boolean urgent) {
        this._id = id;
        this._title = title;
        this._quantity = quantity;
        this._details = details;
        this._date = date;
        this._categoryFkId = categoryFkId;
        this._typeFkId = typeFkId;
        this._contactFkId = contactFkId;
        this._reminderFkId = reminderFkId;
        this._endDate = endDate;
        this._urgent = urgent;
    }

    //Getters

    public int get_id() {
        return this._id;
    }

    public String get_title() {
        return this._title;
    }

    public int get_quantity() {
        return this._quantity;
    }

    public String get_details() {
        return this._details;
    }

    public Date get_date() {
        return this._date;
    }

    public int get_categoryFkId() {
        return this._categoryFkId;
    }

    public int get_typeFkId() {
        return this._typeFkId;
    }

    public int get_contactFkId() {
        return this._contactFkId;
    }

    public Integer get_reminderFkId() {
        return this._reminderFkId;
    }

    public Date get_endDate() {
        return this._endDate;
    }

    public boolean is_urgent() {
        return _urgent;
    }

    //Setters

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_categoryFkId(int _categoryFkId) {
        this._categoryFkId = _categoryFkId;
    }

    public void set_typeFkId(int _typeFkId) {
        this._typeFkId = _typeFkId;
    }

    public void set_contactFkId(int _contactFkId) {
        this._contactFkId = _contactFkId;
    }

    public void set_reminderFkId(Integer _reminderFkId) {
        this._reminderFkId = _reminderFkId;
    }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public void set_urgent(boolean _urgent) {
        this._urgent = _urgent;
    }

    //Event and Reminder adding part
    //An event has to be created into the calendar to then be able to attache a reminder to it.
    public void addEvent(Context context, List<List<String>> eventInfo) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }


        try {
            int date_year, date_month, date_day, endDate_year, endDate_month, endDate_day, hour, minute;

            if(eventInfo.get(0).get(0).matches("Date")){
                date_year = Integer.parseInt(eventInfo.get(0).get(1));
                date_month = Integer.parseInt(eventInfo.get(1).get(1));
                date_day = Integer.parseInt(eventInfo.get(2).get(1));
                endDate_year = Integer.parseInt(eventInfo.get(3).get(1));
                endDate_month = Integer.parseInt(eventInfo.get(4).get(1));
                endDate_day = Integer.parseInt(eventInfo.get(5).get(1));
            } else {
                endDate_year = Integer.parseInt(eventInfo.get(0).get(1));
                endDate_month = Integer.parseInt(eventInfo.get(1).get(1));
                endDate_day = Integer.parseInt(eventInfo.get(2).get(1));
                date_year = Integer.parseInt(eventInfo.get(3).get(1));
                date_month = Integer.parseInt(eventInfo.get(4).get(1));
                date_day = Integer.parseInt(eventInfo.get(5).get(1));
            }
            hour = 11;
            minute = 00;

            GregorianCalendar calDate = new GregorianCalendar(date_year, date_month, date_day, hour, minute);

            dbRemHandler = new DatabaseReminderHandler(context);
            dbRemHandler.open();

            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
            values.put(CalendarContract.Events.TITLE, this._title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
            values.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis()+60*60*500);

            System.out.println(Calendar.getInstance().getTimeZone().getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // Save the eventId into the Task object for possible future delete.
            long eventId = Long.parseLong(uri.getLastPathSegment());

            // Check into database if each of the possible reminders are set to active and add a reminder to it if it's the case.
            if(dbRemHandler.getReminder(ActivityClass.URGENT_REMINDER).is_active())
                setReminder(context, cr, eventId, dbRemHandler.getReminder(ActivityClass.URGENT_REMINDER).get_duration());

            if(dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).is_active())
                setReminder(context, cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).get_duration());

            if(dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).is_active())
                setReminder(context, cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).get_duration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    public void setReminder(Context context, ContentResolver cr, long eventID, int timeBefore) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return  ;
        }

        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});
            if (c.moveToFirst()) {
                System.out.println("calendar"
                        + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddEventClicked(View view, Context context, List<Integer> eventInfo) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        int year = eventInfo.get(0);
        int month = eventInfo.get(1);
        int day = eventInfo.get(2);
        int hour = 11;
        int minute = 0;

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "Calendar test");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        //intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        context.startActivity(intent);
    }
}
