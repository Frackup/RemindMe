package com.example.avescera.remindme.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.Classes.InitDataBaseHandlers;
import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.ContactExchangeActivity;
import com.example.avescera.remindme.Interfaces.ActivityClass;
import com.example.avescera.remindme.MoneyCreationActivity;
import com.example.avescera.remindme.R;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by a.vescera on 05/12/2016.
 * Allows to manage the display of a Money item within a listView, and the interaction with it.
 */

public class MoneyAdapter extends ArrayAdapter<Money> {

    private Dialog dialog;
    private InitDataBaseHandlers dbHandlers;
    private List<Money> moneyList;

    public MoneyAdapter(Context context, List<Money> _moneyList) {
        super(context,0,_moneyList);
        this.moneyList = _moneyList;

        //Initiate the DBHandler
        dbHandlers = new InitDataBaseHandlers(context);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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
        final Contact contact = dbHandlers.getDbContactHandler().getContact(money.get_contactFkId());
        final int contact_id = contact.get_id();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        dialog = new Dialog(getContext());

        if (viewHolder.txtTitle != null) {
            viewHolder.txtTitle.setText(money.get_title());
        }
        if (viewHolder.txtFName != null) {
            viewHolder.txtFName.setText(contact.get_firstName());
            //The 2 first ids are reserved to display add contact and select a contact.
            if(contact.get_id() > 2) {
                viewHolder.txtFName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToContactExchangePage(contact_id);
                    }
                });
            }
        }
        if (viewHolder.txtLName != null) {
            viewHolder.txtLName.setText(contact.get_lastName());
            if(contact.get_id() > 2) {
                viewHolder.txtLName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToContactExchangePage(contact_id);
                    }
                });
            }
        }
        //if (viewHolder.txtAmount != null) { viewHolder.txtAmount.setText(Float.toString(money.get_amount()) + " " + getContext().getResources().getString(R.string.home_currency)); }
        if (viewHolder.txtAmount != null) { viewHolder.txtAmount.setText(String.format(Locale.getDefault(),"%f", money.get_amount()) + " " + getContext().getResources().getString(R.string.home_currency)); }
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
        TextView txtTitle;
        TextView txtFName;
        TextView txtLName;
        TextView txtAmount;
        TextView txtDate;
        ImageView imgDetail;
        ImageView imgEdit;
        ImageView imgDelete;
    }

    private void createDetailDialog(Money money){
        //Custom dialog
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle(getContext().getResources().getString(R.string.dialog_detail_title));

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

    private void editMoneyItem(Money money){
        Intent intent = new Intent(getContext(), MoneyCreationActivity.class);
        intent.putExtra(ActivityClass.MONEY_ITEM,money.get_id());
        if(money.get_typeFkId() == ActivityClass.DATABASE_LOAN_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_LOAN);
        } else if (money.get_typeFkId() == ActivityClass.DATABASE_BORROW_TYPE) {
            intent.putExtra(ActivityClass.CALLING_ACTIVITY, ActivityClass.ACTIVITY_BORROW);
        }
        getContext().startActivity(intent);
    }

    private void deleteMoneyItem(final Money money){

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
                        dbHandlers.getDbMoneyHandler().deleteMoney(money, getContext());

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

    private void goToContactExchangePage(int contact_id){
        Intent intent = new Intent(getContext(), ContactExchangeActivity.class);
        intent.putExtra(ActivityClass.CONTACT_ITEM, contact_id);
        getContext().startActivity(intent);
    }
}
