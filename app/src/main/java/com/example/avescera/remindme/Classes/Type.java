package com.example.avescera.remindme.Classes;

import java.io.Serializable;

/**
 * Created by Frackup on 25/11/2016.
 * This class details the item Type, defining whether an exchange is a loan or a borrow.
 */

public class Type implements Serializable {

    private final int _id;
    private final String _type;

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

}
