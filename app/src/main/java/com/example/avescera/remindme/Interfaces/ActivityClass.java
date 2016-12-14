package com.example.avescera.remindme.Interfaces;

/**
 * Created by a.vescera on 30/11/2016.
 */

public interface ActivityClass {
    public static final String CALLING_ACTIVITY = "Calling-Activity";

    public static final String MONEY_ITEM = "MoneyItem";
    public static final String OBJECT_ITEM = "ObjectItem";
    public static final String CONTACT_ITEM = "ContactItem";
    public static final String CATEGORY_ITEM = "CategoryItem";

    public static final String ACTIVITY_LOAN = "LOAN";
    public static final String ACTIVITY_BORROW = "BORROW";

    public static final int SPINNER_EMPTY_CONTACT = 0;
    public static final int SPINNER_ADD_CONTACT = 1;
    public static final int SPINNER_ADD_CATEGORY = 1;
    public static final int SPINNER_FIRST_CATEGORY = 0;
    public static final int SPINNER_LOAN_TYPE = 0;
    public static final int SPINNER_BORROW_TYPE = 1;

    public static final int DATABASE_LOAN_TYPE = 1;
    public static final int DATABASE_BORROW_TYPE = 2;
    public static final int DATABASE_GRAPH_DATE_LIMIT = 6;

    public static final int OBJECT_DEFAULT_QTY = 1;
}
