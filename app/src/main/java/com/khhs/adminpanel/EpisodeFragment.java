package com.khhs.adminpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodeFragment extends Fragment {

    public EpisodeFragment() {
        // Required empty public constructor
    }

    static  ArrayList<String> epIds = new ArrayList<String>();
    static FirebaseFirestore db;
    static SeriesModel seriesModel = new SeriesModel();
    static RecyclerView rcList;
    TextView sr,name,category;
    int position;
    ImageView image;
    Button btnmore;
    String str;
    static Context context;
    static FragmentManager fragmentManager;
    static CollectionReference episodeRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View myView = inflater.inflate(R.layout.fragment_episode, container, false);
       sr = myView.findViewById(R.id.sr);
       name = myView.findViewById(R.id.name);
       category = myView.findViewById(R.id.category);
       image = myView.findViewById(R.id.image);
       rcList = myView.findViewById(R.id.episodelist);
       btnmore = myView.findViewById(R.id.btnmore);
       btnmore.setVisibility(View.GONE);
       sr.setText(str);
       name.setText(seriesModel.seriesName);
       category.setText(seriesModel.seriesCategory);
        Glide.with(getContext())
                .load(seriesModel.seriesImage)
                .into(image);
         db = FirebaseFirestore.getInstance();
         episodeRef = db.collection("episodes");
        context = getContext();
        fragmentManager = getFragmentManager();
         loadData();
         image.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 PopupMenu popupMenu = new PopupMenu(context,image);
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

                                             setFragment(new SeriesFragment());
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
                             Series.position = position;
                             seriespopup.edit_model = seriesModel;
                             seriespopup.id = SeriesFragment.seriesIds.get(position);
                             seriespopup.show(getFragmentManager(),"Edit Series");
                         }
                         return true;
                     }
                 });
                 return false;
             }
         });
       return  myView;
    }

    public static void loadData()
    {
        episodeRef.whereEqualTo("episodeSeries",seriesModel.seriesName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        ArrayList<EpisodeModel> episodeModels = new ArrayList<EpisodeModel>();
                        epIds.clear();
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            epIds.add(snapshot.getId());
                            episodeModels.add(snapshot.toObject(EpisodeModel.class));
                        }
                        EpisodeAdapter adapter = new EpisodeAdapter(episodeModels,context,fragmentManager);
                        rcList.setAdapter(adapter);
                        rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                    }
                });
    }
    public static void deleteEpisode(int position)
    {
        episodeRef.document(epIds.get(position)).delete();
        loadData();
    }

    public void setFragment(Fragment f)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainframe,f);
        ft.commit();
    }

}
