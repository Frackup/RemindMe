package com.example.avescera.remindme;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.InitDataBase;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Reminder;
import com.example.avescera.remindme.Interfaces.ActivityClass;

public class RemindersActivity extends AppCompatActivity {

    private ColorStateList colorStateList;

    private AppCompatRadioButton rButtonNoReminder;
    private AppCompatRadioButton rButtonOneReminder;
    private AppCompatRadioButton rButtonTwoReminder;
    private RadioGroup rGroupTargetDate;
    private ImageView imgVReminderSave;
    private EditText editTxtNumberOfDays;

    private InitDataBaseHandlers dbHandlers;
    private Reminder urgentReminder;
    private Reminder tgtDateRem1;
    private Reminder tgtDateRem2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachView();
        initVariables();

        imgVReminderSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminders();
            }
        });
    }

    private void attachView(){
        rButtonNoReminder = (AppCompatRadioButton) findViewById(R.id.rButtonNoReminder);
        rButtonOneReminder = (AppCompatRadioButton) findViewById(R.id.rButtonOneReminder);
        rButtonTwoReminder = (AppCompatRadioButton) findViewById(R.id.rButtonTwoReminders);
        rGroupTargetDate = (RadioGroup) findViewById(R.id.rGroupTargetDate);
        imgVReminderSave = (ImageView) findViewById(R.id.imgSaveReminder);
        editTxtNumberOfDays = (EditText) findViewById(R.id.editTxtUrgentReminderNmbr);
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //checked
                },
                new int[] {ContextCompat.getColor(this,R.color.colorReminders) }
        );

        rButtonNoReminder.setButtonTintList(colorStateList);
        rButtonOneReminder.setButtonTintList(colorStateList);
        rButtonTwoReminder.setButtonTintList(colorStateList);

        //filling the edittext with the value currently defined by the user.
        urgentReminder = dbHandlers.getDbReminderHandler().getReminder(ActivityClass.URGENT_REMINDER);
        tgtDateRem1 = dbHandlers.getDbReminderHandler().getReminder(ActivityClass.TGT_DATE_REMINDER_1);
        tgtDateRem2 = dbHandlers.getDbReminderHandler().getReminder(ActivityClass.TGT_DATE_REMINDER_2);

        int nmbrOfDays = urgentReminder.get_hour()/24;
        editTxtNumberOfDays.setText(String.valueOf(nmbrOfDays));

        //check the right radio button depending what's currently setup into the database.
        switch (dbHandlers.getDbReminderHandler().getCountTgtDateActiveReminders()) {
            case 0:
                rButtonNoReminder.setChecked(true);
                break;
            case 1:
                rButtonOneReminder.setChecked(true);
                break;
            case 2:
                rButtonTwoReminder.setChecked(true);
                break;
        }
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

    private void saveReminders(){
        switch(rGroupTargetDate.getCheckedRadioButtonId()) {
            case R.id.rButtonNoReminder:
                tgtDateRem1.set_active(false);
                tgtDateRem2.set_active(false);
                break;
            case R.id.rButtonOneReminder:
                tgtDateRem1.set_active(true);
                tgtDateRem2.set_active(false);
                break;
            case R.id.rButtonTwoReminders:
                tgtDateRem1.set_active(true);
                tgtDateRem2.set_active(true);
                break;
        }


        boolean incorrect = false;
        if(editTxtNumberOfDays.getText().toString().matches("") ||
                Integer.parseInt(editTxtNumberOfDays.getText().toString())<1 ||
                Integer.parseInt(editTxtNumberOfDays.getText().toString())>7){
            Toast.makeText(this, getResources().getString(R.string.urgent_wrong_entry), Toast.LENGTH_LONG).show();
            incorrect = true;
            editTxtNumberOfDays.setText("1");
        }

        int daysToHour = (editTxtNumberOfDays.getText().toString().matches(""))?
                1 :
                Integer.parseInt(editTxtNumberOfDays.getText().toString())*24;

        urgentReminder.set_hour(daysToHour);
        urgentReminder.set_duration(daysToHour*60);
        dbHandlers.getDbReminderHandler().updateReminder(urgentReminder);
        dbHandlers.getDbReminderHandler().updateReminder(tgtDateRem1);
        dbHandlers.getDbReminderHandler().updateReminder(tgtDateRem2);

        if(!incorrect) {
            Toast.makeText(getApplicationContext(), R.string.reminders_saved, Toast.LENGTH_SHORT).show();
        }
    }
}
