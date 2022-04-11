package com.sawkyawhtin.adminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class LiveFragment extends Fragment {



    public LiveFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_live, container, false);
        final RecyclerView recyclerView = myView.findViewById(R.id.list);
        FloatingActionButton add = myView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveTvPopUp popUp = new LiveTvPopUp();
                popUp.show(getFragmentManager(),"Add LiveTV"); //  data ခ်မယ္
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference liveTVRef = db.collection(getString(R.string.live_tv_str));
        liveTVRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<LiveTvModel> liveTvModels = new ArrayList<>();
                ArrayList<String> liveIds =  new ArrayList<>();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots)
                {
                    liveIds.add(snapshot.getId());
                    liveTvModels.add(snapshot.toObject(LiveTvModel.class));
                }
                LiveTVAdapter adapter = new LiveTVAdapter(liveTvModels,getContext(),getChildFragmentManager(),liveIds);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

            }
        });
        return myView;  //  after this go to main activity

    }
}

//  livetv item  For Admin App 1. Menu/bottonnav item 2. livetv fragment 3. livetv model 4. livetv adapter 5. livetvpopup
// 6. mainactivity
//  For Client App  1. move Livetv adapter and livetv model  2. livetvitem xml 3.home fragment xml 3. home fragment
//  4. playlivetvfragment and xml  5. res က new directoryxml