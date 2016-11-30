package com.example.avescera.remindme.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
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
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;
import com.example.avescera.remindme.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

/**
 * Created by a.vescera on 22/11/2016.
 */

public class MoneyAdapter extends ArrayAdapter<Money> {

    private DatabaseContactHandler dbContactHandler;
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

            if (txtTitle != null) { txtTitle.setText(money.get_title()); }
            if (txtFName != null) { txtFName.setText(contact.get_firstName()); }
            if (txtLName != null) { txtLName.setText(contact.get_lastName()); }
            if (txtAmount != null) { txtAmount.setText(Float.toString(money.get_amount()) + " €"); }
            if (txtDate != null) { txtDate.setText(dateFormat.format(money.get_date())); }
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
}
