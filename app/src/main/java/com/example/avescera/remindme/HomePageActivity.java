package com.example.avescera.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Interfaces.ActivityClass;

import static android.graphics.Color.BLUE;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private RelativeLayout RLayoutLoanAmnt;
    private RelativeLayout RLayoutLoanQty;
    private RelativeLayout RLayoutBorrowedAmnt;
    private RelativeLayout RLayoutBorrowedQty;
    private TextView txtVLoanAmnt;
    private TextView txtVLoanQty;
    private TextView txtVBorrowedAmnt;
    private TextView txtVBorrowedQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        attachViewItems();
        initVariables();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreateItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogItemCreation();
            }
        });

        RLayoutLoanAmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityList(ActivityClass.ACTIVITY_LOAN, MoneyListActivity.class);
            }
        });

        RLayoutLoanQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityList(ActivityClass.ACTIVITY_LOAN, ObjectListActivity.class);
            }
        });

        RLayoutBorrowedAmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityList(ActivityClass.ACTIVITY_BORROW, MoneyListActivity.class);
            }
        });

        RLayoutBorrowedQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityList(ActivityClass.ACTIVITY_BORROW, ObjectListActivity.class);
            }
        });
    }

    private void attachViewItems() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
/*
        btnMoneyLoan = (Button) findViewById(R.id.btnMoneyLoan);
        btnMoneyBorrowed = (Button) findViewById(R.id.btnMoneyBorrow);
        btnObjectLoan = (Button) findViewById(R.id.btnObjectLoan);
        btnObjectBorrowed =(Button) findViewById(R.id.btnObjectBorrow);
*/
        RLayoutLoanAmnt = (RelativeLayout) findViewById(R.id.RLayoutHPLoanAmnt);
        RLayoutLoanQty = (RelativeLayout) findViewById(R.id.RLayoutHPLoanQty);
        RLayoutBorrowedAmnt = (RelativeLayout) findViewById(R.id.RLayoutHPBorrowedAmnt);
        RLayoutBorrowedQty = (RelativeLayout) findViewById(R.id.RLayoutHPBorrowedQty);

        txtVLoanAmnt = (TextView) findViewById(R.id.txtVHPLoanAmnt);
        txtVLoanQty = (TextView) findViewById(R.id.txtVHPLoanQty);
        txtVBorrowedAmnt = (TextView) findViewById(R.id.txtVHPBorrowedAmnt);
        txtVBorrowedQty = (TextView) findViewById(R.id.txtVHPBorrowedQty);
    }

    private void initVariables(){
        InitDataBaseHandlers dbHandlers = new InitDataBaseHandlers(this);
/*
        btnMoneyLoan.setText(dbHandlers.getDbMoneyHandler().getTotalAmountByType(ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_currency));
        btnMoneyBorrowed.setText(dbHandlers.getDbMoneyHandler().getTotalAmountByType(ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_currency));
        btnObjectLoan.setText(dbHandlers.getDbObjectHandler().getTotalQtyByType(ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
        btnObjectBorrowed.setText(dbHandlers.getDbObjectHandler().getTotalQtyByType(ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
*/
        txtVLoanAmnt.setText(dbHandlers.getDbMoneyHandler().getTotalAmountByType(ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_currency));
        txtVLoanQty.setText(dbHandlers.getDbObjectHandler().getTotalQtyByType(ActivityClass.DATABASE_LOAN_TYPE) + " " + getResources().getText(R.string.home_objects));
        txtVBorrowedAmnt.setText(dbHandlers.getDbMoneyHandler().getTotalAmountByType(ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_currency));
        txtVBorrowedQty.setText(dbHandlers.getDbObjectHandler().getTotalQtyByType(ActivityClass.DATABASE_BORROW_TYPE) + " " + getResources().getText(R.string.home_objects));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void goToActivityList(String type, Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra(ActivityClass.CALLING_ACTIVITY, type);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //TODO : Implement within the menu the reminders settings (auto-remind...)
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set_reminders) {
            Intent intent = new Intent(this, RemindersActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_drawer_contact) {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_drawer_category) {
            Intent intent = new Intent(this, CategoryListActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_drawer_statistic) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            //TODO : Follow the tutorial to add bar charts for statistics (define which statistics)
        } else if (id == R.id.nav_drawer_contact_us) {
            sendEmail();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initVariables();
    }

    private void dialogItemCreation(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Set Dialog Icon
                .setIcon(android.R.drawable.ic_menu_edit)
                // Set Dialog Title
                .setTitle(R.string.home_item_creation_title)
                // Set Dialog Message
                .setMessage(R.string.home_item_creation)
                .setPositiveButton(R.string.home_item_creation_money, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MoneyCreationActivity.class);
                        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);

                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.home_item_creation_object, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ObjectCreationActivity.class);
                        intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);

                        startActivity(intent);
                    }
                }).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(BLUE);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(BLUE);
            }
        });
        alertDialog.show();
    }

    private void sendEmail(){
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.setType("plain/text");
        String emailAdress = "frackupdev@outlook.com";
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAdress});

        /* Send it off to the Activity-Chooser */
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
