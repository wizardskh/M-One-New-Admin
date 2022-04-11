package com.sawkyawhtin.adminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class LiveTvPopUp extends BottomSheetDialogFragment {

    LiveTvModel editModel; //  for adapter
    String id;             //   for adapter
    FirebaseFirestore db;
    CollectionReference liveTvRef;
    EditText edtname,edtimage,edtlink;
    Button btnsave,btncancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.livetvpopup,container,false);
        edtimage = myView.findViewById(R.id.edtimage);
        edtname = myView.findViewById(R.id.edtname);
        edtlink = myView.findViewById(R.id.edtlink);
        btnsave = myView.findViewById(R.id.btnsave);
        db = FirebaseFirestore.getInstance();
        liveTvRef = db.collection(getString(R.string.live_tv_str));
        if(editModel != null)
        {
            edtname.setText(editModel.name);
            edtimage.setText(editModel.image);
            edtlink.setText(editModel.link);
        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveTvModel model = new LiveTvModel();
                model.name = edtname.getText().toString().trim();
                model.image = edtimage.getText().toString().trim();
                model.link = edtlink.getText().toString().trim();
                if (editModel == null) {
                    liveTvRef.add(model);
                    Toasty.success(getContext(), "Save Success", Toasty.LENGTH_LONG).show();
                }
                else
                {
                    liveTvRef.document(id).set(model);
                    Toasty.info(getContext(),"Update Success",Toasty.LENGTH_LONG).show();
                    editModel = null;
                    id="";
                }
                edtname.setText("");
                edtimage.setText("");
                edtlink.setText("");
            }
        });
        btncancel = myView.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtname.setText("");
                edtimage.setText("");
                edtlink.setText("");

            }
        });
        return myView;
    }
}
