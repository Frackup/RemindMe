package com.example.avescera.remindme.Classes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

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
    private List<Integer> _eventInfo;

    private DatabaseReminderHandler dbRemHandler;
    private static final String DEBUG_TAG = "CalendarActivity";
    private static final String CALENDAR_URI_BASE = "content://com.android.calendar/";

    public Object(int id, String title, int quantity, String details, Date date, int categoryFkId, int typeFkId, int contactFkId, Integer reminderFkId, Date endDate, boolean urgent, List<Integer> eventInfo) {
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
        this._eventInfo = eventInfo;
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
    public void addEvent(Context context) {

        try {
            int year, month, day, hour, minute;
            year = this._eventInfo.get(0);
            month = this._eventInfo.get(1);
            day = this._eventInfo.get(2);
            hour = this._eventInfo.get(3);
            minute = this._eventInfo.get(4);

            GregorianCalendar calDate = new GregorianCalendar(year, month, day, hour, minute);

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
                setReminder(cr, eventId, dbRemHandler.getReminder(ActivityClass.URGENT_REMINDER).get_duration());

            if(dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).is_active())
                setReminder(cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_1).get_duration());

            if(dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).is_active())
                setReminder(cr, eventId, dbRemHandler.getReminder(ActivityClass.TGT_DATE_REMINDER_2).get_duration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) {
        try {
            System.out.println(Calendar.getInstance().getTimeZone().getID());

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(uri.getLastPathSegment()));
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
}
