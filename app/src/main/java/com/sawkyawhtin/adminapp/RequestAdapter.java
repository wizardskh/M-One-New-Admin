package com.sawkyawhtin.adminapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestAdapter  extends RecyclerView.Adapter<RequestAdapter.RequestHolder> {

    ArrayList<RequestModel> requestModels = new ArrayList<>();
    Context context;


    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestitem,parent,false);

        return new RequestHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {

        holder.sr.setText(position+1+"");
        holder.name.setText(requestModels.get(position).name);
        try {
            Glide.with(context)
                    .load(requestModels.get(position).imagelink)
                    .into(holder.image);
        }
        catch (Exception ex)
        {
            Log.e("Exception",ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return requestModels.size();
    }

    public RequestAdapter(ArrayList<RequestModel> requestModels, Context context) {
        this.requestModels = requestModels;
        this.context = context;
    }

    public class RequestHolder extends RecyclerView.ViewHolder{

        TextView sr,name;
        ImageView image;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }
}
