package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.ContactAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;

import java.sql.SQLException;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView contactsListView;
    private InitDataBaseHandlers dbHandlers;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandlers = new InitDataBaseHandlers(this);
        attachViewItems();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactCreation();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attachViewItems(){
        contactsListView =  (ListView) findViewById(R.id.listViewContacts);
    }

    private void populateListView(){
        List<Contact> contactsList = dbHandlers.getDbContactHandler().getAllContacts();
        //Delete the 2 first items which are the empty contact (for display purposes) and the "add a contact" contact, to link to the contact pop-up.
        //As not anymore part of the list, they will not be displayed on the screen.
        contactsList.remove(1);
        contactsList.remove(0);

        adapter =  new ContactAdapter(this, contactsList);
        contactsListView.setAdapter(adapter);
    }

    public void goToContactCreation(){
        Intent intent = new Intent(this, ContactCreationActivity.class);
        startActivity(intent);
    }

    //The onResume method to refresh the listview with the newly created contact.
    @Override
    protected void onResume() {
        super.onResume();

        dbHandlers = new InitDataBaseHandlers(this);
        populateListView();
    }

}
