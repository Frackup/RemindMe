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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Classes.Type;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MoneyCreationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DatePFragment.OnDatePickedListener, Dialog.OnDismissListener {

    private InitDataBaseHandlers dbHandlers;

    private final Context context = this;

    private EditText moneyTitle;
    private Spinner typesSpinner;
    private EditText moneyAmount;
    private Spinner contactsSpinner;
    private EditText moneyDate;
    private EditText moneyDetails;
    private EditText moneyEndDate;
    private CheckBox moneyUrgentChkbox;

    private DateFormat dateFormat;
    private final DateFormat builtDateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
    private Contact selectedContact;
    private Type selectedType;

    private EditText contactFName;
    private EditText contactLName;
    private EditText contactPhone;
    private EditText contactEmail;

    private Calendar cal;
    private Dialog dialog;
    private Money editedMoney;
    private String isDateOrEndDate="";
    private final List<Integer> tgtDateInfo = new ArrayList<>();
    private final List<Integer> urgentInfo = new ArrayList<>();

    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        populateSpinner();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dbHandlers = new InitDataBaseHandlers(getBaseContext());
                populateSpinner();
                //select as default contact the newly created one.
                contactsSpinner.setSelection(dbHandlers.getDbContactHandler().getContactsCount()-1);
            }
        });

        ImageView imgSave = (ImageView) findViewById(R.id.imgSaveMoney);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMoney();
            }
        });

        // Implementing the detection of a click on the date selection line (to display the DatePicker)
        moneyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateOrEndDate = "Date";
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Show Dialog Fragment
                showDatePickerDialog(v.getId());
            }
        });

        moneyEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateOrEndDate = "EndDate";
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Show Dialog Fragment
                showDatePickerDialog(v.getId());
            }
        });
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        dialog = new Dialog(context);
        cal = Calendar.getInstance();
        dateFormat = builtDateFormat;
        Date date = new Date();
        moneyDate.setText(dateFormat.format(date));

        urgentInfo.add(0, cal.get(Calendar.YEAR));
        urgentInfo.add(1, cal.get(Calendar.MONTH) + 1);
        urgentInfo.add(2, cal.get(Calendar.DAY_OF_MONTH));

        if(getIntent().getIntExtra(ActivityClass.MONEY_ITEM, 0) != 0) {
            int editedMoney_id = getIntent().getIntExtra(ActivityClass.MONEY_ITEM, 0);
            editedMoney = dbHandlers.getDbMoneyHandler().getMoney(editedMoney_id);
            moneyTitle.setText(editedMoney.get_title());
            moneyAmount.setText(String.valueOf(editedMoney.get_amount()));
            moneyDate.setText(dateFormat.format(editedMoney.get_date()));
            moneyDetails.setText(editedMoney.get_details());
            contactsSpinner.setSelection(editedMoney.get_contactFkId() - 1);
            if(editedMoney.get_endDate() == null){
                moneyEndDate.setText("");
            } else {
                moneyEndDate.setText(dateFormat.format(editedMoney.get_endDate()));
            }
            moneyUrgentChkbox.setChecked(editedMoney.is_urgent());
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
        moneyEndDate = (EditText) findViewById(R.id.editTxtMoneyCreationEndDate);
        moneyUrgentChkbox = (CheckBox) findViewById(R.id.chkboxImportantMoney);
    }

    private void populateSpinner(){
        List<Contact> listContacts = dbHandlers.getDbContactHandler().getAllContacts();
        contactsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<Contact> contactSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listContacts);
        contactsSpinner.setAdapter(contactSpinnerArrayAdapter);

        int contact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0);
        int money_id = getIntent().getIntExtra(ActivityClass.MONEY_ITEM, 0);
        if(contact_id > 2) {
            contactsSpinner.setSelection(contact_id - 1);
        } else if (money_id != 0) {
            Money money = dbHandlers.getDbMoneyHandler().getMoney(money_id);
            contactsSpinner.setSelection(money.get_contactFkId() - 1);
        }

        List<Type> listTypes = dbHandlers.getDbTypeHandler().getAllTypes();
        typesSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<Type> typeSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTypes);
        typesSpinner.setAdapter(typeSpinnerArrayAdapter);

        String listFilter = (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null)?
                getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) :
                null;
        if(listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
            typesSpinner.setSelection(ActivityClass.SPINNER_LOAN_TYPE);
        } else if (listFilter.matches(ActivityClass.ACTIVITY_BORROW)) {
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

    private void createMoney() {
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
        } else if (editedMoney == null) {
            //Create new Money item
            try {
                Date endDate = (moneyEndDate.getText().toString().matches("")) ?
                        null :
                        dateFormat.parse(moneyEndDate.getText().toString());

                Money money = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(),
                        moneyTitle.getText().toString(),
                        Float.parseFloat(moneyAmount.getText().toString()),
                        moneyDetails.getText().toString(),
                        dateFormat.parse(moneyDate.getText().toString()),
                        selectedType.get_id(),
                        selectedContact.get_id(),
                        endDate,
                        moneyUrgentChkbox.isChecked());

                boolean urgent;
                if(moneyUrgentChkbox.isChecked()) {
                    urgent = true;
                    money.addEvent(this, urgentInfo, urgent);
                }

                if(!moneyEndDate.getText().toString().matches("")) {
                    urgent = false;
                    if (dbHandlers.getDbReminderHandler().getCountTgtDateActiveReminders() > 0)
                        money.addEvent(this, tgtDateInfo, urgent);
                }

                dbHandlers.getDbMoneyHandler().createMoney(money);

                //money.addEvent(this, eventInfo);

                //Reset all fields
                moneyTitle.setText("");
                moneyAmount.setText("");
                moneyDetails.setText("");
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);
                moneyEndDate.setText("");
                moneyUrgentChkbox.setChecked(false);
                Date date = new Date();
                moneyDate.setText(dateFormat.format(date));

                Toast.makeText(getApplicationContext(), R.string.added_item, Toast.LENGTH_SHORT).show();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //Update existing Money item
            try {
                Date endDate = (moneyEndDate.getText().toString().matches("")) ?
                        null :
                        dateFormat.parse(moneyEndDate.getText().toString());

                editedMoney.set_title(moneyTitle.getText().toString());
                editedMoney.set_amount(Float.parseFloat(moneyAmount.getText().toString()));
                editedMoney.set_details(moneyDetails.getText().toString());
                editedMoney.set_date(dateFormat.parse(moneyDate.getText().toString()));
                editedMoney.set_typeFkId(selectedType.get_id());
                editedMoney.set_contactFkId(selectedContact.get_id());
                editedMoney.set_endDate(endDate);
                editedMoney.set_urgent(moneyUrgentChkbox.isChecked());

                dbHandlers.getDbMoneyHandler().updateMoney(editedMoney);
                Toast.makeText(getApplicationContext(), R.string.updated_item, Toast.LENGTH_SHORT).show();

                //Reset all fields
                moneyTitle.setText("");
                moneyAmount.setText("");
                moneyDetails.setText("");
                contactsSpinner.setSelection(ActivityClass.SPINNER_EMPTY_CONTACT);
                moneyEndDate.setText("");
                moneyUrgentChkbox.setChecked(false);
                Date date = new Date();
                moneyDate.setText(dateFormat.format(date));

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
            moneyDate.setText(builtDateFormat.format(intermediateDate));

            urgentInfo.add(0, year);
            urgentInfo.add(1, month + 1);
            urgentInfo.add(2, day);
        } else if (isDateOrEndDate.matches("EndDate")) {
            moneyEndDate.setText(builtDateFormat.format(intermediateDate));

            tgtDateInfo.add(0, year);
            tgtDateInfo.add(1, month + 1);
            tgtDateInfo.add(2, day);
        }
    }

    private void createContactDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.activity_contact_creation);
        dialog.setTitle(getResources().getString(R.string.title_activity_contact_creation));

        //set the custom dialog component
        contactFName = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_first_name);
        contactLName = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_last_name);
        contactPhone = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_phone);
        contactEmail = (EditText) dialog.findViewById(R.id.edit_txt_contact_creation_email);
        ImageView imgSave = (ImageView) dialog.findViewById(R.id.imgSaveContact);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContact(dialog);
            }
        });

        dialog.show();
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
}
