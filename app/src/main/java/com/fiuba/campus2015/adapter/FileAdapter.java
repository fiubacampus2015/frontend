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
    private static final String PDF = "pdf";
    private static final String MP3 = "mp3";
    private static final String DOC = "doc";
    private static final String XLS = "xls";
    private static final String JPEG = "jpeg";
    private static final String JPG = "jpg";
    private static final String PPT = "ppt";
    private static final String MP4 = "mp4";
    private static final String ZIP = "zip";

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

    public void removeFile(int position) {
        this.fileItems.remove(position);
        notifyItemRemoved(position);
    }

    public void addFile(File file) {
        fileItems.add(file);
        notifyDataSetChanged();
    }

    public String getTypeFile(String type)
    {

        if (type.equals("file"))
            return "Archivo";
        else if (type.equals("photo"))
            return "Imagen";
        else if (type.equals("video"))
            return "Video";

        return "Otro";
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

        holder.textViewName.setText(fileItem.originalName);
        holder.textViewDescription.setText(getTypeFile(fileItem.typeOf.toString()));
        holder.fileAdapter = this;
        holder.imageSpace.setVisibility(View.GONE);

        Bitmap photoBitmap;

        if(fileItem.content != null && !fileItem.content.isEmpty()) {
            byte[] decodedString = Base64.decode(fileItem.content , Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);
            photoBitmap = Bitmap.createScaledBitmap(photoBitmap, 75, 75, true);
            holder.imageSpace.setVisibility(View.VISIBLE);

        } else {
            int pos = fileItem.originalName.lastIndexOf(".") + 1;
            String extension = fileItem.originalName.substring(pos);
            int icon;

            switch (extension) {
                case MP3:
                    icon = R.drawable.ic_file_music_grey600_48dp;
                    break;
                case MP4:
                    icon = R.drawable.ic_file_video_grey600_48dp;
                    break;
                case PDF:
                    icon = R.drawable.ic_file_pdf_grey600_48dp;
                    break;
                case DOC:
                    icon = R.drawable.ic_file_word_grey600_48dp;
                    break;
                case XLS:
                    icon = R.drawable.ic_file_excel_grey600_48dp;
                    break;
                case PPT:
                    icon = R.drawable.ic_file_powerpoint_grey600_48dp;
                    break;
                case JPG:
                    icon = R.drawable.ic_file_image_grey600_48dp;
                    break;
                case JPEG:
                    icon = R.drawable.ic_file_image_grey600_48dp;
                    break;
                default:
                    icon = R.drawable.ic_file_document_grey600_48dp;
                    break;
            }
            photoBitmap = BitmapFactory.decodeResource(context.getResources(), icon);
        }

        holder.imagePreView.setImageBitmap(photoBitmap);
    }

    public File getFile(int position)
    {
        return fileItems.get(position);
    }

    private void downloadFile(final int position) {
        final File file = fileItems.get(position);
        fragment.downloadFile(file._id, file.originalName,file.path) ;
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
        ImageView imageSpace;

        public ViewHolderFiles(View itemView) {
            super(itemView);

            imageSpace = (ImageView)itemView.findViewById(R.id.parche);
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
