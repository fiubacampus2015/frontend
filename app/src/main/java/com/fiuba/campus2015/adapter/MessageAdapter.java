package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.gc.materialdesign.views.CheckBox;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolderContacts> {
    private LayoutInflater layoutInflater;
    private List<Message> messageItems;
    private Context context;
    private String userId;

    public MessageAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setMessage(List<Message> listMsgs,String userId) {
        this.messageItems = listMsgs;
        this.userId = userId;
        notifyDataSetChanged();
    }

    @Override
    public MessageAdapter.ViewHolderContacts onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        ViewHolderContacts viewHolder = new ViewHolderContacts(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolderContacts holder, int position) {

        Message msgItem = messageItems.get(position);

        //holder.textViewUser.setText(msgItem.user.name);
        //holder.textContent.setText(msgItem.content);

        holder.viewSeparator.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    public User getMsg(int position) {
        return null;
    }


    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_msg, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mCheckBox.setChecked(true);
        /*
		viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
				// Do something awesome!
			}
		});
		*/

        return convertView;
    }

    static class ViewHolder {
        CheckBox mCheckBox;
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
            //textViewUser = (TextView)itemView.findViewById(R.id.text_name_contact);
            //textContent = (TextView)itemView.findViewById(R.id.mailText);

        }
    }
}
