package com.khhs.adminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class Movie extends BottomSheetDialogFragment {
    MovieModel edit_movieModel;
    EpisodeModel edit_epModel;
    String id;
    ImageView close;
    CheckBox isEp;
    EditText edtname,edtimage,edtvideo;
    Spinner spCategory,spSeries;
    LinearLayout seriespanel,imagepanel,categorypanel;
    Button btnsave,btncancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.movie,container,false);
        close = v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        spCategory = v.findViewById(R.id.category);
        spSeries = v.findViewById(R.id.series);
        isEp = v.findViewById(R.id.isep);
        edtname = v.findViewById(R.id.edtname);
        edtimage = v.findViewById(R.id.edtimage);
        edtvideo = v.findViewById(R.id.edtvideo);
        seriespanel = v.findViewById(R.id.seriespanel);
        imagepanel = v.findViewById(R.id.imagepanel);
        btnsave = v.findViewById(R.id.btnsave);
        btncancel = v.findViewById(R.id.btncancel);
        categorypanel = v.findViewById(R.id.categorypanel);
        if(edit_movieModel!=null && edit_epModel==null)
        {
            isEp.setChecked(false);
            edtname.setText(edit_movieModel.movieName);
            edtvideo.setText(edit_movieModel.movieVideo);
            edtimage.setText(edit_movieModel.movieImage);
            seriespanel.setVisibility(View.GONE);
            imagepanel.setVisibility(View.VISIBLE);
            categorypanel.setVisibility(View.VISIBLE);
            isEp.setEnabled(false);

        }
        if(edit_epModel!=null)
        {
            isEp.setChecked(true);
            edtname.setText(edit_epModel.episodeName);
            edtvideo.setText(edit_epModel.episodeVideo);
            seriespanel.setVisibility(View.VISIBLE);
            imagepanel.setVisibility(View.GONE);
            categorypanel.setVisibility(View.GONE);
            isEp.setEnabled(false);
        }
        isEp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEp.isChecked())
                {
                    seriespanel.setVisibility(View.GONE);
                    imagepanel.setVisibility(View.VISIBLE);
                    categorypanel.setVisibility(View.VISIBLE);
                }
                else
                {
                    seriespanel.setVisibility(View.VISIBLE);
                    imagepanel.setVisibility(View.GONE);
                    categorypanel.setVisibility(View.GONE);
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference categoryRef = db.collection("categories");
        CollectionReference seriesRef = db.collection("series");
        final ArrayList<String> categorynames = new ArrayList<String>();
        categoryRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                categorynames.clear();;
                for(DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    categorynames.add(snapshot.toObject(CategoryModel.class).categoryName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,categorynames);
                spCategory.setAdapter(adapter);
                if(edit_movieModel!=null && edit_epModel==null)
                {
                    for(int i=0;i<categorynames.size();i++)
                    {
                        if(categorynames.get(i).equals(edit_movieModel.movieCategory))
                        {
                            spCategory.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });

        final ArrayList<String> seriesnames = new ArrayList<>();
        seriesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                seriesnames.clear();;
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    seriesnames.add(s.toObject(SeriesModel.class).seriesName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,seriesnames);
                spSeries.setAdapter(adapter);
                if(edit_epModel!=null)
                {
                    for(int i=0;i<seriesnames.size();i++)
                    {
                        if(seriesnames.get(i).equals(edit_epModel.episodeSeries))
                        {
                            spSeries.setSelection(i);
                            break;
                        }
                    }
                }
            }
        });
        final CollectionReference movieRef = db.collection("movies");
        final CollectionReference episodeRef = db.collection("episodes");
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(edtname.getText().toString().equals("") && edtvideo.getText().toString().equals("")&& edtimage.getText().toString().equals(""))
               {
                   Toasty.error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
               }
               else
               {
                   if(isEp.isChecked())
                   {
                       EpisodeModel model = new EpisodeModel();
                       SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhMMss");
                       model.createdAt = format.format(new Date());
                       model.episodeName = edtname.getText().toString().trim();
                       model.episodeSeries = seriesnames.get(spSeries.getSelectedItemPosition());
                       model.episodeVideo = edtvideo.getText().toString().trim();

                       if(edit_epModel!=null){
                           episodeRef.document(id).set(model);
                           Toasty.success(getContext(),"Episode Updated Successuflly",Toasty.LENGTH_LONG).show();
                           edit_epModel=null;
                           id = null;
                       }
                       else {
                           episodeRef.add(model);
                           Toasty.success(getContext(), "Episode Save Successfully!", Toasty.LENGTH_LONG).show();

                       }
                       EpisodeFragment.loadData();
                       }
                   else
                   {

                       MovieModel model = new MovieModel();
                       SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhMMss");
                       model.createdAt = format.format(new Date());
                       model.movieName = edtname.getText().toString().trim();
                       model.movieCategory = categorynames.get(spCategory.getSelectedItemPosition());
                       model.movieVideo = edtvideo.getText().toString().trim();
                       model.movieImage = edtimage.getText().toString().trim();
                      if(edit_movieModel!=null)
                      {
                          movieRef.document(id).set(model);
                          Toasty.success(getContext(),"Movie Update Successfully",Toasty.LENGTH_LONG).show();
                        edit_movieModel = null;
                        id = "";
                      }
                      else {
                          movieRef.add(model);
                          Toasty.success(getContext(), "Movies Save Successfully", Toasty.LENGTH_LONG).show();
                      }
                       MovieFragment.loadData();
                   }
                   edtimage.setText("");
                   edtvideo.setText("");
                   edtname.setText("");

               }
            }
        });
        return  v;
    }
}
