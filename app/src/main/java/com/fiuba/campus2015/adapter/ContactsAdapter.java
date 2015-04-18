package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;

import java.util.Collections;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactItem> {
    private LayoutInflater layoutInflater;
    private List<ContactItem> contactsItems;
    private Context context;

    public ContactsAdapter(Context context, List<ContactItem> contactItems) {
        super(context, 0, contactItems);

        this.contactsItems = contactItems;
        sortMark();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactHolder holder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_contact, null);

            holder = new ContactHolder();
            holder.name = (TextView)convertView.findViewById(R.id.text_name_contact);
            holder.photo = (ImageView) convertView.findViewById(R.id.image_contact);
            holder.car = (TextView) convertView.findViewById(R.id.CharContact);
            holder.separator = (View) convertView.findViewById(R.id.separatorContact);

            convertView.setTag(holder);
        } else {
            holder = (ContactHolder)convertView.getTag();
        }

        ContactItem contactItem = getItem(position);
        holder.bindContact(contactItem);

        return  convertView;
    }

    public void sortMark() {
        if(contactsItems.isEmpty()) {
            return;
        }

        for(ContactItem contact: contactsItems) {
            contact.setMark(false);
        }

        Collections.sort(contactsItems);
        // se marca el primer contacto para mostrar la letra
        contactsItems.get(0).setMark(true);

        for(int i = 1; i < contactsItems.size(); i++) {
            ContactItem contactItem = contactsItems.get(i);
            // si un contacto es distinto al anterior se marca para mostrar la letra
            if(contactsItems.get(i - 1).getTitle() != contactItem.getTitle()) {
                contactItem.setMark(true);
            }
        }
    }

    private class ContactHolder {
        public TextView car;
        public TextView name;
        public ImageView photo;
        public View separator;

        public void bindContact(ContactItem contactItem) {
            name.setText(contactItem.getName());
            separator.setBackgroundColor(Color.WHITE);

            if(contactItem.getMark()) {
                car.setText(Character.toString(contactItem.getTitle()));
                if(contactsItems.get(0) != contactItem) {
                    // los item que muestran la letra y son distintos al primero muestran la division
                    separator.setBackgroundColor(Color.parseColor("#ffcfcfcf"));
                }
            } else {
                car.setText("");
            }
            /*
             Bitmap icon = BitmapFactory.decodeResource(context.getResources(),....
            photo.setImageBitmap(icon);

             */
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.wallpaper);
            photo.setImageBitmap(icon);
        }
    }
}
