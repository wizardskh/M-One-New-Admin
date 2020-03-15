package com.khhs.adminpanel;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.khhs.adminpanel.MainActivity.settingModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {


    FloatingActionButton add;
    static RecyclerView rcList;
    static ProgressBar loading;
    static FirebaseFirestore db;
   static CollectionReference ref;
   static CollectionReference episodeRef,settingRef;
   EditText edtsearch;
    static ArrayList<String> seriesIds = new ArrayList<>();
    static Context context;
    static FragmentManager fragmentManager;
    RadioButton byMovie,byCategory;
    RadioGroup rbtgroup;
    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_series, container, false);
        add = v.findViewById(R.id.add);
        rcList = v.findViewById(R.id.rcList);
        loading = v.findViewById(R.id.loading);
        db =FirebaseFirestore.getInstance();
         ref = db.collection("series");

         episodeRef = db.collection("episodes");
        settingRef = db.collection("setting");
        byMovie = v.findViewById(R.id.byMovieName);
        byCategory = v.findViewById(R.id.byCategoryName);
        rbtgroup = v.findViewById(R.id.findbycategory);
        loadSetting();
        byMovie.setChecked(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Series seriespop = new Series();
                seriespop.show(getFragmentManager(),"Show Series");

            }
        });

        context = getContext();
        fragmentManager = getFragmentManager();

        edtsearch = v.findViewById(R.id.search);
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(edtsearch.getText().toString().equals(""))
                {
                    loadData();
                }
                else
                {
                    String query = edtsearch.getText().toString().trim();

                   if(byMovie.isChecked())
                   {
                       ref.orderBy("seriesName")
                               .startAt(query)
                               .endAt(query+"\uf8ff")
                               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                       seriesModels.clear();
                                       seriesIds.clear();
                                       for(DocumentSnapshot ds : queryDocumentSnapshots)
                                       {
                                           seriesModels.add(ds.toObject(SeriesModel.class));
                                           seriesIds.add(ds.getId());
                                       }
                                       SeriesAdapter adapter = new SeriesAdapter(seriesModels,context,fragmentManager);
                                       rcList.setAdapter(adapter);
                                       rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                                       loading.setVisibility(View.GONE);

                                   }
                               });
                   }
                   if(byCategory.isChecked())
                   {
                       ref.orderBy("seriesCategory")
                               .startAt(query)
                               .endAt(query+"\uf8ff")
                               .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                   @Override
                                   public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                       seriesModels.clear();
                                       seriesIds.clear();
                                       for(DocumentSnapshot ds : queryDocumentSnapshots)
                                       {
                                           seriesModels.add(ds.toObject(SeriesModel.class));
                                           seriesIds.add(ds.getId());
                                       }
                                       SeriesAdapter adapter = new SeriesAdapter(seriesModels,context,fragmentManager);
                                       rcList.setAdapter(adapter);
                                       rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                                       loading.setVisibility(View.GONE);

                                   }
                               });
                   }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loadData();
        return v;
    }

    public static void deleteSeries(int position)
    {
        ref.document(seriesIds.get(position)).delete();
        deleteEpisode(position);
        loadData();

    }

    public static void deleteEpisode(int position)
    {

        String name =seriesModels.get(position).seriesName;
        episodeRef.whereEqualTo("episodeSeries",name).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    episodeRef.document(snapshot.getId()).delete();
                }
            }
        });

    }

   static ArrayList<SeriesModel> seriesModels = new ArrayList<>();
    public static void loadData()
    {
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                seriesModels.clear();
                seriesIds.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots)
                {
                    seriesModels.add(ds.toObject(SeriesModel.class));
                    seriesIds.add(ds.getId());
                }
                SeriesAdapter adapter = new SeriesAdapter(seriesModels,context,fragmentManager);
                rcList.setAdapter(adapter);
                rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                loading.setVisibility(View.GONE);
            }
        });
    }

    public void loadSetting()
    {
        settingRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    settingModel  = s.toObject(SettingModel.class);
                }
                if(settingModel.findByCategory.equals("Yes"))
                {

                    rbtgroup.setVisibility(View.VISIBLE);
                    byMovie.setText("Series Name");
                }
                else
                {
                    rbtgroup.setVisibility(View.GONE);
                }


            }
        });
    }
}
