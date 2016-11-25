package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.Adapters.ContactSpinnerAdapter;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MoneyCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner contactsSpinner;
    private DatabaseMoneyHandler dbMoneyHandler;
    private DatabaseContactHandler dbContactHandler;

    final Context context = this;

    private EditText moneyTitle;
    private Spinner moneyType;
    private EditText moneyAmount;
    private Spinner moneyContact;
    private EditText moneyDate;
    private EditText moneyDetails;

    private DateFormat dateFormat;

    //TODO : Données de test pour le spinner à supprimer après connexion à la base.
    String[] contactNames={"Alexandre VESCERA","Clarisse VESCERA","Anna VESCERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Gathering all the input of the xml part.
        moneyTitle = (EditText) findViewById(R.id.editTxtMoneyCreationTitle);
        moneyType = (Spinner) findViewById(R.id.spinnerMoneyCreationType);
        moneyAmount = (EditText) findViewById(R.id.editTxtMoneyCreationAmount);
        moneyContact = (Spinner) findViewById(R.id.spinnerMoneyCreationContact);
        moneyDate = (EditText) findViewById(R.id.editTxtMoneyCreationDate);
        moneyDetails = (EditText) findViewById(R.id.editTxtMoneyCreationDetails);

        //Initiate the DBHandlers
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbMoneyHandler = new DatabaseMoneyHandler(this);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMoney(view);
            }
        });

        //TODO : Mise en place du remplissage du spinner.
        List<Contact> listContacts = dbContactHandler.getAllContacts();

        contactsSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationContact);
        contactsSpinner.setOnItemSelectedListener(this);

        ContactSpinnerAdapter contactSpinnerAdapter = new ContactSpinnerAdapter(MoneyCreationActivity.this, listContacts);
        contactsSpinner.setAdapter(contactSpinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), contactNames[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
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

    public void createMoney(View view) {
        // Check if all the necessary data have been filled, return an alert instead.
        if(true){
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    // Set Dialog Icon
                    .setIcon(R.drawable.ic_bullet_key_permission)
                    // Set Dialog Title
                    .setTitle(R.string.incomplete_data)
                    // Set Dialog Message
                    .setMessage(R.string.error_message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    }).create();

            alertDialog.show();
        } else {
            try {
                Money money = new Money(dbMoneyHandler.getMoneysCount(),
                        moneyTitle.getText().toString(),
                        Float.parseFloat(moneyAmount.getText().toString()),
                        moneyDetails.getText().toString(),
                        dateFormat.parse(moneyDate.getText().toString()),
                        Integer.parseInt(moneyType.getSelectedItem().toString()),
                        Integer.parseInt(moneyContact.getSelectedItem().toString()),
                        null);

                dbMoneyHandler.createMoney(money);
                Toast.makeText(getApplicationContext(), R.string.added_money, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
