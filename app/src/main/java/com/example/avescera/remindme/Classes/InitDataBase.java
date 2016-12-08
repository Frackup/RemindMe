package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.R;

/**
 * Created by a.vescera on 08/12/2016.
 */

public class InitDataBase {

    private InitDataBaseHandlers dbHandlers;

    public InitDataBase() {

    }

    public void initDB(Context context) {
        dbHandlers = new InitDataBaseHandlers(context);

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
    }
}
