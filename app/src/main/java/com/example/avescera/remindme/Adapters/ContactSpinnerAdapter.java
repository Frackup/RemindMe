package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.R;

import java.util.List;

/**
 * Created by Frackup on 26/11/2016.
 */

public class ContactSpinnerAdapter extends ArrayAdapter<Contact> {

    public ContactSpinnerAdapter(Context context, List<Contact> contactsList){
        super(context, 0, contactsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
/*
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_spinner_item, parent, false);
        }

        ContactSpinnerAdapter.ContactSpinnerViewHolder viewHolder = (ContactSpinnerAdapter.ContactSpinnerViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new ContactSpinnerAdapter.ContactSpinnerViewHolder();
            viewHolder.fName = (TextView) convertView.findViewById(R.id.txtViewContactSpinnerFirstName);
            viewHolder.lName = (TextView) convertView.findViewById(R.id.txtViewContactSpinnerLastName);
        }

        Contact contact = getItem(position);

        viewHolder.fName.setText(contact.get_firstName());
        viewHolder.lName.setText(contact.get_lastName());
*/
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_spinner_item_new, parent, false);
        }

        ContactSpinnerAdapter.ContactSpinnerViewHolder viewHolder = (ContactSpinnerAdapter.ContactSpinnerViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new ContactSpinnerAdapter.ContactSpinnerViewHolder();
            viewHolder.fName = (TextView) convertView.findViewById(R.id.txtViewContactSpinnerFirstName);
            //viewHolder.lName = (TextView) convertView.findViewById(R.id.txtViewContactSpinnerLastName);
        }

        Contact contact = getItem(position);

        viewHolder.fName.setText(contact.get_firstName());

        return convertView;
    }

    private class ContactSpinnerViewHolder{
        public TextView fName;
        //public TextView lName;
    }
}
