package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class MoneyListActivity extends AppCompatActivity {

    private ListView listViewMoneyLoan;
    private DatabaseMoneyHandler dbMoneyHandler;
    private DatabaseContactHandler dbContactHandler;
    private String listFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initDbHandlers();
        initVariables();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMoneyCreationPage();
            }
        });
    }

    private void initDbHandlers(){
        //Initiate the DBHandler
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        listViewMoneyLoan = (ListView) findViewById(R.id.listViewMoneyLoanItems);
    }

    private void populateListView(){
        List<Money> listMoneyItems = null;
        if(listFilter != null) {
            Contact contact = (Contact) getIntent().getSerializableExtra(ActivityClass.CONTACT_ITEM);
            if (getIntent().getSerializableExtra(ActivityClass.CONTACT_ITEM) != null) {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listMoneyItems = dbMoneyHandler.getContactTypeMoneys(contact.get_id(), ActivityClass.DATABASE_LOAN_TYPE);
                } else {
                    listMoneyItems = dbMoneyHandler.getContactTypeMoneys(contact.get_id(), ActivityClass.DATABASE_BORROW_TYPE);
                }

            } else {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listMoneyItems = dbMoneyHandler.getTypeMoneys(ActivityClass.DATABASE_LOAN_TYPE);
                } else {
                    listMoneyItems = dbMoneyHandler.getTypeMoneys(ActivityClass.DATABASE_BORROW_TYPE);
                }
            }
        }

        MoneyAdapter adapter = new MoneyAdapter(MoneyListActivity.this, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);
    }

    private void initVariables(){
        if(getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null) {
            listFilter = getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY);
            if(listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                setTitle(R.string.title_activity_money_loan);
            } else {
                setTitle(R.string.title_activity_money_borrow);
            }
        }
    }

    public void goToMoneyCreationPage() {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
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
