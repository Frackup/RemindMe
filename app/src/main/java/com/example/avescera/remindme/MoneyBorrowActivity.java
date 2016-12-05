package com.example.avescera.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class MoneyBorrowActivity extends AppCompatActivity {

    private ListView listViewMoneyBorrowed;
    private DatabaseMoneyHandler dbMoneyHandler;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initDbHandlers();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateBorrowedMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMoneyCreationPage(view);
            }
        });
    }

    private void initDbHandlers() {
        //Initiate the DBHandler
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        listViewMoneyBorrowed = (ListView) findViewById(R.id.listViewMoneyBorrowedItems);
    }

    private void populateListView(){
        List<Money> moneysList = dbMoneyHandler.getTypeMoneys(ActivityClass.DATABASE_BORROW_TYPE);
        MoneyAdapter adapter = new MoneyAdapter(MoneyBorrowActivity.this, moneysList);
        listViewMoneyBorrowed.setAdapter(adapter);
    }

    public void goToMoneyCreationPage(View view) {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        populateListView();
    }
}
