package com.example.avescera.remindme.Classes;

/**
 * Created by a.vescera on 22/11/2016.
 */

public class Contact {
    private String _firstName;
    private String _lastName;
    private String _phone;
    private String _email;

    public Contact(String firstName, String lastName, String phone, String email) {
        this._firstName = firstName;
        this._lastName = lastName;
        this._phone = phone;
        this._email = email;
    }

    //Getters
    public String get_firstName(){
        return this._firstName;
    }

    public String get_lastName(){
        return this._lastName;
    }

    public String get_phone(){
        return this._phone;
    }

    public String get_email(){
        return this._email;
    }

    //Setters
    public void set_firstName(String firstName){
        this._firstName = firstName;
    }

    public void set_lastName(String lastName){
        this._lastName = lastName;
    }

    public void set_phone(String phone){
        this._phone = phone;
    }

    public void set_email(String email){
        this._email = email;
    }
}
