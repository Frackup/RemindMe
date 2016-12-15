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
import android.widget.EditText;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Interfaces.ActivityClass;

public class ContactCreationActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;

    private Context context = this;
    private Contact editedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContact(view);

                if(editedContact != null){
                    finish();
                }
            }
        });
    }

    public void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        if(getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0) != 0){
            int editedContact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 1);
            editedContact = dbHandlers.getDbContactHandler().getContact(editedContact_id);
            contactFName.setText(editedContact.get_firstName());
            contactLName.setText(editedContact.get_lastName());
            contactPhone.setText(editedContact.get_phone());
            contactEmail.setText(editedContact.get_email());
        }
    }

    private void attachViewItems(){
        contactFName = (EditText) findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) findViewById(R.id.edit_txt_contact_creation_email);
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
        } else if(editedContact == null) {
                Contact contact = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),
                        contactFName.getText().toString(),
                        contactLName.getText().toString(),
                        contactPhone.getText().toString(),
                        contactEmail.getText().toString());

                dbHandlers.getDbContactHandler().createContact(contact);

                //Reset all fields
                contactFName.setText("");
                contactLName.setText("");
                contactPhone.setText("");
                contactEmail.setText("");

                Toast.makeText(getApplicationContext(), R.string.added_contact, Toast.LENGTH_SHORT).show();

        } else {
            editedContact.set_firstName(contactFName.getText().toString());
            editedContact.set_lastName(contactLName.getText().toString());
            editedContact.set_phone(contactPhone.getText().toString());
            editedContact.set_email(contactEmail.getText().toString());

            dbHandlers.getDbContactHandler().updateContact(editedContact);

            //Reset all fields
            contactFName.setText("");
            contactLName.setText("");
            contactPhone.setText("");
            contactEmail.setText("");

            Toast.makeText(getApplicationContext(), R.string.updated_item, Toast.LENGTH_SHORT).show();
        }
    }

}
