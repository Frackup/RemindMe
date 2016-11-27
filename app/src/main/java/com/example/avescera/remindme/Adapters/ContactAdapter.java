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
    public ContactAdapter(Context context, int textViewResourceId) {
        super(context,textViewResourceId);
    }

    public ContactAdapter(Context context, int resource, List<Contact> contactsList) {
        super(context,resource,contactsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, null);
        }

        Contact contact = getItem(position);

        if (contact != null) {


            TextView txtContactFName = (TextView) convertView.findViewById(R.id.txtViewContactFirstName);
            TextView txtContactLName = (TextView) convertView.findViewById(R.id.txtViewContactLastName);
            TextView txtContactPhone = (TextView) convertView.findViewById(R.id.txtViewContactPhone);
            TextView txtContactEmail = (TextView) convertView.findViewById(R.id.txtViewContactEmail);

            if (txtContactFName != null) { txtContactFName.setText(contact.get_firstName()); }
            if (txtContactLName != null) { txtContactLName.setText(contact.get_lastName()); }
            if (txtContactPhone != null) { txtContactPhone.setText(contact.get_phone()); }
            if (txtContactEmail != null) { txtContactEmail.setText(contact.get_email()); }
        }
        return convertView;
    }
}
