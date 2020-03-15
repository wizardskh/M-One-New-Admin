package com.khhs.adminpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeHolder> {

    ArrayList<EpisodeModel> models = new ArrayList<>();
    Context context;
    FragmentManager fragmentManager;

    public EpisodeAdapter(ArrayList<EpisodeModel> models, Context context, FragmentManager fragmentManager) {
        this.models = models;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public EpisodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episodeitem,parent,false);
        EpisodeHolder holder = new EpisodeHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EpisodeHolder holder, final int position) {

        holder.sr.setText(position+1+"");
        holder.name.setText(models.get(position).episodeName);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.options);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                popupMenu.show();;
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.edit_menu)
                        {

                        }
                        if(item.getItemId() == R.id.delete_menu)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation!")
                                    .setMessage("Are you Sure To Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            EpisodeFragment.deleteEpisode(position);
                                            Toasty.success(context,"Episode Deleted Successfully!",Toasty.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();
                        }
                        else
                        {
                            Movie popup = new Movie();
                            popup.edit_epModel = models.get(position);
                            popup.id = EpisodeFragment.epIds.get(position);
                            popup.show(fragmentManager,"Edit Episode");
                        }

                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class EpisodeHolder extends  RecyclerView.ViewHolder
    {
        TextView sr,name;
        ImageView options;
        public EpisodeHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.edtname);
            options = itemView.findViewById(R.id.options);

        }
    }

}
