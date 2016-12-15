package com.example.avescera.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.avescera.remindme.Adapters.CategoryAdapter;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private Button btnCatLoan;
    private Button btnCatBorrow;
    private TextView txtVContact;
    private InitDataBaseHandlers dbHandlers;
    private Category category;
    private int contact_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachViewItems();
        initVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCategoryObjectCreation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToObjectCreation();
            }
        });

        btnCatLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToObjectCatActivity(ActivityClass.ACTIVITY_LOAN, category.get_id(), contact_id);
            }
        });

        btnCatBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToObjectCatActivity(ActivityClass.ACTIVITY_BORROW, category.get_id(), contact_id);
            }
        });
    }

    private void attachViewItems(){
        btnCatLoan = (Button) findViewById(R.id.btnCatLoan);
        btnCatBorrow = (Button) findViewById(R.id.btnCatBorrow);
        txtVContact = (TextView) findViewById(R.id.txtVCatContact);
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
            int category_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);
            category = dbHandlers.getDbCategoryHandler().getCategory(category_id);

            if (getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0) != 0) {
                contact_id = getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0);
                Contact contact = dbHandlers.getDbContactHandler().getContact(contact_id);
                txtVContact.setText(getResources().getString(R.string.txtview_laius_cat_contact) + " " +
                        contact.get_firstName() + " " + contact.get_lastName());
                txtVContact.setVisibility(View.VISIBLE);

                btnCatLoan.setText(dbHandlers.getDbObjectHandler().getTotalQtyByCatTypeAndContact(contact_id, category_id, ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
                btnCatBorrow.setText(dbHandlers.getDbObjectHandler().getTotalQtyByCatTypeAndContact(contact_id, category_id, ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
            } else {
                txtVContact.setVisibility(View.INVISIBLE);

                btnCatLoan.setText(dbHandlers.getDbObjectHandler().getTotalQtyByTypeAndCategory(category_id, ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
                btnCatBorrow.setText(dbHandlers.getDbObjectHandler().getTotalQtyByTypeAndCategory(category_id, ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
            }
        }

        setTitle(category.get_category());
    }

    public void goToObjectCreation() {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.CATEGORY_ITEM, category.get_id());

        if(getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0) != 0) {
            intent.putExtra(ActivityClass.CONTACT_ITEM, getIntent().getIntExtra(ActivityClass.CONTACT_ITEM, 0));
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initVariables();
    }

    private void goToObjectCatActivity(String type, int category_id, int contact_id){
        Intent intent = new Intent(this, ObjectListActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, type);
        intent.putExtra(ActivityClass.CATEGORY_ITEM, category_id);
        if(contact_id != 0){
            intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        }

        startActivity(intent);
    }
}
