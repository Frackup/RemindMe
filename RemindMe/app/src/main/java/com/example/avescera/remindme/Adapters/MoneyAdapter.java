package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Money;
import com.example.avescera.remindme.R;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by a.vescera on 22/11/2016.
 */

public class MoneyAdapter extends ArrayAdapter<Money> {

    public MoneyAdapter(Context context, List<Money> moneyList){
        super(context,0,moneyList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, parent,false);
        }

        MoneyViewHolder viewHolder = (MoneyViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MoneyViewHolder();
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.txtViewMoneyLoanFirstName);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.txtViewMoneyLoanLastName);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.txtViewMoneyLoanAmount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txtViewMoneyLoanDate);
            viewHolder.details = (ImageView) convertView.findViewById(R.id.imgViewMoneyLoanDetail);
        }

        Money money = getItem(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

        //TODO: A remettre une fois l'implémentation des BDD effectuée (id des contacts)
        //viewHolder.firstName.setText(money.get_contact().get_firstName());
        //viewHolder.lastName.setText(money.get_contact().get_lastName());
        viewHolder.amount.setText(Float.toString(money.get_amount())+" €");

        //Formating date according to the location of the device, using the defined dateFormat
        //viewHolder.date.setText(money.get_date().toString());
        viewHolder.date.setText(dateFormat.format(money.get_date()));

        return convertView;
    }

    private class MoneyViewHolder{
        public TextView firstName;
        public TextView lastName;
        public TextView amount;
        public TextView date;
        public ImageView details;
    }
}
