package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseTypeHandler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ObjectCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePFragment.OnDatePickedListener {

    private DatabaseObjectHandler dbObjectHandler;
    private DatabaseContactHandler dbContactHandler;
    private DatabaseCategoryHandler dbCategoryHandler;
    private DatabaseTypeHandler dbTypeHandler;

    final Context context = this;

    private EditText objectTitle;
    private Spinner typesSpinner;
    private EditText objectQty;
    private Spinner categoriesSpinner;
    private Spinner contactsSpinner;
    private EditText objectDate;
    private EditText objectDetails;

    private Contact selectedContact;
    private Type selectedType;
    private Category selectedCategory;

    private DateFormat dateFormat;
    private DateFormat builtDateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    private Calendar cal;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initiate variables
        objectTitle = (EditText) findViewById(R.id.editTxtObjectCreationTitle);
        typesSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationType);
        objectQty = (EditText) findViewById(R.id.editTxtObjectCreationQty);
        categoriesSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationCateroy);
        contactsSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationContact);
        objectDate = (EditText) findViewById(R.id.editTxtObjectCreationDate);
        objectDetails = (EditText) findViewById(R.id.editTxtObjectCreationDetails);

        cal = Calendar.getInstance();

        //Initiate the DBHandlers
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbCategoryHandler = new DatabaseCategoryHandler(this);
        try {
            dbCategoryHandler.open();
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
        objectDate.setText(dateFormat.format(date));
        objectQty.setText("1");

        List<Contact> contactsList = dbContactHandler.getAllContacts();
        List<Type> typesList = dbTypeHandler.getAllTypes();
        List<Category> categoriesList = dbCategoryHandler.getAllCategories();

        ArrayAdapter contactSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, contactsList);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);

        ArrayAdapter typeSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, typesList);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);

        ArrayAdapter categorySpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList);
        categoriesSpinner.setAdapter(categorySpinnerArrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveObject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createObject(view);
            }
        });

        // Implementing the detection of a click on the date selection line (to display the DatePicker)
        objectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Show Dialog Fragment
                showDatePickerDialog(v.getId());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerMoneyCreationContact) {
            selectedContact = (Contact) contactsSpinner.getSelectedItem();
        } else if (spinner.getId() == R.id.spinnerMoneyCreationType) {
            selectedType = (Type) typesSpinner.getSelectedItem();
        } else if (spinner.getId() == R.id.spinnerObjectCreationCateroy) {
            selectedCategory = (Category) categoriesSpinner.getSelectedItem();
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

    public void createObject(View view) {
        // Check if all the necessary data have been filled, return an alert instead.
        if(objectTitle.getText().toString().isEmpty() || contactsSpinner.getSelectedItemPosition() == 0){
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
                Object object = new Object(dbObjectHandler.getObjectsCount(),
                        objectTitle.getText().toString(),
                        Integer.parseInt(objectQty.getText().toString()),
                        objectDetails.getText().toString(),
                        dateFormat.parse(objectDate.getText().toString()),
                        selectedCategory.get_id(),
                        selectedType.get_id(),
                        selectedContact.get_id(),
                        null);

                dbObjectHandler.createObject(object);
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
        objectDate.setText(dateFormat.format(intermediateDate));
    }
}
