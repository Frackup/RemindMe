package com.example.avescera.remindme.Classes;

import java.io.Serializable;
import java.util.Date;

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

    public Object(int id, String title, int quantity, String details, Date date, int categoryFkId, int typeFkId, int contactFkId, Integer reminderFkId) {
        this._id = id;
        this._title = title;
        this._quantity = quantity;
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
}
