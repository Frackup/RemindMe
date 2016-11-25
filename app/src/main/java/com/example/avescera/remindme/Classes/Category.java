package com.example.avescera.remindme.Classes;

/**
 * Created by a.vescera on 25/11/2016.
 */

public class Category {
    private int _id;
    private String _category;

    public Category(int id, String category) {
        this._id = id;
        this._category = category;
    }

    //Getters
    public int get_id() {
        return _id;
    }

    public String get_category() {
        return _category;
    }

    //Setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_category(String _category) {
        this._category = _category;
    }
}
