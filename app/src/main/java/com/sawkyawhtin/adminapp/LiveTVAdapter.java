package com.sawkyawhtin.adminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LiveTVAdapter extends RecyclerView.Adapter<LiveTVAdapter.LiveTVHolder> {
    ArrayList<LiveTvModel> liveTvModels = new ArrayList<>();    //  we create bez arraylist
    Context context;
    FragmentManager fm;
    FirebaseFirestore db;
    CollectionReference liveTvRef;
    ArrayList<String> liveTVIds;

    public LiveTVAdapter(ArrayList<LiveTvModel> liveTvModels,
                         Context context,
                         FragmentManager fm,
                         ArrayList<String>liveTVIds) {
        this.liveTvModels = liveTvModels;
        this.context = context;
        this.fm = fm;
        this.liveTVIds = liveTVIds;
        db = FirebaseFirestore.getInstance();
        liveTvRef = db.collection(context.getString(R.string.live_tv_str));
    }



    @NonNull
    @Override
    public LiveTVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myView = inflater.inflate(R.layout.categoryitem,parent,false);
        return new LiveTVHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LiveTVHolder holder, final int position) {
        holder.sr.setText(position+1+"");
        holder.name.setText(liveTvModels.get(position).name);
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.option);
                popupMenu.getMenuInflater().inflate(R.menu.popmenu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.delete_menu) //   delete from firebase
                        {
                            liveTvRef.document(liveTVIds.get(position)).delete();   //  delete id

                        }
                        else
                        {
                            LiveTvPopUp popUp = new LiveTvPopUp();
                            popUp.editModel = liveTvModels.get(position);
                            popUp.id = liveTVIds.get(position);
                            popUp.show(fm,"Edit PopUp");

                        }
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return liveTvModels.size();
    }

    public class LiveTVHolder extends RecyclerView.ViewHolder{
        TextView sr,name;                                         //  like category item xml
        ImageView option;
        public LiveTVHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.edtname);
            option = itemView.findViewById(R.id.options);


        }
    }
}
