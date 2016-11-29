package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.ContactAdapter;
import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;

import java.sql.SQLException;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView contactsListView;
    private DatabaseContactHandler dbContactHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsListView =  (ListView) findViewById(R.id.listViewContacts);

        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Contact> contactsList = dbContactHandler.getAllContacts();
        //Delete the 2 first items which are the empty contact (for display purposes) and the "add a contact" contact, to link to the contact pop-up.
        //As not anymore part of the list, they will not be displayed on the screen.
        contactsList.remove(1);
        contactsList.remove(0);

        ContactAdapter adapter =  new ContactAdapter(this, R.layout.contact_list_item, contactsList);
        contactsListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactCreation();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goToContactCreation(){
        Intent intent = new Intent(this, ContactCreationActivity.class);
        startActivity(intent);
    }

    //The onResume method to refresh the listview with the newly created contact.
    @Override
    protected void onResume() {
        super.onResume();

        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Contact> contactsList = dbContactHandler.getAllContacts();
        //Delete the 2 first items which are the empty contact (for display purposes) and the "add a contact" contact, to link to the contact pop-up.
        //As not anymore part of the list, they will not be displayed on the screen.
        contactsList.remove(1);
        contactsList.remove(0);

        ContactAdapter adapter =  new ContactAdapter(this, R.layout.contact_list_item, contactsList);
        contactsListView.setAdapter(adapter);
    }

}
