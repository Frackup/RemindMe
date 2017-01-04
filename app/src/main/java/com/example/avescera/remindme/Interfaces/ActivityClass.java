package com.example.avescera.remindme.Interfaces;

/**
 * Created by a.vescera on 30/11/2016.
 * Allow to keep standard information through all the app.
 */

public interface ActivityClass {
    String CALLING_ACTIVITY = "Calling-Activity";

    String MONEY_ITEM = "MoneyItem";
    String OBJECT_ITEM = "ObjectItem";
    String CONTACT_ITEM = "ContactItem";
    String CATEGORY_ITEM = "CategoryItem";

    String ACTIVITY_LOAN = "LOAN";
    String ACTIVITY_BORROW = "BORROW";

    int SPINNER_EMPTY_CONTACT = 0;
    int SPINNER_ADD_CONTACT = 1;
    int SPINNER_ADD_CATEGORY = 1;
    int SPINNER_FIRST_CATEGORY = 0;
    int SPINNER_LOAN_TYPE = 0;
    int SPINNER_BORROW_TYPE = 1;

    int DATABASE_LOAN_TYPE = 1;
    int DATABASE_BORROW_TYPE = 2;

    int OBJECT_DEFAULT_QTY = 1;

    String FILTER_SPEC_ASC = "Spec_Asc";
    String FILTER_SPEC_DESC = "Spec_Desc";
    String FILTER_DATE_ASC = "Date_Asc";
    String FILTER_DATE_DESC = "Date_Desc";

    String URGENT_REMINDER = "URGENT_REMINDER";
    String TGT_DATE_REMINDER_0 = "TGT_DATE_REMINDER_0";
    String TGT_DATE_REMINDER_1 = "TGT_DATE_REMINDER_1";
    String TGT_DATE_REMINDER_2 = "TGT_DATE_REMINDER_2";
}
