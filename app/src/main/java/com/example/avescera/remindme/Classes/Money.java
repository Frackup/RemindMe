package com.example.avescera.remindme.Classes;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;

import com.example.avescera.remindme.DBHandlers.DatabaseReminderHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by a.vescera on 22/11/2016.
 * This class details the item Money, exchanged between users.
 */

public class Money implements Serializable {
    private final int _id;
    private String _title;
    private float _amount;
    private String _details;
    private Date _date;
    private int _typeFkId;
    private int _contactFkId;
    private Date _endDate;
    private boolean _urgent;

    public Money(int id, String title, float amount, String details, Date date, int typeFkId, int contactFkId, Date endDate, boolean urgent) {
        this._id = id;
        this._title = title;
        this._amount = Math.round(amount*100)/100f;
        this._details = details;
        this._date = date;
        this._typeFkId = typeFkId;
        this._contactFkId = contactFkId;
        this._endDate = endDate;
        this._urgent = urgent;
    }

    //Getters
    public int get_id() { return this._id; }

    public String get_title() { return this._title; }

    public float get_amount() { return this._amount; }

    public String get_details() { return this._details; }

    public Date get_date() { return this._date; }

    public int get_typeFkId() { return this._typeFkId; }

    public int get_contactFkId() { return this._contactFkId; }

    public Date get_endDate() {
        return _endDate;
    }

    public boolean is_urgent() {
        return _urgent;
    }

    //Setters
    public void set_title(String title) { this._title = title; }

    public void set_amount(float amount) { this._amount = amount; }

    public void set_details(String details) { this._details = details; }

    public void set_date(Date date) { this._date = date; }

    public void set_typeFkId(int typeFkId) { this._typeFkId = typeFkId; }

    public void set_contactFkId(int contactFkId) { this._contactFkId = contactFkId; }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public void set_urgent(boolean _urgent) {
        this._urgent = _urgent;
    }

    //Event and Reminder adding part
    //An event has to be created into the calendar to then be able to attache a reminder to it.
    public void addEvent(Context context, List<Integer> eventInfo, boolean urgent) {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try {
            int date_year, date_month, date_day, hour, minute;

            date_year = eventInfo.get(0);
            date_month = eventInfo.get(1);
            date_day = eventInfo.get(2);
            hour = 11;
            minute = 0;

            Integer[] months31 = {1,3,5,7,8,10,12};
            Integer[] months30 = {4,6,9,11};
            Integer feb = 2;

            DatabaseReminderHandler dbRemHandler = new DatabaseReminderHandler(context);
            try {
                dbRemHandler.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(urgent){
                Reminder urgentRem = dbRemHandler.getReminder(ActivityClass.URGENT_REMINDER);
                date_day = urgentRem.get_hour()/24 + date_day;
                if(date_day >= 31){
                    if (date_month == feb){
                        date_month += 1;
                        date_day -= 28;
                    } else if (Arrays.asList(months30).contains(date_month)){
                        date_day -= 30;
                        date_month += 1;
                    } else if (Arrays.asList(months31).contains(date_month)) {
                        date_day -= 31;
                        date_month += 1;
                        if (date_month == 13) {
                            date_month = 1;
                            date_year += 1;
                        }
                    }
                }
            }
            date_month -= 1; // months start from 0 with calendar.

            GregorianCalendar calDate = new GregorianCalendar(date_year, date_month, date_day, hour, minute);

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

            // Check into database which reminders have to be set.
            if (urgent) {
                setReminder(context, cr, eventId, 5);
            } else {
                if (dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).is_active())
                    setReminder(context, cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).get_duration());

                if (dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).is_active())
                    setReminder(context, cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).get_duration());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    private void setReminder(Context context, ContentResolver cr, long eventID, int timeBefore) {
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
            cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
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
}
