package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class ObjectCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePFragment.OnDatePickedListener, Dialog.OnDismissListener {

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

    private DateFormat dateFormat;
    private DateFormat builtDateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    private List<Contact> listContacts;
    private Contact selectedContact;
    private List<Type> listTypes;
    private Type selectedType;
    private List<Category> listCategory;
    private Category selectedCategory;
    private Date date;

    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;
    private EditText categoryTitle;

    private ArrayAdapter contactSpinnerArrayAdapter;
    private ArrayAdapter typeSpinnerArrayAdapter;
    private ArrayAdapter categorySpinnerArrayAdapter;

    private int addContact = 2;
    private int emptyContact = 1;
    private int addCategory = 1;
    private int firstCategory = 2;

    private Dialog dialog;

    private Calendar cal;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(context);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Initiate the DBHandler
                dbContactHandler = new DatabaseContactHandler(context);
                try {
                    dbContactHandler.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                listContacts = dbContactHandler.getAllContacts();
                ArrayAdapter contactSpinnerArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listContacts);
                contactsSpinner.setAdapter(contactSpinnerArrayAdapter);

                dbCategoryHandler = new DatabaseCategoryHandler(context);
                try {
                    dbCategoryHandler.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                listCategory = dbCategoryHandler.getAllCategories();
                categorySpinnerArrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listCategory);
                categoriesSpinner.setAdapter(categorySpinnerArrayAdapter);
                categoriesSpinner.setSelection(firstCategory);
            }
        });

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

        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        Date date = new Date();
        objectDate.setText(dateFormat.format(date));
        objectQty.setText("1");

        listContacts = dbContactHandler.getAllContacts();
        contactsSpinner.setOnItemSelectedListener(this);
        contactSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);


        listTypes = dbTypeHandler.getAllTypes();
        typesSpinner.setOnItemSelectedListener(this);
        typeSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);


        listCategory = dbCategoryHandler.getAllCategories();
        categoriesSpinner.setOnItemSelectedListener(this);
        categorySpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory);
        categoriesSpinner.setAdapter(categorySpinnerArrayAdapter);
        categoriesSpinner.setSelection(firstCategory);


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
    public void onDismiss(final DialogInterface dialog) {
        //Fragment dialog had been dismissed
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinnerObjectCreationContact) {
            selectedContact = (Contact) contactsSpinner.getSelectedItem();
            if (selectedContact.get_id() == addContact) {
                createContactDialog();
            }
        } else if (spinner.getId() == R.id.spinnerObjectCreationType) {
            selectedType = (Type) typesSpinner.getSelectedItem();
        } else if (spinner.getId() == R.id.spinnerObjectCreationCateroy) {
            selectedCategory = (Category) categoriesSpinner.getSelectedItem();
            if(selectedCategory.get_id() == addCategory) {
                createCategoryDialog();
            }
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
        if(objectTitle.getText().toString().isEmpty() || contactsSpinner.getSelectedItemPosition() == emptyContact){
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

    public void createCategoryDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.activity_category_creation);
        dialog.setTitle(getResources().getString(R.string.title_activity_category_creation).toString());

        //set the custom dialog component
        categoryTitle = (EditText) dialog.findViewById(R.id.editTxtCategoryCreationTitle);
        FloatingActionButton fabCategory = (FloatingActionButton) dialog.findViewById(R.id.fabCategorySave);

        fabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory(dialog);
            }
        });

        dialog.show();
    }

    public void createCategory(Dialog dialog) {
        // Check if all the necessary data have been filled, return an alert instead.
        if(categoryTitle.getText().toString().isEmpty() ){
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
            Category category = new Category(dbCategoryHandler.getCategoriesCount(),
                    categoryTitle.getText().toString());

            dbCategoryHandler.createCategory(category);

            //Reset all fields
            categoryTitle.setText("");

            Toast.makeText(getApplicationContext(), R.string.added_category, Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        }
    }
}
