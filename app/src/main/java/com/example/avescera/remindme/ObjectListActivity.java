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

import com.example.avescera.remindme.Adapters.ObjectAdapter;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.util.List;

public class ObjectListActivity extends AppCompatActivity {

    private InitDataBaseHandlers dbHandlers;
    private ListView listViewObjectLoan;
    private String listFilter = null;
    private int category_id;
    private int contact_id;
    private Dialog dialog;
    private String filterType;
    private ImageView imgVObjectFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();
        populateListView();

        imgVObjectFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog();
            }
        });
    }

    private void attachViewItems(){
        listViewObjectLoan = (ListView) findViewById(R.id.listViewObjectLoanItems);
        imgVObjectFilter = (ImageView) findViewById(R.id.imgVObjectFilter);
    }

    private void populateListView(){
        List<Object> listObjectItems = null;

        if(listFilter != null) {
            if (contact_id > 2) {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listObjectItems = (category_id > 2)?
                            dbHandlers.getDbObjectHandler().getContactCatTypeObjects(contact_id, category_id, ActivityClass.DATABASE_LOAN_TYPE, filterType) :
                            dbHandlers.getDbObjectHandler().getContactTypeObjects(contact_id, ActivityClass.DATABASE_LOAN_TYPE, filterType);
                } else {
                    listObjectItems = (category_id > 2)?
                            dbHandlers.getDbObjectHandler().getContactCatTypeObjects(contact_id, category_id, ActivityClass.DATABASE_BORROW_TYPE, filterType) :
                            dbHandlers.getDbObjectHandler().getContactTypeObjects(contact_id, ActivityClass.DATABASE_BORROW_TYPE, filterType);
                }
            } else {
                if (listFilter.matches(ActivityClass.ACTIVITY_LOAN)) {
                    listObjectItems = (category_id > 2)?
                            dbHandlers.getDbObjectHandler().getCategoryTypeObjects(category_id, ActivityClass.DATABASE_LOAN_TYPE, filterType) :
                            dbHandlers.getDbObjectHandler().getTypeObjects(ActivityClass.DATABASE_LOAN_TYPE, filterType);
                } else {
                    listObjectItems = (category_id > 2)?
                            dbHandlers.getDbObjectHandler().getCategoryTypeObjects(category_id, ActivityClass.DATABASE_BORROW_TYPE, filterType) :
                            dbHandlers.getDbObjectHandler().getTypeObjects(ActivityClass.DATABASE_BORROW_TYPE, filterType);

                }
            }
        }

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectItems);
        listViewObjectLoan.setAdapter(adapter);
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);
        dialog = new Dialog(this);

        listFilter = (getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) != null)?
                getIntent().getStringExtra(ActivityClass.CALLING_ACTIVITY) :
                null;
        String title = (listFilter.matches(ActivityClass.ACTIVITY_LOAN))?
                getString(R.string.title_activity_object_loan) :
                getString(R.string.title_activity_object_borrow);
        setTitle(title);

        contact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0);
        category_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        } else if (id == R.id.object_creation){
            goToObjectCreationPage();
        } else if (id == R.id.object_home) {
            goToHomePage();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToObjectCreationPage() {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, listFilter);
        if(category_id != 0) {
            intent.putExtra(ActivityClass.CATEGORY_ITEM, category_id);
        }
        if (contact_id != 0) {
            intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        }
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
                new int[] {ContextCompat.getColor(this,R.color.colorObject)                }
        );


        //Set the custom dialog components
        final RadioGroup radioGrp = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        AppCompatRadioButton radioBAmntUp = (AppCompatRadioButton) dialog.findViewById(R.id.radioBAmntOrQtyUp);
        radioBAmntUp.setText(getText(R.string.quantity_up));
        AppCompatRadioButton radioBAmntDown = (AppCompatRadioButton) dialog.findViewById(R.id.radioBAmntOrQtyDown);
        radioBAmntDown.setText(getText(R.string.quantity_down));
        AppCompatRadioButton radioBDateUp = (AppCompatRadioButton) dialog.findViewById(R.id.radioBDateUp);
        AppCompatRadioButton radioBDateDown = (AppCompatRadioButton) dialog.findViewById(R.id.radioBDateDown);
        ImageView imgVCheck = (ImageView) dialog.findViewById(R.id.imgVCheck);
        imgVCheck.setImageResource(R.mipmap.object_checck);

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
