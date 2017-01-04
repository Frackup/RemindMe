package com.example.avescera.remindme;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.avescera.remindme.Adapters.MoneyAdapter;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.util.List;

public class MoneyListActivity extends AppCompatActivity {

    private ListView listViewMoneyLoan;
    private InitDataBaseHandlers dbHandlers;
    private String listFilter = null;
    private int contactId;
    private ImageView imgVFilter;
    private Dialog dialog;
    private String filterType = ActivityClass.FILTER_DATE_DESC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        populateListView();

        imgVFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog();
            }
        });
    }

    private void attachViewItems(){
        listViewMoneyLoan = (ListView) findViewById(R.id.listViewMoneyLoanItems);
        imgVFilter = (ImageView) findViewById(R.id.imgVMoneyFilter);
    }

    private void populateListView(){
        List<Money> listMoneyItems = null;
        if(listFilter != null) {
            if (contactId > 2) {
                listMoneyItems = (listFilter.matches(ActivityClass.ACTIVITY_LOAN))?
                        dbHandlers.getDbMoneyHandler().getContactTypeMoneys(contactId, ActivityClass.DATABASE_LOAN_TYPE, filterType) :
                        dbHandlers.getDbMoneyHandler().getContactTypeMoneys(contactId, ActivityClass.DATABASE_BORROW_TYPE, filterType);
            } else {
                listMoneyItems = (listFilter.matches(ActivityClass.ACTIVITY_LOAN))?
                        dbHandlers.getDbMoneyHandler().getTypeMoneys(ActivityClass.DATABASE_LOAN_TYPE, filterType) :
                        dbHandlers.getDbMoneyHandler().getTypeMoneys(ActivityClass.DATABASE_BORROW_TYPE, filterType);
            }
        }

        MoneyAdapter adapter = new MoneyAdapter(MoneyListActivity.this, listMoneyItems);
        listViewMoneyLoan.setAdapter(adapter);
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
        dialog =  new Dialog(this);

        listFilter = (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null)?
                getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) :
                null;
        contactId = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM,0);
        String title = (listFilter.matches(ActivityClass.ACTIVITY_LOAN))?
                getString(R.string.title_activity_money_loan) :
                getString(R.string.title_activity_money_borrow);
        setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_money_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        } else if (id == R.id.money_creation) {
            goToMoneyCreationPage();
        } else if (id == R.id.money_home) {
            goToHomePage();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMoneyCreationPage() {
        Intent intent = new Intent(this, MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, listFilter);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHandlers = new InitDataBaseHandlers(this);
        populateListView();
    }

    private void goToHomePage(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    private void filterDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.filter_dialog);
        dialog.setTitle(getText(R.string.filter_title));

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //checked
                },
                new int[] {ContextCompat.getColor(this,R.color.colorMoney)                }
        );


        //Set the custom dialog components
        final RadioGroup radioGrp = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        AppCompatRadioButton radioBAmntUp = (AppCompatRadioButton) dialog.findViewById(R.id.radioBAmntOrQtyUp);
        radioBAmntUp.setText(getText(R.string.amount_up));
        AppCompatRadioButton radioBAmntDown = (AppCompatRadioButton) dialog.findViewById(R.id.radioBAmntOrQtyDown);
        radioBAmntDown.setText(getText(R.string.amount_down));
        AppCompatRadioButton radioBDateUp = (AppCompatRadioButton) dialog.findViewById(R.id.radioBDateUp);
        AppCompatRadioButton radioBDateDown = (AppCompatRadioButton) dialog.findViewById(R.id.radioBDateDown);
        ImageView imgVCheck = (ImageView) dialog.findViewById(R.id.imgVCheck);
        imgVCheck.setImageResource(R.mipmap.money_check);

        radioBAmntUp.setButtonTintList(colorStateList);
        radioBAmntDown.setButtonTintList(colorStateList);
        radioBDateUp.setButtonTintList(colorStateList);
        radioBDateDown.setButtonTintList(colorStateList);

        imgVCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(radioGrp.getCheckedRadioButtonId()) {
                    case R.id.radioBAmntOrQtyUp:
                        filterType = ActivityClass.FILTER_SPEC_ASC;
                        break;
                    case R.id.radioBAmntOrQtyDown:
                        filterType = ActivityClass.FILTER_SPEC_DESC;
                        break;
                    case R.id.radioBDateUp:
                        filterType = ActivityClass.FILTER_DATE_ASC;
                        break;
                    case R.id.radioBDateDown:
                        filterType = ActivityClass.FILTER_DATE_DESC;
                        break;
                }
                dialog.dismiss();
                onResume();
            }
        });

        dialog.show();
    }
}
