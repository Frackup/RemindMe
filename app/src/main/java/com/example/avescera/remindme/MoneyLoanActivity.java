package com.example.avescera.remindme;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initDbHandlers();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMoneyCreationPage(view);
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
    }

    private void attachViewItems(){
        listViewMoneyLoan = (ListView) findViewById(R.id.listViewMoneyLoanItems);
    }

    private void populateListView(){
        List<Money> listMoneyItems = dbMoneyHandler.getTypeMoneys(ActivityClass.DATABASE_LOAN_TYPE);
        MoneyAdapter adapter = new MoneyAdapter(MoneyLoanActivity.this, R.layout.money_list_item, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);
    }

    public void goToMoneyCreationPage(View view) {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        populateListView();
    }

}
