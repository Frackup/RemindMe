package com.example.avescera.remindme.Classes;

import java.io.Serializable;

/**
 * Created by a.vescera on 22/11/2016.
 * This class details the item Contact, dealing with the Contacts to whom the user will loan or borrow objects or money.
 */

public class Contact implements Serializable {
    private int _id;
    private String _firstName;
    private String _lastName;
    private String _phone;
    private String _email;

    public Contact(int id, String firstName, String lastName, String phone, String email) {
        this._id = id;
        this._firstName = firstName;
        this._lastName = lastName;
        this._phone = phone;
        this._email = email;
    }

    public String toString() {
        return (this._firstName + " " + this._lastName);
    }

    //Getters
    public int get_id() { return this._id; }

    public String get_firstName(){ return this._firstName; }

    public String get_lastName(){ return this._lastName; }

    public String get_phone(){ return this._phone; }

    public String get_email(){ return this._email; }

    public void set_firstName(String firstName){ this._firstName = firstName; }

    public void set_lastName(String lastName){ this._lastName = lastName; }

    public void set_phone(String phone){ this._phone = phone; }

    public void set_email(String email){ this._email = email; }
}
