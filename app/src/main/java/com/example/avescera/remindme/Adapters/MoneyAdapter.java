package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
/*
    public MoneyAdapter(Context context, List<Money> moneysList){
        super(context,0,moneysList);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, parent,false);
        }

        MoneyViewHolder viewHolder = (MoneyViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MoneyViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txtViewMoneyTitle);
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.txtViewMoneyFirstName);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.txtViewMoneyLastName);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.txtViewMoneyAmount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txtViewMoneyDate);
            viewHolder.details = (ImageView) convertView.findViewById(R.id.imgViewMoneyDetail);
        }

        Money money = getItem(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

        Contact contact = dbContactHandler.getContact(money.get_contactFkId());

        viewHolder.title.setText(money.get_title());
        viewHolder.firstName.setText(contact.get_firstName());
        viewHolder.lastName.setText(contact.get_lastName());
        viewHolder.amount.setText(Float.toString(money.get_amount())+" €");
        viewHolder.date.setText(dateFormat.format(money.get_date()));

        return convertView;
    }

    private class MoneyViewHolder{
        public TextView title;
        public TextView firstName;
        public TextView lastName;
        public TextView amount;
        public TextView date;
        public ImageView details;
    }
*/
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

    public MoneyAdapter(Context context, int resource, List<Money> categoriesList) {
        super(context,resource,categoriesList);

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
        Money money = getItem(position);
        Contact contact = dbContactHandler.getContact(money.get_contactFkId());

        if (money != null) {


            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtViewMoneyTitle);
            TextView txtFName = (TextView) convertView.findViewById(R.id.txtViewMoneyFirstName);
            TextView txtLName = (TextView) convertView.findViewById(R.id.txtViewMoneyLastName);
            TextView txtAmount = (TextView) convertView.findViewById(R.id.txtViewMoneyAmount);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtViewMoneyDate);
            ImageView imgDetail = (ImageView) convertView.findViewById(R.id.imgViewMoneyDetail);

            if (txtTitle != null) { txtTitle.setText(money.get_title()); }
            if (txtFName != null) { txtFName.setText(contact.get_firstName()); }
            if (txtLName != null) { txtLName.setText(contact.get_lastName()); }
            if (txtAmount != null) { txtAmount.setText(Float.toString(money.get_amount())); }
            if (txtDate != null) { txtDate.setText(dateFormat.format(money.get_date())); }
            //TODO : implémenter le click sur l'image pour ouvrir une pop-up et afficher le détail du prêt/emprunt
        }
        return convertView;
    }
}
