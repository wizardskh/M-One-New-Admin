package com.khhs.adminpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> {

    ArrayList<SeriesModel> seriesModels = new ArrayList<>();

    Context context;
    FragmentManager fm;
    public SeriesAdapter(ArrayList<SeriesModel> seriesModels, Context context,FragmentManager fm) {
        this.seriesModels = seriesModels;
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.seriesitem,parent,false);
        SeriesHolder holder = new SeriesHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SeriesHolder holder, final int position) {

        final SeriesModel model = seriesModels.get(position);
        holder.category.setText(model.seriesCategory);
        holder.name.setText(model.seriesName);

        holder.sr.setText(position+1+"");
        Glide.with(context)
                .load(model.seriesImage)
                .into(holder.image);
        holder.btmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpisodeFragment episodeFragment = new EpisodeFragment();
                episodeFragment.seriesModel = model;
                episodeFragment.str = position+1+"";

                episodeFragment.position = position;
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.mainframe,episodeFragment);
                ft.commit();
            }
        });
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.image);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.delete_menu)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation!")
                                    .setMessage("Are you Sure To Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SeriesFragment.deleteSeries(position);
                                            Toasty.success(context,"Series Deleted Successfully!",Toasty.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();
                        }
                        if(item.getItemId()==R.id.edit_menu)
                        {
                            Series  seriespopup = new Series();
                            seriespopup.edit_model = seriesModels.get(position);
                            seriespopup.id = SeriesFragment.seriesIds.get(position);
                            seriespopup.show(fm,"Edit Series");
                        }
                        return true;
                    }
                });
                return  true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesModels.size();
    }

    public class  SeriesHolder extends RecyclerView.ViewHolder{
        TextView sr,name,category;
        ImageView image;
        Button btmore;

        public SeriesHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            image = itemView.findViewById(R.id.image);
            btmore = itemView.findViewById(R.id.btnmore);
        }
    }

}
