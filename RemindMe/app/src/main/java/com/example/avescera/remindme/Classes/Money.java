package com.example.avescera.remindme.Classes;

import com.example.avescera.remindme.DBHandlers.DatabaseAdapter;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;

import java.util.Date;

/**
 * Created by a.vescera on 22/11/2016.
 */

public class Money {
    private String _title;
    private float _amount;
    private int _contactFkId;
    private String _details;
    private Date _date;
    private int _id;
    private int _reminderFkId;
    private int _typeFkId;

    private DatabaseMoneyHandler dbRemHandler;
    private DatabaseAdapter dbAdapter;

    //Constructeur sans reminder sur le prêt / emprunt d'argent
    public Money(int id, String title, float amount, String details, Date date, int contactFkId, int typeFkId) {
        this._id = id;
        this._title = title;
        this._details = details;
        this._date = date;
        this._amount = amount;
        this._contactFkId = contactFkId;
        this._typeFkId = typeFkId;
    }

    //Constructeur avec reminder sur le prêt / emprunt d'argent
    public Money(String title, float amount, String details, Date date, int contactFkId, int typeFkId, int reminderFkId) {
        this._title = title;
        this._details = details;
        this._date = date;
        this._amount = amount;
        this._contactFkId = contactFkId;
        this._typeFkId = typeFkId;
        this._reminderFkId = reminderFkId;
    }

    //Getters
    public String get_title() {
        return this._title;
    }

    public String get_details() {
        return this._details;
    }

    public float get_amount() {
        return this._amount;
    }

    public int get_contactFkId() {
        return this._contactFkId;
    }

    public Date get_date() {
        return this._date;
    }

    public int get_id() {
        return this._id;
    }

    public int get_reminderFkId() {
        return this._reminderFkId;
    }

    public int get_typeFkId() {
        return this._typeFkId;
    }


    //Setters
    public void set_title(String title) {
        this._title = title;
    }

    public void set_details(String details) {
        this._details = details;
    }

    public void set_amount(float amount) {
        this._amount = amount;
    }

    public void set_contactFkId(int contactFkId) {
        this._contactFkId = contactFkId;
    }

    public void set_date(Date date) {
        this._date = date;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public void set_reminderFkId(int reminderFkId) {
        this._reminderFkId = reminderFkId;
    }

    public void set_typeFkId(int typeFkId) {
        this._typeFkId = typeFkId;
    }

}
