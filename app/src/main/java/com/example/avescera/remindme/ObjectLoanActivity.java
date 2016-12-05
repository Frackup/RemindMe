package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.avescera.remindme.Adapters.ObjectAdapter;
import com.example.avescera.remindme.Classes.Object;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import java.sql.SQLException;
import java.util.List;

public class ObjectLoanActivity extends AppCompatActivity {

    private DatabaseObjectHandler dbObjectHandler;
    private ListView listViewObjectLoan;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_loan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachViewItems();
        initDbHandlers();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateLoanObject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToObjectCreationPage(view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDbHandlers(){
        //Initiate the DBHandler
        dbObjectHandler = new DatabaseObjectHandler(this);
        try {
            dbObjectHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void attachViewItems(){
        listViewObjectLoan = (ListView) findViewById(R.id.listViewObjectLoanItems);
    }

    private void populateListView(){
        List<Object> listObjectItems = dbObjectHandler.getTypeObjects(ActivityClass.DATABASE_LOAN_TYPE);

        ObjectAdapter adapter = new ObjectAdapter(this, listObjectItems);
        listViewObjectLoan.setAdapter(adapter);
    }

    public void goToObjectCreationPage(View view) {
        Intent intent = new Intent(this, ObjectCreationActivity.class);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDbHandlers();
        populateListView();
    }

    private void deleteItem(final Object deleteObject) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbObjectHandler.deleteObject(deleteObject, context);

                        Toast.makeText(getApplicationContext(), R.string.deletion_confirmation, Toast.LENGTH_SHORT).show();
                        populateListView();
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
