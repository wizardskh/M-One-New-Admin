package com.sawkyawhtin.adminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class Episode extends BottomSheetDialogFragment {



    ImageView close;
    CheckBox isEp;
    EditText edtname,edtvideo;
    Spinner spSeries;
    //   LinearLayout seriespanel;
    Button btnsave,btncancel;
    MovieModel add_episodeModel;
    ArrayList<SeriesModel> seriesModels = new ArrayList<>();  //  test ep  i reuse from  episode adapter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.episode,container,false);
        spSeries = v.findViewById(R.id.series);
        isEp = v.findViewById(R.id.isep);
        edtname = v.findViewById(R.id.edtname);
        edtvideo = v.findViewById(R.id.edtvideo);
        //     seriespanel = v.findViewById(R.id.seriespanel);
        btnsave = v.findViewById(R.id.btnsave);
        btncancel = v.findViewById(R.id.btncancel);
        close = v.findViewById(R.id.close);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
//
//        isEp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                    isEp.setEnabled(false);
//            }
//
//            });

//       isEp.setOnClickListener(new View.OnClickListener() {   //  i passed this test ep add  episode at episode fragment
//           @Override
//           public void onClick(View v) {
//  //       seriespanel.setVisibility(View.VISIBLE);
//               if(add_episodeModel!=null )
//               {
//                   edtname.setText(add_episodeModel.movieVideo);
//                   edtvideo.setText(add_episodeModel.movieVideo);
//  //                 seriespanel.setVisibility(View.GONE);
//
//               }
//               else
//               {
//
//                //   seriespanel.setVisibility(View.VISIBLE);
//               }
//
//           }
//       });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference seriesRef = db.collection("series");

        final ArrayList<String> seriesnames = new ArrayList<>();   // i change seriesnames to episodeSeries
        seriesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                seriesnames.clear();
                for (DocumentSnapshot s: queryDocumentSnapshots)
                {
                    seriesnames.add(s.toObject(SeriesModel.class).seriesName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,seriesnames);
                spSeries.setAdapter(adapter);


                if(add_episodeModel!=null )
                {
                    for(int i=0;i<seriesnames.size();i++)
                    {
                        if(seriesnames.get(i).equals(add_episodeModel.movieCategory))   //   i change movieCategory
                        {
                            spSeries.setSelection(i);

                            break;
                        }

                    }
                } else {


                }


            }
        });
        final CollectionReference episodeRef = db.collection("episodes");
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                if (edtname.getText().toString().equals("") && edtvideo.getText().toString().equals(""))
                {
                    Toasty.error(getContext(),"Please Fill Data",Toasty.LENGTH_LONG).show();
                }
                else
                {
//                   if (isEp.isChecked())
//                   {
                    EpisodeModel model = new EpisodeModel();
                    SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhhMMss");
                    model.createdAt = format.format(new Date());
                    model.episodeName = edtname.getText().toString().trim();
                    model.episodeSeries = seriesnames.get(spSeries.getSelectedItemPosition());
                    model.episodeVideo = edtvideo.getText().toString().trim();
                    episodeRef.add(model);
                    Toasty.success(getContext(),"Episode Save Successfully!", Toasty.LENGTH_LONG).show();


                }
//                   if(isEp.isChecked())     //   renew test ep
//                   {
//                       isEp.setEnabled(false);
//                   }                                 //   renew test ep

                edtname.setText("");
                edtvideo.setText("");
//                EpisodeFragment.loadData();

            }
            //              }

        });
        return v;

    }

}
