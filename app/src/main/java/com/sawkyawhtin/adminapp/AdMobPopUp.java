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

public class AdMobPopUp extends BottomSheetDialogFragment {
    ImageView close;
    EditText edtappId,edtbannerId,edtInterstitalId,edtRewardId,edtNativeId;
    Button btnsave,btncancel;
    View myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         myView = inflater.inflate(R.layout.admob,container,false);
        initUI();
        edtappId.setText(SettingFragment.appId);
        edtbannerId.setText(SettingFragment.bannerId);
        edtInterstitalId.setText(SettingFragment.interstitialId);
        edtRewardId.setText(SettingFragment.rewardId);
        edtNativeId.setText(SettingFragment.nativeId);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment.appId = edtappId.getText().toString().trim();
                SettingFragment.bannerId = edtbannerId.getText().toString().trim();
                SettingFragment.interstitialId = edtInterstitalId.getText().toString().trim();
                SettingFragment.rewardId = edtRewardId.getText().toString().trim();
                SettingFragment.nativeId = edtNativeId.getText().toString().trim();
                dismiss();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtappId.setText("");
                edtbannerId.setText("");
                edtNativeId.setText("");
                edtInterstitalId.setText("");
                edtRewardId.setText("");
                dismiss();
            }
        });

        return  myView;
    }

    private void initUI() {
        close = myView.findViewById(R.id.close);
        btnsave = myView.findViewById(R.id.btnsave);
        btncancel = myView.findViewById(R.id.btncancel);
        edtappId = myView.findViewById(R.id.edtappId);
        edtbannerId = myView.findViewById(R.id.edtbannerId);
        edtNativeId = myView.findViewById(R.id.edtnative);
        edtInterstitalId =  myView.findViewById(R.id.edtinterstialId);
        edtRewardId = myView.findViewById(R.id.edtreward);
    }
}
