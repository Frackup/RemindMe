package com.example.avescera.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.avescera.remindme.Adapters.ContactSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoneyCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner contactsSpinner;

    //TODO : Données de test pour le spinner à supprimer après connexion à la base.
    String[] contactNames={"Alexandre VESCERA","Clarisse VESCERA","Anna VESCERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save Money to DataBase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //TODO : Mise en place du remplissage du spinner par données de tests, à supprimer une fois l'accès DB réalisé
        List<String> contactList = listContacts(contactNames);

        contactsSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationContact);
        contactsSpinner.setOnItemSelectedListener(this);

        ContactSpinnerAdapter contactSpinnerAdapter = new ContactSpinnerAdapter(MoneyCreationActivity.this, contactNames);
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

    //TODO : à supprimer lorsque l'on se connectera à la BDD.
    public List<String> listContacts(String[] contactNames){
        List<String> contacts = new ArrayList<String>();
        contacts.add(contactNames[0]);
        contacts.add(contactNames[1]);
        contacts.add(contactNames[2]);

        return contacts;
    }
}
