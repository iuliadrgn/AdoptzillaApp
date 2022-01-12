package com.example.adoptzilla;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Entity.Upload;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;
    private StorageReference storageReference;
    private String filename;
    private String imageName;
    private String imgExtension;
    private String[] imgParts;




    public ImageAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;

    }

    @NonNull

    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.pet_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.petName.setText(uploadCurrent.getPetName());
        holder.petAge.setText(uploadCurrent.getPetAge());

        filename = uploadCurrent.getImgName();
        imgParts = filename.split("\\.");
        imageName = imgParts[0];
        imgExtension = imgParts[1];

        storageReference= FirebaseStorage.getInstance().getReference().child("uploads/" + filename);
        try {
            final File localFile = File.createTempFile(imageName,imgExtension);
            storageReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.imgName.setImageBitmap(bitmap);

                    }).addOnFailureListener(e -> Toast.makeText(mContext,"error loading data",Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button seeMore = holder.seeMore;
        seeMore.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SeeSpecificAd.class);
            Bundle info = new Bundle();
            info.putString("imgName",uploadCurrent.getImgName());
            info.putString("petName",uploadCurrent.getPetName());
            info.putString("petDescription",uploadCurrent.getPetDescription());
            intent.putExtras(info);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView petName, petAge;
        ImageView imgName;
        Button seeMore;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            petName = itemView.findViewById(R.id.pet_name);
            imgName = itemView.findViewById(R.id.imageViewPet);
            petAge = itemView.findViewById(R.id.petAge);

            seeMore = itemView.findViewById(R.id.more);
        }
    }
}
