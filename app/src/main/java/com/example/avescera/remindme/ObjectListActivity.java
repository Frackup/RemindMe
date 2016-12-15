package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.ObjectAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class ObjectListActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private ListView listViewObjectLoan;
    private String listFilter = null;
    private int category_id;
    private int contact_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachViewItems();
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

    private void attachViewItems(){
        listViewObjectLoan = (ListView) findViewById(R.id.listViewObjectLoanItems);
    }

    private void populateListView(){
        List<Object> listObjectItems = null;

        if(listFilter != null) {
            if (contact_id != 0 && contact_id != 1) {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
                        listObjectItems = dbHandlers.getDbObjectHandler().getContactCatTypeObjects(contact_id, category_id, ActivityClass.DATABASE_LOAN_TYPE);
                    } else {
                        listObjectItems = dbHandlers.getDbObjectHandler().getContactTypeObjects(contact_id, ActivityClass.DATABASE_LOAN_TYPE);
                    }
                } else {
                    if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
                        listObjectItems = dbHandlers.getDbObjectHandler().getContactCatTypeObjects(contact_id, category_id, ActivityClass.DATABASE_BORROW_TYPE);
                    } else {
                        listObjectItems = dbHandlers.getDbObjectHandler().getContactTypeObjects(contact_id, ActivityClass.DATABASE_BORROW_TYPE);
                    }
                }
            } else {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
                        listObjectItems = dbHandlers.getDbObjectHandler().getCategoryTypeObjects(category_id, ActivityClass.DATABASE_LOAN_TYPE);
                    } else {
                        listObjectItems = dbHandlers.getDbObjectHandler().getTypeObjects(ActivityClass.DATABASE_LOAN_TYPE);
                    }
                } else {
                    if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
                        listObjectItems = dbHandlers.getDbObjectHandler().getCategoryTypeObjects(category_id, ActivityClass.DATABASE_BORROW_TYPE);
                    } else {
                        listObjectItems = dbHandlers.getDbObjectHandler().getTypeObjects(ActivityClass.DATABASE_BORROW_TYPE);
                    }
                }
            }
        }

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectItems);
        listViewObjectLoan.setAdapter(adapter);
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        if(getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null) {
            listFilter = getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY);
            if(listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                setTitle(R.string.title_activity_object_loan);
            } else {
                setTitle(R.string.title_activity_object_borrow);
            }
        }
        contact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0);
        category_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToObjectCreationPage(View view) {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, listFilter);
        if(category_id != 0) {
            intent.putExtra(ActivityClass.CATEGORY_ITEM, category_id);
        }
        if (contact_id != 0) {
            intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHandlers = new InitDataBaseHandlers(this);
        populateListView();
    }
}
