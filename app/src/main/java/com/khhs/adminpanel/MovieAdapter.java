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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
    Context context;
    FragmentManager fm;

    public MovieAdapter(ArrayList<MovieModel> movieModels, Context context, FragmentManager fm) {
        this.movieModels = movieModels;
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitem,parent,false);
        MovieHolder holder = new MovieHolder(view);

        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, final int position) {

        MovieModel model = movieModels.get(position);
        holder.sr.setText(position+1+"");
        holder.name.setText(model.movieName);
        holder.category.setText(model.movieCategory);
        Glide.with(context)
                .load(model.movieImage)
                .into(holder.image);
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.image);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                popupMenu.show();;
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete_menu)
                        {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation!")
                                    .setMessage("Are you Sure To Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MovieFragment.deleteMovies(position);
                                            Toasty.success(context,"Movie Deleted Successfully!",Toasty.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();

                        }
                        if(item.getItemId() == R.id.edit_menu)
                        {

                            Movie popup = new Movie();
                            popup.edit_movieModel = movieModels.get(position);
                            popup.id = MovieFragment.movieIds.get(position);
                            popup.show(fm,"Edit Movie");


                        }
                        return true;
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{

        TextView sr,name,category;
        ImageView image;
        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            image = itemView.findViewById(R.id.image);
        }
    }
}
