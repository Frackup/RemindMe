package com.example.avescera.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avescera.remindme.Classes.Contact;
import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.R;

import java.util.List;

/**
 * Created by a.vescera on 23/11/2016.
 */

public class ContactSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> listContacts;
    private LayoutInflater inflater;
    private DatabaseContactHandler dbContactHandler;

    public ContactSpinnerAdapter(Context applicationContext, List<Contact> listContacts) {
        this.context = applicationContext;
        this.listContacts = listContacts;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listContacts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null){
            inflater = LayoutInflater.from(context);

            view = inflater.inflate(R.layout.contact_spinner_item, null);
        }

        Contact contact = dbContactHandler.getContact(position);

        TextView names = (TextView) view.findViewById(R.id.txtViewContactSpinnerFirstName);
        names.setText(contact.get_firstName() + " " + contact.get_lastName());
        return view;
    }
}
