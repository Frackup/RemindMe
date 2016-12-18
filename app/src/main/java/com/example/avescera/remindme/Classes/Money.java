package com.example.avescera.remindme.Classes;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by a.vescera on 22/11/2016.
 * This class details the item Money, exchanged between users.
 */

public class Money implements Serializable {
    private int _id;
    private String _title;
    private float _amount;
    private String _details;
    private Date _date;
    private int _typeFkId;
    private int _contactFkId;
    private Integer _reminderFkId;
    private Date _endDate;
    private boolean _urgent;

    public Money(int id, String title, float amount, String details, Date date, int typeFkId, int contactFkId, Integer reminderFkId, Date endDate, boolean urgent) {
        this._id = id;
        this._title = title;
        this._amount = Math.round(amount*100)/100f;
        this._details = details;
        this._date = date;
        this._typeFkId = typeFkId;
        this._contactFkId = contactFkId;
        this._reminderFkId = reminderFkId;
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

    public Integer get_reminderFkId() { return this._reminderFkId; }

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

    public void set_reminderFkId(Integer reminderFkId) { this._reminderFkId = reminderFkId; }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public void set_urgent(boolean _urgent) {
        this._urgent = _urgent;
    }
}
