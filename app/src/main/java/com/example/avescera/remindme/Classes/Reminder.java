package com.example.avescera.remindme.Classes;

/**
 * Created by a.vescera on 20/12/2016.
 */

public class Reminder {

    private int _id;
    private String _description;
    private boolean _active;
    private int _hour;
    private int _minute;
    private int _duration;

    public Reminder(int id, String description, boolean active, int hour, int minute, int duration){
        this._id = id;
        this._description = description;
        this._active = active;
        this._hour = hour;
        this._minute = minute;
        this._duration = duration;
    }

    //Getters

    public int get_id() {
        return _id;
    }

    public String get_description() {
        return _description;
    }

    public boolean is_active() {
        return _active;
    }

    public int get_hour() {
        return _hour;
    }

    public int get_minute() {
        return _minute;
    }

    public int get_duration() {
        return _duration;
    }

    //Setters

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_active(boolean _active) {
        this._active = _active;
    }

    public void set_hour(int _hour) {
        this._hour = _hour;
    }

    public void set_minute(int _minute) {
        this._minute = _minute;
    }

    public void set_duration(int _duration) {
        this._duration = _duration;
    }
}
