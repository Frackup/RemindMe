package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;

public class ContactExchangeActivity extends AppCompatActivity {

    private DatabaseMoneyHandler dbMoneyHandler;
    private DatabaseObjectHandler dbObjectHandler;

    private Contact contact;

    private Button btnMoneyLoan;
    private Button btnObjectLoan;
    private Button btnMoneyBorrowed;
    private Button btnObjectBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_exchange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachView();
        initDbHandlers();
        initVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabContactExchangeCreation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogItemCreation();
            }
        });

        btnMoneyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityListPage(ActivityClass.ACTIVITY_LOAN, MoneyListActivity.class);
            }
        });

        btnMoneyBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityListPage(ActivityClass.ACTIVITY_BORROW, MoneyListActivity.class);
            }
        });

        btnObjectLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityListPage(ActivityClass.ACTIVITY_LOAN, ObjectListActivity.class);
            }
        });

        btnObjectBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityListPage(ActivityClass.ACTIVITY_BORROW, ObjectListActivity.class);
            }
        });
    }

    private void attachView(){
        btnMoneyLoan = (Button) findViewById(R.id.btnMoneyLoan);
        btnMoneyBorrowed = (Button) findViewById(R.id.btnMoneyBorrow);
        btnObjectLoan = (Button) findViewById(R.id.btnObjectLoan);
        btnObjectBorrowed =(Button) findViewById(R.id.btnObjectBorrow);
    }

    private void initDbHandlers(){
        //Initiate the DBHandlers
        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initVariables(){
        contact =  (Contact) getIntent().getSerializableExtra(ActivityClass.CONTACT_ITEM);
        btnMoneyLoan.setText(dbMoneyHandler.getTotalAmountByTypeAndContact(contact.get_id(), ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_currency));
        btnMoneyBorrowed.setText(dbMoneyHandler.getTotalAmountByTypeAndContact(contact.get_id(), ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_currency));
        btnObjectLoan.setText(dbObjectHandler.getTotalQtyByTypeAndContact(contact.get_id(), ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
        btnObjectBorrowed.setText(dbObjectHandler.getTotalQtyByTypeAndContact(contact.get_id(), ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
        setTitle(contact.get_firstName() + " " + contact.get_lastName());
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

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        initVariables();
    }

    private void goToActivityListPage(String type, Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, type);
        intent.putExtra(ActivityClass.CONTACT_ITEM, contact);
        startActivity(intent);
    }

    private void dialogItemCreation(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Set Dialog Icon
                .setIcon(android.R.drawable.ic_menu_edit)
                // Set Dialog Title
                .setTitle(R.string.home_item_creation_title)
                // Set Dialog Message
                .setMessage(R.string.home_item_creation)
                .setPositiveButton(R.string.home_item_creation_money, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MoneyCreationActivity.class);
                        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
                        intent.putExtra(ActivityClass.CONTACT_ITEM, contact.get_id());

                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.home_item_creation_object, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ObjectCreationActivity.class);
                        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
                        intent.putExtra(ActivityClass.CONTACT_ITEM, contact.get_id());

                        startActivity(intent);
                    }
                }).create();

        alertDialog.show();
    }
}
