package com.example.avescera.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;

import java.sql.SQLException;

public class ObjectCreationActivity extends AppCompatActivity {

    private DatabaseObjectHandler dbObjectHandler;
    private DatabaseContactHandler dbContactHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_creation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initiate the DBHandlers
        dbContactHandler = new DatabaseContactHandler(this);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSaveObject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save Object to DataBase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    //TODO : Implement a check button in the action bar to save the newly created Object item

}
