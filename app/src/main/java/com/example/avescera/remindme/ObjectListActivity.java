package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.ObjectAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class ObjectListActivity extends AppCompatActivity {

    private DatabaseObjectHandler dbObjectHandler;
    private DatabaseContactHandler dbContactHandler;
    private ListView listViewObjectLoan;
    private String listFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachViewItems();
        initDbHandlers();
        initVariables();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanObject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToObjectCreationPage(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDbHandlers(){
        //Initiate the DBHandler
        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        listViewObjectLoan = (ListView) findViewById(R.id.listViewObjectLoanItems);
    }

    private void populateListView(){
        List<Object> listObjectItems = null;

        if(listFilter != null) {
            Contact contact = (Contact) getIntent().getSerializableExtra(ActivityClass.CONTACT_ITEM);
            if (getIntent().getSerializableExtra(ActivityClass.CONTACT_ITEM) != null) {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listObjectItems = dbObjectHandler.getContactTypeObjects(contact.get_id(), ActivityClass.DATABASE_LOAN_TYPE);
                } else {
                    listObjectItems = dbObjectHandler.getContactTypeObjects(contact.get_id(), ActivityClass.DATABASE_BORROW_TYPE);
                }

            } else {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listObjectItems = dbObjectHandler.getTypeObjects(ActivityClass.DATABASE_LOAN_TYPE);
                } else {
                    listObjectItems = dbObjectHandler.getTypeObjects(ActivityClass.DATABASE_BORROW_TYPE);
                }
            }
        }

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectItems);
        listViewObjectLoan.setAdapter(adapter);
    }

    private void initVariables(){
        if(getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null) {
            listFilter = getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY);
            if(listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                setTitle(R.string.title_activity_object_loan);
            } else {
                setTitle(R.string.title_activity_object_borrow);
            }
        }
    }

    public void goToObjectCreationPage(View view) {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, listFilter);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        populateListView();
    }
}