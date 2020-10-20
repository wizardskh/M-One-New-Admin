package com.sawkyawhtin.adminapp;


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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    private SettingModel settingModel;

    public MovieFragment() {
        // Required empty public constructor
    }

    static ArrayList<String>movieIds = new ArrayList<String>();

    FloatingActionButton add;
    static RecyclerView rcList;
    static ProgressBar loading;
    static FirebaseFirestore db;
    static CollectionReference ref,settingRef;
    static Context context;
    static FragmentManager fm;
    EditText edtsearch;
    RadioButton byMovie,byCategory;
    RadioGroup rbtgroup;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_movie, container, false);
        add = v.findViewById(R.id.add);
        loading = v.findViewById(R.id.loading);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie moviepopup = new Movie();
                moviepopup.show(getFragmentManager(),"Show Movie");
            }
        });
        rcList = v.findViewById(R.id.rcList);
        db = FirebaseFirestore.getInstance();
         ref = db.collection("movies");

         settingRef = db.collection("setting");
         byMovie = v.findViewById(R.id.byMovieName);
         byCategory = v.findViewById(R.id.byCategoryName);
         rbtgroup = v.findViewById(R.id.findbycategory);
         if(settingModel!=null) {
             loadSetting();
         }
        byMovie.setChecked(true);

         context = getContext();
         fm = getFragmentManager();

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
                    if(byCategory.isChecked())
                    {ref.orderBy("movieCategory")
                            .startAt(query)
                            .endAt(query+"\uf8ff")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    ArrayList<MovieModel> movieModels = new ArrayList<>();
                                    movieIds.clear();
                                    for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                    {
                                        movieIds.add(snapshot.getId());
                                        movieModels.add(snapshot.toObject(MovieModel.class));
                                    }
                                    MovieAdapter adapter = new MovieAdapter(movieModels,context,fm);
                                    rcList.setAdapter( adapter);
                                    rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                                    loading.setVisibility(View.GONE);
                                }
                            });

                    }
                    if(byMovie.isChecked())
                    {
                        ref.orderBy("movieName")
                                .startAt(query)
                                .endAt(query+"\uf8ff")
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                        ArrayList<MovieModel> movieModels = new ArrayList<>();
                                        movieIds.clear();
                                        for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                                        {
                                            movieIds.add(snapshot.getId());
                                            movieModels.add(snapshot.toObject(MovieModel.class));
                                        }
                                        MovieAdapter adapter = new MovieAdapter(movieModels,context,fm);
                                        rcList.setAdapter( adapter);
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
        return  v;
    }


    public static  void loadData()
    {
        ref.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        ArrayList<MovieModel> movieModels = new ArrayList<>();
                        movieIds.clear();
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            movieIds.add(snapshot.getId());
                            movieModels.add(snapshot.toObject(MovieModel.class));
                        }
                        MovieAdapter adapter = new MovieAdapter(movieModels,context,fm);
                        rcList.setAdapter( adapter);
                        rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                        loading.setVisibility(View.GONE);
                    }
                });
    }
    public static void deleteMovies(int postion)
    {
        ref.document(movieIds.get(postion)).delete();
        loadData();
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
                }
                else
                {
                    rbtgroup.setVisibility(View.GONE);
                }


            }
        });
    }

}
