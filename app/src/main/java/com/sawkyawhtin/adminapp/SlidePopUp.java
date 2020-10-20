package com.sawkyawhtin.adminapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SlidePopUp extends BottomSheetDialogFragment {
    View myView;
    Button btnsave,btncancel;
    EditText s1,s2,s3,s4,s5;
    ImageView close;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.slideshow,container,false);
        initUI();
        s1.setText(SettingFragment.s1);
        s2.setText(SettingFragment.s2);
        s3.setText(SettingFragment.s3);
        s4.setText(SettingFragment.s4);
        s5.setText(SettingFragment.s5);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment.s1 = s1.getText().toString().trim();
                SettingFragment.s2 = s2.getText().toString().trim();
                SettingFragment.s3 = s3.getText().toString().trim();
                SettingFragment.s4 = s4.getText().toString().trim();
                SettingFragment.s5 = s5.getText().toString().trim();
                dismiss();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setText("");
                s2.setText("");
                s3.setText("");
                s4.setText("");
                s5.setText("");
                dismiss();
            }
        });
        return  myView;
    }
    public void initUI()
    {
        btnsave = myView.findViewById(R.id.btnsave);
        btncancel = myView.findViewById(R.id.btncancel);
        close = myView.findViewById(R.id.close);
        s1 = myView.findViewById(R.id.edts1);
        s2 = myView.findViewById(R.id.edts2);
        s3= myView.findViewById(R.id.edts3);
        s4 = myView.findViewById(R.id.edts4);
        s5 = myView.findViewById(R.id.edts5);
    }

}
