package com.khhs.adminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

public class Series  extends BottomSheetDialogFragment {
    ImageView close ;
    Spinner spCategory;
    EditText edtname,edtimage;
    Button btnsave,btncancel;
    SeriesModel edit_model;
    String id;
    static int position=0;
    FirebaseFirestore db;
    CollectionReference ref;
     CollectionReference seriesRef;
     CollectionReference episodeRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.series,container,false);
        close = v.findViewById(R.id.close);
        spCategory = v.findViewById(R.id.category);
        edtimage = v.findViewById(R.id.edtimage);
        edtname = v.findViewById(R.id.edtname);
        btnsave = v.findViewById(R.id.btnsave);
        btncancel = v.findViewById(R.id.btncancel);
        if(edit_model!=null)
        {
            edtimage.setText(edit_model.seriesImage);
            edtname.setText(edit_model.seriesName);
        }
        db = FirebaseFirestore.getInstance();
         ref = db.collection("categories");
        final ArrayList<String> categorynames = new ArrayList<>();
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               categorynames.clear();
                for(DocumentSnapshot ds : queryDocumentSnapshots)
                {
                    CategoryModel model = ds.toObject(CategoryModel.class);
                    categorynames.add(model.categoryName);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        categorynames);
                spCategory.setAdapter(adapter);
                if(edit_model!=null)
                {
                    for(int i=0;i<categorynames.size();i++){
                        if(categorynames.get(i).equals(edit_model.seriesCategory))
                        {
                            spCategory.setSelection(i);
                            break;
                        }
                    }
                }

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

         seriesRef = db.collection("series");
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtname.getText().toString().equals(""))
                {
                    Toasty.error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
                }
                else {
                    SeriesModel model = new SeriesModel();
                    SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhmmss");
                    model.createdAt = format.format(new Date());
                    model.seriesName = edtname.getText().toString().trim();
                    model.seriesImage = edtimage.getText().toString().trim();
                    model.seriesCategory = categorynames.get(spCategory.getSelectedItemPosition());
                    if(edit_model!=null)
                    {

                        seriesRef.document(id).set(model);
                        updateEpiSode(model.seriesName);
                        EpisodeFragment fragment = new EpisodeFragment();
                        fragment.seriesModel =model;
                        fragment.str = position+1+"";
                        position=0;
                        setFragment(fragment);

                        EpisodeFragment.loadData();
                        Toasty.success(getContext(),"Series Update Successfully",Toasty.LENGTH_LONG).show();

                    }
                    else
                    {
                        seriesRef.add(model);
                        Toasty.success(getContext(),"Series Save Successfully",Toasty.LENGTH_LONG).show();

                    }
                   edtname.setText("");
                    edtimage.setText("");
                    SeriesFragment.loadData();

                }
            }
        });
        return  v;
    }

    public void updateEpiSode(final String seriesName)
    {


        episodeRef = db.collection("episodes");
        episodeRef.whereEqualTo("episodeSeries",edit_model.seriesName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    EpisodeModel model = snapshot.toObject(EpisodeModel.class);
                    model.episodeSeries = seriesName;
                    episodeRef.document(snapshot.getId()).set(model);
                }

            }
        });
    }

    public void setFragment(Fragment f)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainframe,f);
        ft.commit();
    }
}
