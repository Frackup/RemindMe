package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;

import java.sql.SQLException;

public class CategoryCreationActivity extends AppCompatActivity {

    private DatabaseCategoryHandler dbCategoryHandler;
    private EditText editCategoryTitle;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDbHandlers();

        attachViewItems();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCategorySave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategory(view);
            }
        });
    }

    private void initDbHandlers() {
        //Initiate the DBHandlers
        dbCategoryHandler = new DatabaseCategoryHandler(this);
        try {
            dbCategoryHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        editCategoryTitle = (EditText) findViewById(R.id.editTxtCategoryCreationTitle);
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

    public void createCategory(View view) {
        // Check if all the necessary data have been filled, return an alert instead.
        if(editCategoryTitle.getText().toString().isEmpty() ){
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
                    editCategoryTitle.getText().toString());

            dbCategoryHandler.createCategory(category);

            //Reset all fields
            editCategoryTitle.setText("");

            Toast.makeText(getApplicationContext(), R.string.added_category, Toast.LENGTH_SHORT).show();

        }
    }

}
