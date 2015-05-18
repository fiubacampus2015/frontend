package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.ContactFragment;
import com.fiuba.campus2015.services.RestClient;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.util.Collections;
import java.util.List;

import retrofit.client.Response;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolderContacts> {
    private LayoutInflater layoutInflater;
    private List<User> contactsItems;
    private Context context;
    private String userId;
    private ContactFragment contactFragment;

    public ContactsAdapter(Context context, ContactFragment contactFragment){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contactFragment =contactFragment;
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

        Collections.sort(contactsItems);
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
        holder.adapter = this;
        holder.viewSeparator.setBackgroundColor(Color.WHITE);

        if(contactsItems.get(0) != contactItem) {
            // los item que muestran la letra y son distintos al primero muestran la division
            holder.viewSeparator.setBackgroundColor(Color.parseColor("#ffcfcfcf"));
        }

        //Valido si es amigo o no
        if (contactItem.status == null || (contactItem.status!= null && contactItem.status.equals("reject")))
        {
            holder.viewSendInvitation.setVisibility(View.VISIBLE);
            holder.viewDeleteContact.setVisibility(View.GONE);
        }else {
            holder.viewSendInvitation.setVisibility(View.GONE);
            holder.viewDeleteContact.setVisibility(View.VISIBLE);
        }

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

    private void removeContact(final int position) {
        final User contact = getContact(position);
        Dialog dialog = new Dialog(contactFragment.getActivity(),null, "Esta seguro que desea eliminar su contacto");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsItems.remove(position);
                notifyItemRemoved(position);
                contactFragment.removeContact(contact);
            }
        });
        dialog.addCancelButton("Cancelar");
        dialog.show();
        dialog.getButtonAccept().setText("Aceptar");


    }

    private void setUserSelected(int position) {
        contactFragment.loadProfile(getContact(position));
    }

    static class ViewHolderContacts extends RecyclerView.ViewHolder  implements View.OnClickListener {
        ContactsAdapter adapter;
        TextView textViewName;
        ImageView imageViewContact;
        View viewSeparator;
        ImageView viewSendInvitation;
        ImageView viewDeleteContact;
        TextView textMail;

        public ViewHolderContacts(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.text_name_contact);
            imageViewContact = (ImageView) itemView.findViewById(R.id.image_contact);
            viewSeparator = (View) itemView.findViewById(R.id.separatorContact);
            viewSendInvitation = (ImageView) itemView.findViewById(R.id.sendInvitation);
            viewDeleteContact = (ImageView) itemView.findViewById(R.id.deleteContact);
            textMail = (TextView)itemView.findViewById(R.id.mailText);
            itemView.setOnClickListener(this);
            viewDeleteContact.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.deleteContact:
                    adapter.removeContact(getAdapterPosition());
                    break;
                default:
                    adapter.setUserSelected(getAdapterPosition());
            }
        }
    }


}
