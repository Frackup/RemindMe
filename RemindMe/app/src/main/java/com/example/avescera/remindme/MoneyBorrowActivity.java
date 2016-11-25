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

import java.util.ArrayList;
import java.util.List;

public class MoneyBorrowActivity extends AppCompatActivity {

    private ListView listViewMoneyBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewMoneyBorrowed = (ListView) findViewById(R.id.listViewMoneyBorrowedItems);

        //TODO : à remplacer plus tard par la complétion via données en BDD.

        //List<Money> listMoneyItems = ;

        MoneyAdapter adapter = new MoneyAdapter(MoneyBorrowActivity.this, listMoneyItems);
        listViewMoneyBorrowed.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateBorrowedMoney);
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
}
