package com.tiberiuciuc.projectbday;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.MyViewHolder> {

    private int[] list;
    private Context context;

    public RecyclerAdaptor(int[] list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, context, list);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int singleItem = list[position];
//        holder.imageView.setImageResource(singleItem);
        Glide.with(context).load(singleItem).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        Context context;
        int[] list;

        public MyViewHolder(@NonNull View itemView, Context context, int[] list) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
            this.context = context;
            this.list = list;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("saveAndShareClickable", true);
            intent.putExtra("image_id", list[getAdapterPosition()]);
//            Toast.makeText(context, "Image selected: " + list[getAdapterPosition()], Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        }
    }
}
