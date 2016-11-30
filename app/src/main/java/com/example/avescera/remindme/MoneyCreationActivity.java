package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoneyCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePFragment.OnDatePickedListener, Dialog.OnDismissListener {

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
    private DateFormat builtDateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    private List<Contact> listContacts;
    private Contact selectedContact;
    private List<Type> listTypes;
    private Type selectedType;
    private Date date;

    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;

    private ArrayAdapter contactSpinnerArrayAdapter;
    private ArrayAdapter typeSpinnerArrayAdapter;

    private Calendar cal;
    private Dialog dialog;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        initDbHandlers();
        populateSpinner();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                initDbHandlers();
                populateSpinner();
                //select as default contact the newly created one.
                contactsSpinner.setSelection(dbContactHandler.getContactsCount()-1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveMoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMoney(view);
            }
        });

        // Implementing the detection of a click on the date selection line (to display the DatePicker)
        moneyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Show Dialog Fragment
                showDatePickerDialog(v.getId());
            }
        });
    }

    private void initVariables(){
        dialog = new Dialog(context);
        cal = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        date = new Date();
        moneyDate.setText(dateFormat.format(date));
    }

    private void initDbHandlers() {
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
    }

    private void attachViewItems(){
        //Gathering all the input of the xml part.
        moneyTitle = (EditText) findViewById(R.id.editTxtMoneyCreationTitle);
        typesSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationType);
        moneyAmount = (EditText) findViewById(R.id.editTxtMoneyCreationAmount);
        contactsSpinner = (Spinner) findViewById(R.id.spinnerMoneyCreationContact);
        moneyDate = (EditText) findViewById(R.id.editTxtMoneyCreationDate);
        moneyDate.setFocusable(false);
        moneyDetails = (EditText) findViewById(R.id.editTxtMoneyCreationDetails);
    }

    private void populateSpinner(){
        listContacts = dbContactHandler.getAllContacts();
        contactsSpinner.setOnItemSelectedListener(this);
        contactSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);

        listTypes = dbTypeHandler.getAllTypes();
        typesSpinner.setOnItemSelectedListener(this);
        typeSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);

        if(getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_LOAN)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        } else if (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_BORROW)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_BORROW_TYPE);
        } else {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        //Fragment dialog had been dismissed
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerMoneyCreationContact) {
            selectedContact = (Contact) contactsSpinner.getSelectedItem();

            if (contactsSpinner.getSelectedItemPosition() == ActivityClass.SPINNER_ADD_CONTACT) {
                createContactDialog();
            }

        } else if (spinner.getId() == R.id.spinnerMoneyCreationType) {
            selectedType = (Type) typesSpinner.getSelectedItem();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Auto-generated method stub
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
        if(moneyTitle.getText().toString().isEmpty() || moneyAmount.getText().toString().isEmpty()
                || contactsSpinner.getSelectedItemPosition() == ActivityClass.SPINNER_EMPTY_CONTACT){
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

    // Methods to display the calendar to pick a date.
    public void showDatePickerDialog(int layoutId) {
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = createDatePickerBundle(layoutId, year, month, day);
        DialogFragment newFragment = new DatePFragment();
        newFragment.setArguments(bundle);
        newFragment.show(fm, Integer.toString(layoutId));
    }

    public Bundle createDatePickerBundle(int layoutId, int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        return bundle;
    }

    // This is used when a date is selected into the DatePicker widget
    @Override
    public void onDatePicked(int LayoutId, int year, int month, int day){
        // Gathering the value to then be used within the money object.
        String date;
        Date intermediateDate = new Date();

        // Building the date to be displayed (as int are used, check if they're on 1 or 2 digit(s) to add a 0 before).
        if (String.valueOf(day).length() == 1) {
            date = "0" + day + "/";
        } else {
            date = day + "/";
        }

        if (String.valueOf(month).length() == 1) {
            date += "0" + (month+1) + "/" + year;
        } else {
            date += (month+1) + "/" + year;
        }

        // Displaying the date in the EditText box.
        try {
            intermediateDate = builtDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        moneyDate.setText(dateFormat.format(intermediateDate));
    }

    public void createContactDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.activity_contact_creation);
        dialog.setTitle(getResources().getString(R.string.title_activity_contact_creation).toString());

        //set the custom dialog component
        contactFName = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_email);
        FloatingActionButton fabContact = (FloatingActionButton) dialog.findViewById(R.id.fabSaveContact);

        fabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContact(dialog);
            }
        });

        dialog.show();
    }

    public void createContact(Dialog dialog) {
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

            dialog.dismiss();
        }
    }
}
