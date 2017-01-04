package com.example.avescera.remindme.Classes;

import java.io.Serializable;

/**
 * Created by a.vescera on 25/11/2016.
 * This class details the item Category, dealing with the Categories an object can be.
 */

public class Category implements Serializable {
    private final int _id;
    private String _category;

    public Category(int id, String category) {
        this._id = id;
        this._category = category;
    }

    public String toString() {
        return (this._category);
    }

    //Getters
    public int get_id() {
        return _id;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }
}
