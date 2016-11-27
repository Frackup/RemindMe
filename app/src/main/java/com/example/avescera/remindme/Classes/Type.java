package com.example.avescera.remindme.Classes;

import android.widget.ArrayAdapter;

/**
 * Created by Frackup on 25/11/2016.
 */

public class Type {

    private int _id;
    private String _type;

    public Type(int id, String type) {
        this._id = id;
        this._type = type;
    }

    public String toString() {
        return (this._type);
    }

    //Getters
    public int get_id() {
        return _id;
    }

    public String get_type() {
        return _type;
    }

    //Setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_type(String _type) {
        this._type = _type;
    }
}
