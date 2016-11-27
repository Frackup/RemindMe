package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseTypeHandler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoneyCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseMoneyHandler dbMoneyHandler;
    private DatabaseContactHandler dbContactHandler;
    private DatabaseTypeHandler dbTypeHandler;

    final Context context = this;

    private EditText moneyTitle;
    private Spinner typesSpinner;
    private EditText moneyAmount;
    private Spinner contactsSpinner;
    private EditText moneyDate;
    private EditText moneyDetails;

    private DateFormat dateFormat;
    List<Contact> listContacts;
    private Contact selectedContact;
    List<Type> listTypes;
    private Type selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Gathering all the input of the xml part.
        moneyTitle = (EditText) findViewById(R.id.editTxtMoneyCreationTitle);
        typesSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationType);
        moneyAmount = (EditText) findViewById(R.id.editTxtMoneyCreationAmount);
        contactsSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationContact);
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

        dbTypeHandler = new DatabaseTypeHandler(this);
        try {
            dbTypeHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //TODO : Implémenter le DatePicker pour remplir la date
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        Date date = new Date();
        moneyDate.setText(dateFormat.format(date));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMoney(view);
            }
        });

        listContacts = dbContactHandler.getAllContacts();
        listTypes = dbTypeHandler.getAllTypes();

        contactsSpinner.setOnItemSelectedListener(this);
        typesSpinner.setOnItemSelectedListener(this);

        ArrayAdapter contactSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);

        ArrayAdapter typeSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerMoneyCreationContact) {
            selectedContact = (Contact) contactsSpinner.getSelectedItem();
        } else if (spinner.getId() == R.id.spinnerMoneyCreationType) {
            selectedType = (Type) typesSpinner.getSelectedItem();
        }
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
        if(moneyTitle.getText().toString().isEmpty() || moneyAmount.getText().toString().isEmpty()){
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
                        selectedType.get_id(),
                        selectedContact.get_id(),
                        null);

                dbMoneyHandler.createMoney(money);

                //Reset all fields
                moneyTitle.setText("");
                moneyAmount.setText("");
                moneyDetails.setText("");
                typesSpinner.setSelection(0);
                contactsSpinner.setSelection(0);

                Toast.makeText(getApplicationContext(), R.string.added_item, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
