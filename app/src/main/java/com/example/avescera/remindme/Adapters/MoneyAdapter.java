package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.MoneyCreationActivity;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by a.vescera on 05/12/2016.
 */

public class MoneyAdapter extends ArrayAdapter<Money> {

    private Dialog dialog;
    private DatabaseContactHandler dbContactHandler;
    private DatabaseMoneyHandler dbMoneyHandler;
    private List<Money> moneyList;

    public MoneyAdapter(Context context, List<Money> _moneyList) {
        super(context,0,_moneyList);
        this.moneyList = _moneyList;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, parent, false);
        }

        MoneyViewHolder viewHolder = (MoneyViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new MoneyViewHolder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtViewMoneyTitle);
            viewHolder.txtFName = (TextView) convertView.findViewById(R.id.txtViewMoneyFirstName);
            viewHolder.txtLName = (TextView) convertView.findViewById(R.id.txtViewMoneyLastName);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txtViewMoneyAmount);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtViewMoneyDate);
            viewHolder.imgDetail = (ImageView) convertView.findViewById(R.id.imgViewMoneyDetail);
            viewHolder.imgEdit = (ImageView) convertView.findViewById(R.id.imgViewMoneyEdit);
            viewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.imgViewMoneyDelete);
        }

        final Money money = getItem(position);
        Contact contact = dbContactHandler.getContact(money.get_contactFkId());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        dialog = new Dialog(getContext());

        if (viewHolder.txtTitle != null) { viewHolder.txtTitle.setText(money.get_title()); }
        if (viewHolder.txtFName != null) { viewHolder.txtFName.setText(contact.get_firstName()); }
        if (viewHolder.txtLName != null) { viewHolder.txtLName.setText(contact.get_lastName()); }
        if (viewHolder.txtAmount != null) { viewHolder.txtAmount.setText(Float.toString(money.get_amount()) + " €"); }
        if (viewHolder.txtDate != null) { viewHolder.txtDate.setText(dateFormat.format(money.get_date())); }
        if (viewHolder.imgEdit != null) {
            viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMoneyItem(money);
                }
            });
        }
        if (viewHolder.imgDelete != null) {
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMoneyItem(money);
                }
            });
        }
        if (viewHolder.imgDetail != null) {
            viewHolder.imgDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDetailDialog(money);
                }
            });
        }

        return convertView;
    }

    private class MoneyViewHolder{
        public TextView txtTitle;
        public TextView txtFName;
        public TextView txtLName;
        public TextView txtAmount;
        public TextView txtDate;
        public ImageView imgDetail;
        public ImageView imgEdit;
        public ImageView imgDelete;
    }

    public void createDetailDialog(Money money){
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

    public void editMoneyItem(Money money){
        Intent intent = new Intent(getContext(), MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.MONEY_ITEM,money);
        if(money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (money.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    public void deleteMoneyItem(final Money money){

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_bullet_key_permission)
                // Set Dialog Title
                .setTitle(R.string.deletion_process)
                // Set Dialog Message
                .setMessage(R.string.deletion_warning)
                .setPositiveButton(R.string.positive_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        moneyList.remove(getPosition(money));
                        notifyDataSetChanged();
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
