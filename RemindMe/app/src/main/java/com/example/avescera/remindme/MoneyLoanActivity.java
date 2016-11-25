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

public class MoneyLoanActivity extends AppCompatActivity {

    private ListView listViewMoneyLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_loan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewMoneyLoan = (ListView) findViewById(R.id.listViewMoneyLoanItems);

        //TODO : à remplacer plus tard par la complétion via données en BDD.
        //TODO : tester aussi ce que donne l'acitivté sans données à afficher.

        //List<Money> listMoneyItems = ;

        MoneyAdapter adapter = new MoneyAdapter(MoneyLoanActivity.this, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("New Adding", null).show();
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
