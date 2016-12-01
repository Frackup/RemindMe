package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.MoneyBorrowActivity;
import com.example.avescera.remindme.MoneyCreationActivity;
import com.example.avescera.remindme.MoneyLoanActivity;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by a.vescera on 22/11/2016.
 */

public class MoneyAdapter extends ArrayAdapter<Money> {

    private DatabaseContactHandler dbContactHandler;
    private DatabaseMoneyHandler dbMoneyHandler;
    private Dialog dialog;
    private Money money;

    public MoneyAdapter(Context context, int textViewResourceId) {
        super(context,textViewResourceId);

        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(context);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MoneyAdapter(Context context, int resource, List<Money> moneyList) {
        super(context,resource,moneyList);

        //Initiate the DBHandler
        dbContactHandler = new DatabaseContactHandler(context);
        try {
            dbContactHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbMoneyHandler = new DatabaseMoneyHandler(context);
        try {
            dbMoneyHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, null);
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        money = getItem(position);
        Contact contact = dbContactHandler.getContact(money.get_contactFkId());

        if (money != null) {
            dialog = new Dialog(getContext());

            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtViewMoneyTitle);
            TextView txtFName = (TextView) convertView.findViewById(R.id.txtViewMoneyFirstName);
            TextView txtLName = (TextView) convertView.findViewById(R.id.txtViewMoneyLastName);
            TextView txtAmount = (TextView) convertView.findViewById(R.id.txtViewMoneyAmount);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtViewMoneyDate);
            ImageView imgDetail = (ImageView) convertView.findViewById(R.id.imgViewMoneyDetail);
            ImageView imgEdit = (ImageView) convertView.findViewById(R.id.imgViewMoneyEdit);
            ImageView imgDelete = (ImageView) convertView.findViewById(R.id.imgViewMoneyDelete);

            if (txtTitle != null) { txtTitle.setText(money.get_title()); }
            if (txtFName != null) { txtFName.setText(contact.get_firstName()); }
            if (txtLName != null) { txtLName.setText(contact.get_lastName()); }
            if (txtAmount != null) { txtAmount.setText(Float.toString(money.get_amount()) + " €"); }
            if (txtDate != null) { txtDate.setText(dateFormat.format(money.get_date())); }
            if (imgEdit != null) {
                imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editMoneyItem();
                    }
                });
            }
            if (imgDelete != null) {
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMoneyItem();
                    }
                });
            }
            if (imgDetail != null) {
                imgDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDetailDialog();
                    }
                });
            }
        }
        return convertView;
    }

    public void createDetailDialog(){
        //Custom dialog
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle(getContext().getResources().getString(R.string.dialog_detail_title).toString());

        //set the custom dialog component
        TextView txtDetail = (TextView) dialog.findViewById(R.id.txtViewDetailDisplay);
        FloatingActionButton fabDetail = (FloatingActionButton) dialog.findViewById(R.id.fabDetailOK);

        if (txtDetail != null) { txtDetail.setText(money.get_details()); }

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void editMoneyItem(){
        Intent intent = new Intent(getContext(), MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.MONEY_ITEM,money);
        if(money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (money.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    public void deleteMoneyItem(){

        if(money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            Intent intent = new Intent(getContext(), MoneyLoanActivity.class);
            intent.putExtra(ActivityClass.ACTIVITY_DELETE, ActivityClass.ACTIVITY_DELETE);
            intent.putExtra(ActivityClass.MONEY_ITEM, money);
            getContext().startActivity(intent);
        } else if (money.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            Intent intent = new Intent(getContext(), MoneyBorrowActivity.class);
            intent.putExtra(ActivityClass.ACTIVITY_DELETE, ActivityClass.ACTIVITY_DELETE);
            intent.putExtra(ActivityClass.MONEY_ITEM, money);
            getContext().startActivity(intent);
        }

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbMoneyHandler.deleteMoney(money, getContext());

                        Toast.makeText(getContext(), R.string.deletion_confirmation, Toast.LENGTH_SHORT).show();
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
