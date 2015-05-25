package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Forum;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolderForums> {
    private LayoutInflater layoutInflater;
    private List<Forum> forumItems;
    private Context context;
    private String userId;

    public ForumAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setForums(List<Forum> groups,String userId) {
        this.forumItems = groups;
        this.userId = userId;
        sortMark();
        notifyDataSetChanged();
    }

    public void sortMark() {
        if(forumItems.isEmpty()) {
            return;
        }

        //TODO: orden alfabeticamente por nombre de foro
        //Collections.sort(groupItems);
    }

    @Override
    public ForumAdapter.ViewHolderForums onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_forum, parent, false);
        ViewHolderForums viewHolder = new ViewHolderForums(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForumAdapter.ViewHolderForums holder, int position) {

        Forum forumItem = forumItems.get(position);

        holder.textViewTitle.setText(forumItem.title);
        holder.textLastMsg.setText("");

        holder.viewSeparator.setBackgroundColor(Color.WHITE);

        if(forumItems.get(0) != forumItem) {
                // los item que muestran la letra y son distintos al primero muestran la division
                holder.viewSeparator.setBackgroundColor(Color.parseColor("#ffcfcfcf"));
            }

    }

    @Override
    public int getItemCount() {
        return forumItems.size();
    }

    public Forum getForum(int position)
    {
        return forumItems.get(position);
    }


    static class ViewHolderForums extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewForum;
        View viewSeparator;
        TextView textLastMsg;

        public ViewHolderForums(View itemView) {
            super(itemView);
            textViewTitle = (TextView)itemView.findViewById(R.id.forum_title);
            imageViewForum = (ImageView) itemView.findViewById(R.id.image_forum);
            viewSeparator = (View) itemView.findViewById(R.id.separatorForum);
            textLastMsg = (TextView)itemView.findViewById(R.id.lastMsgText);

        }
    }
}
