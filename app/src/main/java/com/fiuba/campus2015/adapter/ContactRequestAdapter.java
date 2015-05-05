package com.fiuba.campus2015.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.FriendRequest;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;

import java.util.Collections;
import java.util.List;

public class ContactRequestAdapter  extends RecyclerView.Adapter<ContactRequestAdapter.ViewHolderContactsRequest> {
    private LayoutInflater layoutInflater;
    private List<User> contactsItems;
    private Context context;
    private FriendRequest friendRequest;

    public ContactRequestAdapter(FriendRequest friendRequest) {
        this.context = friendRequest;
        layoutInflater = LayoutInflater.from(context);
        this.friendRequest = friendRequest;
    }

    public void setContacts(List<User> listContacts) {
        contactsItems = listContacts;
        if(!contactsItems.isEmpty()) {
            Collections.sort(contactsItems);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolderContactsRequest onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact_request, parent, false);
        ViewHolderContactsRequest viewHolderContactsRequest = new ViewHolderContactsRequest(view);
        return viewHolderContactsRequest;
    }

    @Override
    public void onBindViewHolder(ViewHolderContactsRequest holder, int position) {
        User contactItem = contactsItems.get(position);
        holder.textViewName.setText(contactItem.name + " " + contactItem.username);
        holder.textViewEmail.setText(contactItem.email);
        holder.adapter = this;

        Bitmap icon;
        if (contactItem.personal!= null && !contactItem.personal.photo.isEmpty()) {
            byte[] decodedString = Base64.decode(contactItem.personal.photo, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            icon = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
            icon = Bitmap.createScaledBitmap(icon, 50 , 50 , true);
        }else {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_grey_48dp);
        }
        holder.photo.setImageBitmap(icon);
    }

    private void remove(int position) {
        contactsItems.remove(position);
        notifyItemRemoved(position);
    }

    private void rejectInvitation(int position) {
        User user = contactsItems.get(position);
        remove(position);
        friendRequest.setInvitationDeleted(user);
    }

    private void acceptInvitation(int position) {
        User user = contactsItems.get(position);
        remove(position);
        friendRequest.setUserInvited(user);
    }

    @Override
    public int getItemCount() {
        return contactsItems.size();
    }


    static class ViewHolderContactsRequest extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewName;
        TextView textViewEmail;
        ImageView photo;
        ButtonFloatMaterial buttonAccept;
        ButtonFloatMaterial buttonDelete;
        ContactRequestAdapter adapter;

        public ViewHolderContactsRequest(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textView2);
            textViewEmail = (TextView) itemView.findViewById(R.id.mailText_request);
            photo = (ImageView) itemView.findViewById(R.id.image_contact_request);
            buttonDelete = (ButtonFloatMaterial)itemView.findViewById(R.id.buttonDeleteInvitation);
            buttonAccept = (ButtonFloatMaterial)itemView.findViewById(R.id.buttonAcceptInvitation);

            buttonAccept.setOnClickListener(this);
            buttonDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.buttonAcceptInvitation) {
                adapter.acceptInvitation(getAdapterPosition());
            } else {
                adapter.rejectInvitation(getAdapterPosition());
            }
        }
    }
}
