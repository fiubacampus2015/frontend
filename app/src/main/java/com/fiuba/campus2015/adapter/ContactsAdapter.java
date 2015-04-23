package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;

import java.util.Collections;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolderContacts> {
    private LayoutInflater layoutInflater;
    private List<ContactItem> contactsItems;
    private Context context;

    //public ContactsAdapter(Context context, List<ContactItem> contactItems) {
        //super(context, 0, contactItems);

        //this.contactsItems = contactItems;
        //sortMark();
    public ContactsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setContacts(List<ContactItem> listMovies) {
        this.contactsItems = listMovies;
        sortMark();
        notifyDataSetChanged();
        //notifyItemRangeChanged(0,listMovies.size());
    }

/*
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

      //  ContactItem contactItem = getItem(position);
      //  holder.bindContact(contactItem);

        return  convertView;
    }
*/
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

    @Override
    public ContactsAdapter.ViewHolderContacts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        ViewHolderContacts viewHolder = new ViewHolderContacts(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolderContacts holder, int position) {

        ContactItem contactItem = contactsItems.get(position);

        holder.textViewName.setText(contactItem.getName());
        holder.viewSeparator.setBackgroundColor(Color.WHITE);

        if(contactItem.getMark()) {
            holder.textViewChar.setText(Character.toString(contactItem.getTitle()));
            if(contactsItems.get(0) != contactItem) {
                // los item que muestran la letra y son distintos al primero muestran la division
                holder.viewSeparator.setBackgroundColor(Color.parseColor("#ffcfcfcf"));
            }
        } else {
            holder.textViewChar.setText("");
        }
            /*
             Bitmap icon = BitmapFactory.decodeResource(context.getResources(),....
            photo.setImageBitmap(icon);

             */
        //    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.wallpaper);
        //   photo.setImageBitmap(icon);
    }

    @Override
    public int getItemCount() {
        return contactsItems.size();
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
        //    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.wallpaper);
         //   photo.setImageBitmap(icon);
        }
    }
    static class ViewHolderContacts extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewChar;
        ImageView imageViewContact;
        View viewSeparator;

        public ViewHolderContacts(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.text_name_contact);
            imageViewContact = (ImageView) itemView.findViewById(R.id.image_contact);
            textViewChar = (TextView) itemView.findViewById(R.id.CharContact);
            viewSeparator = (View) itemView.findViewById(R.id.separatorContact);
        }
    }
}
