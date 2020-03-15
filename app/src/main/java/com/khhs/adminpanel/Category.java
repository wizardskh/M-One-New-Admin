package com.khhs.adminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;

public class Category extends BottomSheetDialogFragment {
    ImageView close;
    Button btnsave,btncanel;
    EditText edtname;
    CategoryModel editmodel;
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category,container,false);
        close = v.findViewById(R.id.close);
        btnsave = v.findViewById(R.id.btnsave);
        btncanel = v.findViewById(R.id.btncancel);
        edtname = v.findViewById(R.id.edtname);

        // for edit
        if(editmodel!=null)
        {
            edtname.setText(editmodel.categoryName);
        }
        //Close BottomSheet
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // Save Action

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference ref = db.collection("categories");
       btnsave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(edtname.getText().toString().trim().equals("")) {
               Toasty.error(getContext(),"Please Fill Data!",Toasty.LENGTH_LONG).show();
               }
               else {
                   CategoryModel model = new CategoryModel();
                   model.categoryName = edtname.getText().toString().trim();

                   if(editmodel!=null)
                   {
                       ref.document(id).set(model);
                       Toasty.success(getContext(), "Category Update Successfully!", Toasty.LENGTH_LONG).show();
                        editmodel=null;
                        id = null;
                   }
                   else {
                       ref.add(model);
                       Toasty.success(getContext(), "Category Save Successfully!", Toasty.LENGTH_LONG).show();
                   }
                   edtname.setText("");
                   CategoryFragment.loadData();

               }
               }
       });
        return  v;
    }
}
