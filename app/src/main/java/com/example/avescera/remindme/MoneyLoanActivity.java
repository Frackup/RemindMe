package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoneyLoanActivity extends AppCompatActivity {

    private ListView listViewMoneyLoan;
    private DatabaseMoneyHandler dbMoneyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_loan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewMoneyLoan = (ListView) findViewById(R.id.listViewMoneyLoanItems);

        //Initiate the DBHandler
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Money> listMoneyItems = dbMoneyHandler.getAllMoneys();

        MoneyAdapter adapter = new MoneyAdapter(MoneyLoanActivity.this, R.layout.money_list_item, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMoneyCreationPage(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goToMoneyCreationPage(View view) {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Initiate the DBHandler
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Money> listMoneyItems = dbMoneyHandler.getAllMoneys();

        MoneyAdapter adapter = new MoneyAdapter(MoneyLoanActivity.this, R.layout.money_list_item, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);
    }

}
