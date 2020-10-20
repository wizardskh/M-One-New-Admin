package com.sawkyawhtin.adminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class ReplacePopUp extends BottomSheetDialogFragment {
    View myView;
    RadioButton movieOnly,seriesOnly,both;
    Button btnreplace;
    Spinner findcategory,replaceCategory;
    FirebaseFirestore db;
    CollectionReference categoryRef;
    CollectionReference movieRef,seriesRef;
    ImageView close;
    ArrayList<String>cateogrynames = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView  = inflater.inflate(R.layout.categoryreplace,container,false);
        initUI();
        categoryRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                cateogrynames.clear();
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    cateogrynames.add(s.toObject(CategoryModel.class).categoryName);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,cateogrynames);
                findcategory.setAdapter(adapter);
                replaceCategory.setAdapter(adapter);

            }
        });
        btnreplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strfind = cateogrynames.get(findcategory.getSelectedItemPosition());
                final String strreplace = cateogrynames.get(replaceCategory.getSelectedItemPosition());
                if(!strfind.equals(strreplace)){

                    if(movieOnly.isChecked())
                    {
                       updateMovie(strfind,strreplace);
                    }
                    if(seriesOnly.isChecked())
                    {
                       updateSeries(strfind,strreplace);
                    }
                    if(both.isChecked())
                    {
                        updateMovie(strfind,strreplace);
                        updateSeries(strfind,strreplace);
                    }
                    Toasty.success(getContext(),"Update Complete",Toasty.LENGTH_LONG).show();;
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return myView;
    }
    public void initUI()
    {
        movieOnly = myView.findViewById(R.id.chbmovieonly);
        seriesOnly = myView.findViewById(R.id.chbseriesonly);
        both = myView.findViewById(R.id.chbboth);
        btnreplace = myView.findViewById(R.id.btnreplace);
        findcategory = myView.findViewById(R.id.findcategory);
        replaceCategory = myView.findViewById(R.id.replacecategory);
        db = FirebaseFirestore.getInstance();
        categoryRef = db.collection("categories");
        seriesRef = db.collection("series");
        movieRef = db.collection("movies");
        close = myView.findViewById(R.id.close);
    }

    public void updateSeries(String strfind, final String strreplace)
    {
        seriesRef.whereEqualTo("seriesCategory",strfind).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    SeriesModel seriesModel = s.toObject(SeriesModel.class);
                    seriesModel.seriesCategory = strreplace;
                    seriesRef.document(s.getId()).set(seriesModel);
                }
            }
        });
    }

    public void updateMovie(String strfind, final String strreplace)
    {
        movieRef.whereEqualTo("movieCategory",strfind).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    MovieModel movieModel = s.toObject(MovieModel.class);
                    movieModel.movieCategory = strreplace;
                    movieRef.document(s.getId()).set(movieModel);
                }
            }
        });
    }


}
