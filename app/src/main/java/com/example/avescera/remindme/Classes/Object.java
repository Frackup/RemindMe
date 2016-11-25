package com.example.avescera.remindme.Classes;

import java.util.Date;

/**
 * Created by a.vescera on 24/11/2016.
 */

public class Object {

    private int _id;
    private String _title;
    private int _number;
    private String _details;
    private Date _date;
    private int _categoryFkId;
    private int _typeFkId;
    private int _contactFkId;
    private Integer _reminderFkId;

    public Object(int id, String title, int number, String details, Date date, int categoryFkId, int typeFkId, int contactFkId) {
        this._id = id;
        this._title = title;
        this._number = number;
        this._details = details;
        this._date = date;
        this._categoryFkId = categoryFkId;
        this._typeFkId = typeFkId;
        this._contactFkId = contactFkId;
    }

    public Object(int id, String title, int number, String details, Date date, int categoryFkId, int typeFkId, int contactFkId, Integer reminderFkId) {
        this._id = id;
        this._title = title;
        this._number = number;
        this._details = details;
        this._date = date;
        this._categoryFkId = categoryFkId;
        this._typeFkId = typeFkId;
        this._contactFkId = contactFkId;
        this._reminderFkId = reminderFkId;
    }

    //Getters

    public int get_id() {
        return this._id;
    }

    public String get_title() {
        return this._title;
    }

    public int get_number() {
        return this._number;
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

    //Setters

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_number(int _number) {
        this._number = _number;
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
}
