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
import android.widget.EditText;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;

import java.sql.SQLException;
import java.text.ParseException;

public class ContactCreationActivity extends AppCompatActivity {

    private DatabaseContactHandler dbContactHandler;
    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initiate the DBHandlers
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        contactFName = (EditText) findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) findViewById(R.id.edit_txt_contact_creation_email);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContact(view);
            }
        });
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

    public void createContact(View view) {
        // Check if all the necessary data have been filled, return an alert instead.
        if(contactFName.getText().toString().isEmpty() || contactLName.getText().toString().isEmpty()
                || contactPhone.getText().toString().isEmpty() || contactEmail.getText().toString().isEmpty()){
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
                Contact contact = new Contact(dbContactHandler.getContactsCount(),
                        contactFName.getText().toString(),
                        contactLName.getText().toString(),
                        contactPhone.getText().toString(),
                        contactEmail.getText().toString());

                dbContactHandler.createContact(contact);

                //Reset all fields
                contactFName.setText("");
                contactLName.setText("");
                contactPhone.setText("");
                contactEmail.setText("");

                Toast.makeText(getApplicationContext(), R.string.added_contact, Toast.LENGTH_SHORT).show();

        }
    }

}
