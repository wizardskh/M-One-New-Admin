package com.sawkyawhtin.adminapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }
    View myView;

    public static String appId="",bannerId="",interstitialId="",rewardId="",nativeId="";
    public static String s1="",s2="",s3="",s4="",s5="";
     FirebaseFirestore db;
     CollectionReference ref;
    CheckBox chbusecategory,chbfindbycategory,chbuseslideshow,chbuseadmob;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      myView = inflater.inflate(R.layout.fragment_setting, container, false);

      intiUI();
      loadSetting();



      chbuseadmob.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(chbuseadmob.isChecked())
              {
                  AdMobPopUp popUp = new AdMobPopUp();
                  popUp.show(getFragmentManager(),"Show AdMob Setting");
              }
          }
      });
      chbuseslideshow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             if(chbuseslideshow.isChecked())
             {
                 SlidePopUp popUp = new SlidePopUp();
                 popUp.show(getFragmentManager(),"Show Slide Setting");
             }
          }
      });




      Button btnsave = myView.findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SettingModel model = new SettingModel();
                if(chbuseadmob.isChecked())
                {
                    model.useAdMob = "Yes";
                    model.appId=appId;
                    model.bannerId = bannerId;
                    model.nativeId = nativeId;
                    model.interstitialId = interstitialId;
                    model.rewardId = rewardId;
                }
                else
                {
                    model.useAdMob = "No";
                    model.appId=appId;
                    model.bannerId = bannerId;
                    model.nativeId = nativeId;
                    model.interstitialId = interstitialId;
                    model.rewardId = rewardId;
                }
                if(chbuseslideshow.isChecked())
                {
                    model.useSlideShow = "Yes";
                    model.slide1 = s1;
                    model.slide2 = s2;
                    model.slide3 = s3;
                    model.slide4 = s4;
                    model.slide5 = s5;
                }
                else
                {
                    model.useSlideShow = "No";
                    model.slide1 = s1;
                    model.slide2 = s2;
                    model.slide3 = s3;
                    model.slide4 = s4;
                    model.slide5 = s5;
                }

                if(chbfindbycategory.isChecked())
                {
                    model.findByCategory = "Yes";
                }
                else
                {
                    model.findByCategory="No";
                }
                if(chbusecategory.isChecked())
                {
                    model.useCategoryReplace="Yes";

                }
                else
                {
                    model.useCategoryReplace = "No";
                }

                final ArrayList<SettingModel> arrayList = new ArrayList<>();
                final ArrayList<String> Ids = new ArrayList<>();
                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot s: queryDocumentSnapshots)
                        {
                            arrayList.add(s.toObject(SettingModel.class));
                            Ids.add(s.getId());
                        }
                        if(Ids.size()>0)
                        {
                            ref.document(Ids.get(0)).set(model);
                        }
                        else
                        {
                            ref.add(model);
                        }
                        Toasty.success(getContext(),"Setting Save Successfully",Toasty.LENGTH_LONG).show();;
                        loadSetting();
                    }
                });


            }
        });
     return  myView;
    }

    public void intiUI()
    {
        chbusecategory = myView.findViewById(R.id.usecategoryreplace);
        chbfindbycategory = myView.findViewById(R.id.usefindbycategory);
        chbuseslideshow = myView.findViewById(R.id.useslideshow);
        chbuseadmob  = myView.findViewById(R.id.useadmobsetting);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("setting");


    }

    public void loadSetting()
    {
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                SettingModel model  = new SettingModel();
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    model = s.toObject(SettingModel.class);
                }
                if(model!=null) {
                    try {
                        if (model.useCategoryReplace.equals("Yes")) {
                            chbusecategory.setChecked(true);
                        }
                        if (model.findByCategory.equals("Yes")) {
                            chbfindbycategory.setChecked(true);
                        }
                        if (model.useSlideShow.equals("Yes")) {
                            chbuseslideshow.setChecked(true);

                        }
                        s1 = model.slide1;
                        s2 = model.slide2;
                        s3 = model.slide3;
                        s4 = model.slide4;
                        s5 = model.slide5;
                        if (model.useAdMob.equals("Yes")) {
                            chbuseadmob.setChecked(true);

                        }
                        appId = model.appId;
                        bannerId = model.bannerId;
                        interstitialId = model.interstitialId;
                        rewardId = model.rewardId;
                        nativeId = model.nativeId;
                    }
                    catch (Exception ex)
                    {
                        Toasty.error(getContext(),"Database Error",Toasty.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
