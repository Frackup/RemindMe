package com.example.avescera.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.avescera.remindme.Adapters.CategoryAdapter;
import com.example.avescera.remindme.Classes.Category;
import com.example.avescera.remindme.DBHandlers.DatabaseCategoryHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;

import java.sql.SQLException;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private List<Category> categoriesList;
    private DatabaseCategoryHandler dbCategoryHandler;
    private ListView categoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoriesListView = (ListView) findViewById(R.id.listViewCategories);

        //Initiate the DBHandler
        dbCategoryHandler = new DatabaseCategoryHandler(this);
        try {
            dbCategoryHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        categoriesList = dbCategoryHandler.getAllCategories();
        //Working, trying the old way (contact, object, ...)
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, R.layout.category_list_item, categoriesList);
        categoriesListView.setAdapter(categoryAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
