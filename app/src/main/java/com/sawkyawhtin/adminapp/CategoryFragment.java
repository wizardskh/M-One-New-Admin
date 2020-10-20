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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class CategoryFragment extends Fragment {

    static ArrayList<String> categoryIds = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    FloatingActionButton add;
    static RecyclerView rcList;
   static ProgressBar loading;
   static FirebaseFirestore db;
   static CollectionReference ref;
   static Context context;
   static FragmentManager fm;
   EditText edtsearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_category, container, false);
        add = v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category categorypop = new Category();
                categorypop.show(getFragmentManager(),"Show Category");
            }
        });
        rcList = v.findViewById(R.id.rcList);
        loading = v.findViewById(R.id.loading);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("categories");
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
                   ref.orderBy("categoryName")
                           .startAt(query)
                           .endAt(query+"\uf8ff")
                           .addSnapshotListener(new EventListener<QuerySnapshot>() {
                               @Override
                               public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                   ArrayList<CategoryModel> categoryModels = new ArrayList<>();
                                   categoryIds.clear();
                                   for(DocumentSnapshot ds : queryDocumentSnapshots)
                                   {

                                       CategoryModel model = ds.toObject(CategoryModel.class);
                                       categoryModels.add(model);
                                       categoryIds.add(ds.getId());

                                   }

                                   CategoryAdapter adapter = new CategoryAdapter(categoryModels,context,fm);
                                   rcList.setAdapter(adapter);
                                   rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                                   loading.setVisibility(View.GONE);
                               }
                           });
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loadData();

        return  v;
    }

    public static void loadData()
    {
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<CategoryModel> categoryModels = new ArrayList<>();
                categoryIds.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots)
                {

                    CategoryModel model = ds.toObject(CategoryModel.class);
                    categoryModels.add(model);
                    categoryIds.add(ds.getId());

                }

                CategoryAdapter adapter = new CategoryAdapter(categoryModels,context,fm);
                rcList.setAdapter(adapter);
                rcList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

                loading.setVisibility(View.GONE);
            }
        });
    }
    public static void deleteCategory(int postion)
    {
        ref.document(categoryIds.get(postion)).delete();
        loadData();
    }

}
