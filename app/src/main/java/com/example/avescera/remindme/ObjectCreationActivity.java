package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ObjectCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePFragment.OnDatePickedListener, Dialog.OnDismissListener {

    private InitDataBaseHandlers dbHandlers;

    private final Context context = this;

    private Object editedObject;

    private EditText objectTitle;
    private Spinner typesSpinner;
    private EditText objectQty;
    private Spinner categoriesSpinner;
    private Spinner contactsSpinner;
    private EditText objectDate;
    private EditText objectDetails;
    private EditText objectEndDate;
    private CheckBox objectUrgentChkBox;

    private DateFormat dateFormat;
    private final DateFormat builtDateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    private Contact selectedContact;
    private Type selectedType;
    private Category selectedCategory;
    private final List<Integer> tgtDateInfo = new ArrayList<>();
    private final List<Integer> urgentInfo = new ArrayList<>();

    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;
    private EditText categoryTitle;

    private Dialog contactDialog;
    private Dialog categoryDialog;

    private Calendar cal;
    private String isDateOrEndDate="";

    private final FragmentManager fm = getSupportFragmentManager();

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

        ImageView imgSave = (ImageView) findViewById(R.id.imgSaveObject);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createObject();
            }
        });

        // Implementing the detection of a click on the date selection line (to display the DatePicker)
        objectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateOrEndDate = "Date";
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Show Dialog Fragment
                showDatePickerDialog(v.getId());
            }
        });

        objectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateOrEndDate = "EndDate";
                //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

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
        dateFormat = builtDateFormat;
        Date date = new Date();
        objectDate.setText(dateFormat.format(date));
        objectDate.setInputType(InputType.TYPE_NULL);
        objectEndDate.setInputType(InputType.TYPE_NULL);
        objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));
        objectUrgentChkBox.setChecked(false);

        urgentInfo.add(0, cal.get(Calendar.YEAR));
        urgentInfo.add(1, cal.get(Calendar.MONTH) + 1);
        urgentInfo.add(2, cal.get(Calendar.DAY_OF_MONTH));

        int contact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0);
        int category_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);
        int editedObject_id = getIntent().getIntExtra(ActivityClass.OBJECT_ITEM, 0);

        if(editedObject_id != 0) {
            editedObject = dbHandlers.getDbObjectHandler().getObject(editedObject_id);
            objectTitle.setText(editedObject.get_title());
            objectQty.setText(String.valueOf(editedObject.get_quantity()));
            objectDate.setText(dateFormat.format(editedObject.get_date()));
            objectDetails.setText(editedObject.get_details());
            contactsSpinner.setSelection(editedObject.get_contactFkId() - 1);
            categoriesSpinner.setSelection(editedObject.get_categoryFkId() - 1);
            if(editedObject.get_endDate() == null){
                objectEndDate.setText("");
            } else {
                objectEndDate.setText(dateFormat.format(editedObject.get_endDate()));
            }
        }

        if(category_id != 0) {
            categoriesSpinner.setSelection(category_id - 1);
        }

        if(contact_id != 0) {
            contactsSpinner.setSelection(contact_id - 1);
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
        objectEndDate = (EditText) findViewById(R.id.editTxtObjectCreationEndDate);
        objectUrgentChkBox = (CheckBox) findViewById(R.id.chkbxImportantObject);
    }

    private void populateSpinner() {
        List<Contact> listContacts = dbHandlers.getDbContactHandler().getAllContacts();
        contactsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<Contact> contactSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);
        if(getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0) != 0) {
            contactsSpinner.setSelection(getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0) - 1);
        }

        List<Type> listTypes = dbHandlers.getDbTypeHandler().getAllTypes();
        typesSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<Type> typeSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);
        if (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_LOAN)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        } else if (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY).matches(ActivityClass.ACTIVITY_BORROW)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_BORROW_TYPE);
        } else {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        }

        List<Category> listCategory = dbHandlers.getDbCategoryHandler().getAllCategories();
        categoriesSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<Category> categorySpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCategory);
        categoriesSpinner.setAdapter(categorySpinnerArrayAdapter);
        categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);

        if (getIntent().getIntExtra(ActivityClass.OBJECT_ITEM, 0) != 0) {
            int object_id = getIntent().getIntExtra(ActivityClass.OBJECT_ITEM, 1);
            Object object = dbHandlers.getDbObjectHandler().getObject(object_id);
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

    private void createObject() {
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
                Date endDate = (objectEndDate.getText().toString().matches(""))?
                        null :
                        dateFormat.parse(objectEndDate.getText().toString());

                Object object = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(),
                        objectTitle.getText().toString(),
                        Integer.parseInt(objectQty.getText().toString()),
                        objectDetails.getText().toString(),
                        dateFormat.parse(objectDate.getText().toString()),
                        selectedCategory.get_id(),
                        selectedType.get_id(),
                        selectedContact.get_id(),
                        endDate,
                        objectUrgentChkBox.isChecked());

                //object.addEvent(this, eventInfo);
                //object.onAddEventClicked(view, this, eventInfo);

                boolean urgent;
                if(objectUrgentChkBox.isChecked()) {
                    urgent = true;
                    object.addEvent(this, urgentInfo, urgent);
                }

                if(!objectEndDate.getText().toString().matches("")) {
                    urgent = false;
                    if (dbHandlers.getDbReminderHandler().getCountTgtDateActiveReminders() > 0)
                        object.addEvent(this, tgtDateInfo, urgent);
                }

                dbHandlers.getDbObjectHandler().createObject(object);
                Toast.makeText(getApplicationContext(), R.string.added_item, Toast.LENGTH_SHORT).show();

                //Reset all fields
                objectTitle.setText("");
                objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));
                objectDetails.setText("");
                categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);
                objectEndDate.setText("");
                objectUrgentChkBox.setChecked(false);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Date endDate = (objectEndDate.getText().toString().matches(""))?
                        null :
                        dateFormat.parse(objectEndDate.getText().toString());

                editedObject.set_title(objectTitle.getText().toString());
                editedObject.set_quantity(Integer.parseInt(objectQty.getText().toString()));
                editedObject.set_details(objectDetails.getText().toString());
                editedObject.set_date(dateFormat.parse(objectDate.getText().toString()));
                editedObject.set_categoryFkId(selectedCategory.get_id());
                editedObject.set_typeFkId(selectedType.get_id());
                editedObject.set_contactFkId(selectedContact.get_id());
                editedObject.set_endDate(endDate);
                editedObject.set_urgent(objectUrgentChkBox.isChecked());

                dbHandlers.getDbObjectHandler().updateObject(editedObject);
                Toast.makeText(getApplicationContext(), R.string.updated_item, Toast.LENGTH_SHORT).show();

                //Reset all fields
                objectTitle.setText("");
                objectQty.setText(String.valueOf(ActivityClass.OBJECT_DEFAULT_QTY));
                objectDetails.setText("");
                categoriesSpinner.setSelection(ActivityClass.SPINNER_FIRST_CATEGORY);
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);
                objectEndDate.setText("");
                objectUrgentChkBox.setChecked(false);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Methods to display the calendar to pick a date.
    private void showDatePickerDialog(int layoutId) {
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = createDatePickerBundle(layoutId, year, month, day);
        DialogFragment newFragment = new DatePFragment();
        newFragment.setArguments(bundle);
        newFragment.show(fm, Integer.toString(layoutId));
    }

    private Bundle createDatePickerBundle(int layoutId, int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("year", year);
        bundle.putInt("month", month+1);
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
        date = (String.valueOf(day).length() == 1)?
                "0" + day + "/" :
                day + "/";

        date += (String.valueOf(month).length() == 1)?
                "0" + (month+1) + "/" + year :
                (month+1) + "/" + year;

        // Displaying the date in the EditText box.
        try {
            intermediateDate = builtDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(isDateOrEndDate.matches("Date")) {
            objectDate.setText(dateFormat.format(intermediateDate));

            urgentInfo.add(0, year);
            urgentInfo.add(1, month + 1);
            urgentInfo.add(2, day);
        } else if (isDateOrEndDate.matches("EndDate")){
            objectEndDate.setText(dateFormat.format(intermediateDate));

            tgtDateInfo.add(0, year);
            tgtDateInfo.add(1, month + 1);
            tgtDateInfo.add(2, day);
        }
    }

    private void createContactDialog(){
        //Custom dialog
        contactDialog.setContentView(R.layout.activity_contact_creation);
        contactDialog.setTitle(getResources().getString(R.string.title_activity_contact_creation));

        //set the custom dialog component
        contactFName = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) contactDialog.findViewById(R.id.edit_txt_contact_creation_email);
        ImageView imgSave = (ImageView) contactDialog.findViewById(R.id.imgSaveContact);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContact(contactDialog);
            }
        });

        contactDialog.show();
    }

    private void createContact(Dialog dialog) {
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

    private void createCategoryDialog(){
        //Custom dialog
        categoryDialog.setContentView(R.layout.activity_category_creation);
        categoryDialog.setTitle(getResources().getString(R.string.title_activity_category_creation));

        //set the custom dialog component
        categoryTitle = (EditText) categoryDialog.findViewById(R.id.editTxtCategoryCreationTitle);
        ImageView imgSave = (ImageView) categoryDialog.findViewById(R.id.imgCategorySave);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory(categoryDialog);
            }
        });

        categoryDialog.show();
    }

    private void createCategory(Dialog dialog) {
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
