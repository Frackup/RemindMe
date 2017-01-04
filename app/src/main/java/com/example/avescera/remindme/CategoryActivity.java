package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Interfaces.ActivityClass;

public class CategoryActivity extends AppCompatActivity {

    private Button btnCatLoan;
    private Button btnCatBorrow;
    private Category category;
    private final int contact_id = 0;
    private InitDataBaseHandlers dbHandlers;

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
    }

    private void initVariables(){
        dbHandlers = new InitDataBaseHandlers(this);

        if(getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0) != 0) {
            int category_id = getIntent().getIntExtra(ActivityClass.CATEGORY_ITEM, 0);
            category = dbHandlers.getDbCategoryHandler().getCategory(category_id);

            btnCatLoan.setText(dbHandlers.getDbObjectHandler().getTotalQtyByTypeAndCategory(category_id, ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
            btnCatBorrow.setText(dbHandlers.getDbObjectHandler().getTotalQtyByTypeAndCategory(category_id, ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
        }

        setTitle(category.get_category());
    }

    private void goToObjectCreation() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_edit_category) {
            Intent intent = new Intent(this, CategoryCreationActivity.class);
            intent.putExtra(ActivityClass.CATEGORY_ITEM, category.get_id());
            startActivity(intent);
        } else if (id == R.id.action_delete_category) {
            deleteCategory();
        }

        return super.onOptionsItemSelected(item);
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

    private void deleteCategory(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.category_deletion_process)
                // Set Dialog Message
                .setMessage(R.string.category_deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandlers.getDbCategoryHandler().deleteCategory(category);

                        Toast.makeText(getBaseContext(), R.string.deletion_confirmation, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.negative_answer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        alertDialog.show();
    }
}
