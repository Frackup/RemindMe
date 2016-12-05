package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.R;

import java.util.List;

/**
 * Created by a.vescera on 05/12/2016.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> _contactList) {
        super(context,0,_contactList);

        this.contactList = _contactList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.money_list_item, parent, false);
        }

        ContactAdapter.ContactViewHolder viewHolder = (ContactAdapter.ContactViewHolder) convertView.getTag();
        if(viewHolder == null) {
            viewHolder = new ContactAdapter.ContactViewHolder();
            viewHolder.txtContactFName = (TextView) convertView.findViewById(R.id.txtViewContactFirstName);
            viewHolder.txtContactLName = (TextView) convertView.findViewById(R.id.txtViewContactLastName);
            viewHolder.txtContactPhone = (TextView) convertView.findViewById(R.id.txtViewContactPhone);
            viewHolder.txtContactEmail = (TextView) convertView.findViewById(R.id.txtViewContactEmail);
        }

        final Contact contact = getItem(position);

        if (viewHolder.txtContactFName != null) { viewHolder.txtContactFName.setText(contact.get_firstName()); }
        if (viewHolder.txtContactLName != null) { viewHolder.txtContactLName.setText(contact.get_lastName()); }
        if (viewHolder.txtContactPhone != null) { viewHolder.txtContactPhone.setText(contact.get_phone()); }
        if (viewHolder.txtContactEmail != null) { viewHolder.txtContactEmail.setText(contact.get_email()); }

        return convertView;
    }

    private class ContactViewHolder{
        public TextView txtContactFName;
        public TextView txtContactLName;
        public TextView txtContactPhone;
        public TextView txtContactEmail;
    }
}
