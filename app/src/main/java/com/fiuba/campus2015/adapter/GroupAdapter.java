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
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.MemberShip;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.GroupFragment;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.gc.materialdesign.widgets.Dialog;

import java.util.Collections;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolderGroups> {
    private LayoutInflater layoutInflater;
    private List<Group> groupItems;
    private Context context;
    private String userId;
    private GroupFragment fragment;

    public GroupAdapter(Context context, GroupFragment fragment){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }


    public void setGroups(List<Group> groups,String userId) {
        this.groupItems = groups;
        this.userId = userId;
        sortMark();
        notifyDataSetChanged();
    }

    public void sortMark() {
        if(groupItems.isEmpty()) {
            return;
        }

        //TODO: orden alfabeticamente por nombre de grupo
        //Collections.sort(groupItems);
    }

    @Override
    public GroupAdapter.ViewHolderGroups onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_group, parent, false);
        ViewHolderGroups viewHolder = new ViewHolderGroups(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolderGroups holder, int position) {

        Group groupItem = groupItems.get(position);

        holder.textViewName.setText(groupItem.name);
        holder.text_description_group.setText(groupItem.description);
        holder.adapter = this;

            Bitmap  icon;
            if (groupItem.photo != null && !groupItem.photo.isEmpty())
            {
                byte[] decodedString = Base64.decode(groupItem.photo, Base64.DEFAULT);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                icon = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, options);
                icon = Bitmap.createScaledBitmap(icon, 50 , 50 , true);

            }else {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_grey_48dp);
            }
            holder.imageViewGroup.setImageBitmap(icon);

        if (groupItem.actions.get(0).action.equals("suscribe"))
        {
            holder.buttonSuscribe.setVisibility(View.VISIBLE);
        }

        if(groupItem.owner._id.equals(userId)) {
            holder.imageStar.setVisibility(View.VISIBLE);
        } else {
            holder.imageStar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return groupItems.size();
    }

    public Group getGroup(int position) {
        return groupItems.get(position);
    }

    private void suscribeToGroup(final int position) {
        final Group group = getGroup(position);
        fragment.subscribeGroup(group._id);
    }

    private void setGroupSelected(int position) {
        final Group group = getGroup(position);
        Dialog dialog2 = new Dialog(this.fragment.getActivity(), null, "Para poder ingresar a " + group.name + " primero tenés que unirte.");
        dialog2.show();
        dialog2.getButtonAccept().setText("Aceptar");
    }

    static class ViewHolderGroups extends RecyclerView.ViewHolder {
        GroupAdapter adapter;
        TextView textViewName;
        ImageView imageViewGroup;
        TextView text_description_group;
        ImageView imageStar;
        ImageView buttonSuscribe;

        public ViewHolderGroups(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.text_name_group);
            imageViewGroup = (ImageView) itemView.findViewById(R.id.image_group);
            text_description_group = (TextView)itemView.findViewById(R.id.text_description_group);
            imageStar = (ImageView) itemView.findViewById(R.id.isOwner);
            buttonSuscribe =  (ImageView) itemView.findViewById(R.id.needsSuscription);
            buttonSuscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(v.getId()) {
                        case R.id.needsSuscription:
                            adapter.suscribeToGroup(getAdapterPosition());
                            break;
                        default:
                            adapter.setGroupSelected(getAdapterPosition());
                    }
                }
            });
       }
    }
}
