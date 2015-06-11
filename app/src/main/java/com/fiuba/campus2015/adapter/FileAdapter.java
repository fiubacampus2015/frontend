package com.fiuba.campus2015.adapter;


import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.fragments.GroupFilesFragment;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter  extends RecyclerView.Adapter<FileAdapter.ViewHolderFiles>{
    private LayoutInflater layoutInflater;
    private List<File> fileItems;
    private Context context;
    private GroupFilesFragment fragment;

    public FileAdapter(Context context, GroupFilesFragment fragment) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
        fileItems = new ArrayList<>();
    }

    public void setFiles(List<File> files) {
        this.fileItems = files;
        notifyDataSetChanged();
    }

    public void addFile(File file) {
        fileItems.add(file);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderFiles onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_file, parent, false);
        ViewHolderFiles viewHolder = new ViewHolderFiles(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderFiles holder, int position) {
        File fileItem = fileItems.get(position);

        holder.textViewName.setText(fileItem.name);
        holder.textViewDescription.setText(fileItem.description);
        holder.fileAdapter = this;

        Bitmap photoBitmap;

        if(fileItem.preview != null && !fileItem.preview.isEmpty()) {
            byte[] decodedString = Base64.decode(fileItem.preview , Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);
            photoBitmap = Bitmap.createScaledBitmap(photoBitmap, 50, 50, true);

        } else {
            // se podria cambiar de icono segun el archivo pdf txt, etc
            photoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_insert_drive_file_grey600_36dp);
        }

        holder.imagePreView.setImageBitmap(photoBitmap);
    }

    private void downloadFile(final int position) {
        final File file = fileItems.get(position);
        String name = fileItems.get(position).name;
        fragment.downloadFile(file._id, name) ;
    }

    @Override
    public int getItemCount() {
        return fileItems.size();
    }



    static class ViewHolderFiles extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        ImageView imagePreView;
        ImageView buttonDownload;
        FileAdapter fileAdapter;

        public ViewHolderFiles(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.file_title);
            textViewDescription = (TextView) itemView.findViewById(R.id.textView16);
            imagePreView = (ImageView) itemView.findViewById(R.id.image_preview_file);
            buttonDownload = (ImageView) itemView.findViewById(R.id.imageView25);

            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileAdapter.downloadFile(getAdapterPosition());
                }
            });
        }
    }
}
