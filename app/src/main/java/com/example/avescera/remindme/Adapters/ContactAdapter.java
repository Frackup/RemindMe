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
 * Created by a.vescera on 25/11/2016.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> contactsList){
        super(context,0,contactsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent,false);
        }

        ContactAdapter.ContactViewHolder viewHolder = (ContactAdapter.ContactViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ContactAdapter.ContactViewHolder();
            viewHolder.firstName = (TextView) convertView.findViewById(R.id.txtViewContactFirstName);
            viewHolder.lastName = (TextView) convertView.findViewById(R.id.txtViewContactLastName);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.txtViewContactPhone);
            viewHolder.email = (TextView) convertView.findViewById(R.id.txtViewContactEmail);
        }

        Contact contact = getItem(position);

        viewHolder.firstName.setText(contact.get_firstName());
        viewHolder.lastName.setText(contact.get_lastName());
        viewHolder.phone.setText(contact.get_phone());
        viewHolder.email.setText(contact.get_email());

        return convertView;
    }

    private class ContactViewHolder{
        public TextView firstName;
        public TextView lastName;
        public TextView phone;
        public TextView email;
    }
}
