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

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
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

public class ObjectCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePFragment.OnDatePickedListener, Dialog.OnDismissListener {

    private InitDataBaseHandlers dbHandlers;

    final Context context = this;

    private Object editedObject;

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

    private Dialog contactDialog;
    private Dialog categoryDialog;

    private Calendar cal;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        populateSpinner();

        contactDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dbHandlers = new InitDataBaseHandlers(getBaseContext());
                populateSpinner();

                //select as defaut contact the newly created contact
                contactsSpinner.setSelection(dbHandlers.getDbContactHandler().getContactsCount() - 1);
            }
        });

        categoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dbHandlers = new InitDataBaseHandlers(getBaseContext());
                populateSpinner();

                //select as defaut contact the newly created contact
                categoriesSpinner.setSelection(dbHandlers.getDbCategoryHandler().getCategoriesCount() - 1);
            }
        });

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

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        contactDialog = new Dialog(context);
        categoryDialog = new Dialog(context);
        cal = Calendar.getInstance();
        //dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        dateFormat = builtDateFormat;
        date = new Date();
        objectDate.setText(dateFormat.format(date));
        objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));

        if(getIntent().getSerializableExtra(ActivityClass.OBJECT_ITEM) != null) {
            editedObject = (Object) getIntent().getSerializableExtra(ActivityClass.OBJECT_ITEM);
            objectTitle.setText(editedObject.get_title());
            objectQty.setText(String.valueOf(editedObject.get_quantity()));
            objectDate.setText(dateFormat.format(editedObject.get_date()));
            objectDetails.setText(editedObject.get_details());
            contactsSpinner.setSelection(editedObject.get_contactFkId() - 1);
            categoriesSpinner.setSelection(editedObject.get_categoryFkId() - 1);
        }
    }

    private void attachViewItems(){
        //Initiate variables
        objectTitle = (EditText) findViewById(R.id.editTxtObjectCreationTitle);
        typesSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationType);
        objectQty = (EditText) findViewById(R.id.editTxtObjectCreationQty);
        categoriesSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationCateroy);
        contactsSpinner = (Spinner) findViewById(R.id.spinnerObjectCreationContact);
        objectDate = (EditText) findViewById(R.id.editTxtObjectCreationDate);
        objectDetails = (EditText) findViewById(R.id.editTxtObjectCreationDetails);
    }

    private void populateSpinner() {
        listContacts = dbHandlers.getDbContactHandler().getAllContacts();
        contactsSpinner.setOnItemSelectedListener(this);
        contactSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);
        if(getIntent().getStringExtra(ActivityClass.CONTACT_ITEM) != null) {
            contactsSpinner.setSelection(getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 1) - 1);
        }

        listTypes = dbHandlers.getDbTypeHandler().getAllTypes();
        typesSpinner.setOnItemSelectedListener(this);
        typeSpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);
        if (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_LOAN)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        } else if (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_BORROW)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_BORROW_TYPE);
        } else {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        }

        listCategory = dbHandlers.getDbCategoryHandler().getAllCategories();
        categoriesSpinner.setOnItemSelectedListener(this);
        categorySpinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory);
        categoriesSpinner.setAdapter(categorySpinnerArrayAdapter);
        categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);

        if (getIntent().getSerializableExtra(ActivityClass.OBJECT_ITEM) != null) {
            Object object = (Object) getIntent().getSerializableExtra(ActivityClass.OBJECT_ITEM);
            contactsSpinner.setSelection(object.get_contactFkId() - 1);
            categoriesSpinner.setSelection(object.get_categoryFkId() - 1);
        }
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
            if (contactsSpinner.getSelectedItemPosition() == ActivityClass.SPINNER_ADD_CONTACT) {
                createContactDialog();
            }
        } else if (spinner.getId() == R.id.spinnerObjectCreationType) {
            selectedType = (Type) typesSpinner.getSelectedItem();
        } else if (spinner.getId() == R.id.spinnerObjectCreationCateroy) {
            selectedCategory = (Category) categoriesSpinner.getSelectedItem();
            if(categoriesSpinner.getSelectedItemPosition() == ActivityClass.SPINNER_ADD_CATEGORY) {
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
        if(objectTitle.getText().toString().isEmpty() || contactsSpinner.getSelectedItemPosition() == ActivityClass.SPINNER_EMPTY_CONTACT){
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
        } else if (editedObject == null) {
            try {
                Object object = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(),
                        objectTitle.getText().toString(),
                        Integer.parseInt(objectQty.getText().toString()),
                        objectDetails.getText().toString(),
                        dateFormat.parse(objectDate.getText().toString()),
                        selectedCategory.get_id(),
                        selectedType.get_id(),
                        selectedContact.get_id(),
                        null);

                dbHandlers.getDbObjectHandler().createObject(object);
                Toast.makeText(getApplicationContext(), R.string.added_item, Toast.LENGTH_SHORT).show();

                //Reset all fields
                objectTitle.setText("");
                objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));
                objectDetails.setText("");
                categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                editedObject.set_title(objectTitle.getText().toString());
                editedObject.set_quantity(Integer.parseInt(objectQty.getText().toString()));
                editedObject.set_details(objectDetails.getText().toString());
                editedObject.set_date(dateFormat.parse(objectDate.getText().toString()));
                editedObject.set_categoryFkId(selectedCategory.get_id());
                editedObject.set_typeFkId(selectedType.get_id());
                editedObject.set_contactFkId(selectedContact.get_id());

                dbHandlers.getDbObjectHandler().updateObject(editedObject);
                Toast.makeText(getApplicationContext(), R.string.updated_item, Toast.LENGTH_SHORT).show();

                //Reset all fields
                objectTitle.setText("");
                objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));
                objectDetails.setText("");
                categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);

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
        contactDialog.setContentView(R.layout.activity_contact_creation);
        contactDialog.setTitle(getResources().getString(R.string.title_activity_contact_creation).toString());

        //set the custom dialog component
        contactFName = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_email);
        FloatingActionButton fabContact = (FloatingActionButton) contactDialog.findViewById(R.id.fabSaveContact);

        fabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContact(contactDialog);
            }
        });

        contactDialog.show();
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

            dialog.dismiss();
        }
    }

    public void createCategoryDialog(){
        //Custom dialog
        categoryDialog.setContentView(R.layout.activity_category_creation);
        categoryDialog.setTitle(getResources().getString(R.string.title_activity_category_creation).toString());

        //set the custom dialog component
        categoryTitle = (EditText) categoryDialog.findViewById(R.id.editTxtCategoryCreationTitle);
        FloatingActionButton fabCategory = (FloatingActionButton) categoryDialog.findViewById(R.id.fabCategorySave);

        fabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory(categoryDialog);
            }
        });

        categoryDialog.show();
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
            Category category = new Category(dbHandlers.getDbCategoryHandler().getCategoryNextId(),
                    categoryTitle.getText().toString());

            dbHandlers.getDbCategoryHandler().createCategory(category);

            //Reset all fields
            categoryTitle.setText("");

            Toast.makeText(getApplicationContext(), R.string.added_category, Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        }
    }
}
