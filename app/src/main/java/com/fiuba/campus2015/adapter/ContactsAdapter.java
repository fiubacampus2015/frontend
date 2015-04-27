package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;

import java.util.Collections;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolderContacts> {
    private LayoutInflater layoutInflater;
    private List<User> contactsItems;
    private Context context;
    private String userId;

    public ContactsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setContacts(List<User> listContacts,String userId) {
        this.contactsItems = listContacts;
        this.userId = userId;
        sortMark();
        notifyDataSetChanged();
        //notifyItemRangeChanged(0,listMovies.size());
    }

    public void sortMark() {
        if(contactsItems.isEmpty()) {
            return;
        }

        //for(ContactItem contact: contactsItems) {
        //    contact.setMark(false);
        //}

        Collections.sort(contactsItems);
        // se marca el primer contacto para mostrar la letra
        //contactsItems.get(0).setMark(true);

        /*for(int i = 1; i < contactsItems.size(); i++) {
            ContactItem contactItem = contactsItems.get(i);
            // si un contacto es distinto al anterior se marca para mostrar la letra
            if(contactsItems.get(i - 1).getTitle() != contactItem.getTitle()) {
                contactItem.setMark(true);
            }
        }*/
    }

    @Override
    public ContactsAdapter.ViewHolderContacts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        ViewHolderContacts viewHolder = new ViewHolderContacts(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolderContacts holder, int position) {

        User contactItem = contactsItems.get(position);

        holder.textViewName.setText(contactItem.name + " "+ contactItem.username);
        holder.textMail.setText(contactItem.email);
        //holder.textCountry.setText(contactItem.personal.nacionality);

        holder.viewSeparator.setBackgroundColor(Color.WHITE);

        //if(contactItem.getMark()) {
            //holder.textViewChar.setText(Character.toString(contactItem.getTitle()));
            if(contactsItems.get(0) != contactItem) {
                // los item que muestran la letra y son distintos al primero muestran la division
                holder.viewSeparator.setBackgroundColor(Color.parseColor("#ffcfcfcf"));
            }
           if (!contactItem.contacts.contains(userId)) {
              holder.viewSendInvitation.setVisibility(View.VISIBLE);
           }else
              holder.viewSendInvitation.setVisibility(View.GONE);


        //} else {
            //holder.textViewChar.setText("");
        //}
            /*
             Bitmap icon = BitmapFactory.decodeResource(context.getResources(),....
            photo.setImageBitmap(icon);

             */
            Bitmap  icon;
            if (contactItem.personal!= null && !contactItem.personal.photo.isEmpty())
            {
                byte[] decodedString = Base64.decode(contactItem.personal.photo, Base64.DEFAULT);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                icon = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
                icon = Bitmap.createScaledBitmap(icon, 50 , 50 , true);

            }else {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_grey_48dp);
            }
            holder.imageViewContact.setImageBitmap(icon);
    }

    @Override
    public int getItemCount() {
        return contactsItems.size();
    }

    public User getContact(int position) {
        return contactsItems.get(position);
    }


    static class ViewHolderContacts extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewChar;
        ImageView imageViewContact;
        View viewSeparator;
        ImageView viewSendInvitation;
        TextView textCountry;
        TextView textMail;




        public ViewHolderContacts(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.text_name_contact);
            imageViewContact = (ImageView) itemView.findViewById(R.id.image_contact);
            //textViewChar = (TextView) itemView.findViewById(R.id.CharContact);
            viewSeparator = (View) itemView.findViewById(R.id.separatorContact);
            viewSendInvitation = (ImageView) itemView.findViewById(R.id.sendInvitation);
            //textCountry = (TextView)itemView.findViewById(R.id.countryText);
            textMail = (TextView)itemView.findViewById(R.id.mailText);

        }
    }
}
