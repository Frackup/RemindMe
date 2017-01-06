package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.R;

/**
 * Created by a.vescera on 08/12/2016.
 * This class allows to initialize the applications data, filling it with some categories and the add part of the contact and category spinner.
 */

public class InitDataBase {

    public InitDataBase() {

    }

    public void initDB(Context context) {
        InitDataBaseHandlers dbHandlers = new InitDataBaseHandlers(context);

        //Adding the 5 first categories
        Category category0 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_0));
        dbHandlers.getDbCategoryHandler().createCategory(category0);
        Category category1 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_1));
        dbHandlers.getDbCategoryHandler().createCategory(category1);
        Category category2 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_2));
        dbHandlers.getDbCategoryHandler().createCategory(category2);
        Category category3 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_3));
        dbHandlers.getDbCategoryHandler().createCategory(category3);
        Category category4 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_4));
        dbHandlers.getDbCategoryHandler().createCategory(category4);
        Category category5 = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(), context.getResources().getString(R.string.categorie_5));
        dbHandlers.getDbCategoryHandler().createCategory(category5);

        //Adding the 2 types
        Type type1 = new Type(dbHandlers.getDbTypeHandler().getTypeNextId(), context.getResources().getString(R.string.type_loan));
        dbHandlers.getDbTypeHandler().createType(type1);
        Type type2 = new Type(dbHandlers.getDbTypeHandler().getTypeNextId(), context.getResources().getString(R.string.type_borrow));
        dbHandlers.getDbTypeHandler().createType(type2);

        //Adding the 2 contacts
        Contact baseContact1 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(), context.getResources().getString(R.string.select_contact_fname), "", "", "");
        dbHandlers.getDbContactHandler().createContact(baseContact1);
        Contact baseContact2 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(), context.getResources().getString(R.string.base_contact_fname), context.getResources().getString(R.string.base_contact_lname),
                context.getResources().getString(R.string.base_contact_phone), context.getResources().getString(R.string.base_contact_email));
        dbHandlers.getDbContactHandler().createContact(baseContact2);

        //Adding default reminders
        // Create the 3 reminders, the duration is expressed in minutes

        //First reminder is done XX  times after the creation of the exchange, so we set it to 1 week.
        Reminder rem1 = new Reminder(dbHandlers.getDbReminderHandler().getRemindersNextId(), "URGENT_REMINDER", true,168, 0, 10080);
        dbHandlers.getDbReminderHandler().createReminder(rem1);

        //The 2 other reminders allow to set reminders prior to a target date, set to 1h before and 1 day before the tgt date.
        Reminder rem2 = new Reminder(dbHandlers.getDbReminderHandler().getRemindersNextId(), "TGT_DATE_REMINDER_1", true, 1, 0, 60);
        dbHandlers.getDbReminderHandler().createReminder(rem2);
        Reminder rem3 = new Reminder(dbHandlers.getDbReminderHandler().getRemindersNextId(), "TGT_DATE_REMINDER_2", true, 24, 0, 1440);
        dbHandlers.getDbReminderHandler().createReminder(rem3);
    }
}
