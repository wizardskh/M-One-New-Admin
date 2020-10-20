package com.sawkyawhtin.adminapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }


    RecyclerView request ;
    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_request, container, false);
        request = myView.findViewById(R.id.requests);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("requests");
        ref.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        ArrayList<RequestModel> requestModels = new ArrayList<>();
                        for (DocumentSnapshot ds : queryDocumentSnapshots)
                        {
                            requestModels.add(ds.toObject(RequestModel.class));

                        }
                        RequestAdapter adapter = new RequestAdapter(requestModels,getContext());
                        request.setAdapter(adapter);
                        request.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

                    }
                });
        return  myView;
    }
}
