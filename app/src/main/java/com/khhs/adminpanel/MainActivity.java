package com.khhs.adminpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

   static SettingModel settingModel;
    BottomNavigationView btnView;
    RelativeLayout data_panel,splash_screen;
    ProgressBar progressBar;
    Handler mh = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            splash_screen.setVisibility(View.GONE);
            data_panel.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }
    };

    FirebaseFirestore db;
    CollectionReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnView = findViewById(R.id.bottomnav);
        getSupportActionBar().hide();
        splashScreen();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainframe,new MovieFragment());
        ft.commit();
        btnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.moviemenu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new MovieFragment());
                    ft.commit();
                }
                if(item.getItemId() == R.id.seriesmenu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new SeriesFragment());
                    ft.commit();
                }
                if(item.getItemId() == R.id.category)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new CategoryFragment());
                    ft.commit();
                }
                if(item.getItemId()==R.id.requestmenu)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.mainframe,new RequestFragment());
                    ft.commit();
                }
                return false;
            }
        });
        data_panel = findViewById(R.id.data_panel);
        splash_screen = findViewById(R.id.splash_srccen);
        mh.postDelayed(runnable,5000);



    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        menu.getItem(2).setVisible(false);
        db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("setting");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    settingModel  = s.toObject(SettingModel.class);
                }
                if(settingModel.useCategoryReplace.equals("Yes"))
                {
                    menu.getItem(2).setVisible(true);
                }
                else
                {
                    menu.getItem(2).setVisible(false);
                }
            }
        });
        return true;
    }

    public void splashScreen()
    {
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setting_menu)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainframe,new SettingFragment());
            ft.commit();
        }
        if(item.getItemId()==R.id.replace_menu)
        {

            ReplacePopUp popUp = new ReplacePopUp();
            popUp.show(getSupportFragmentManager(),"Show Replace");
        }
        if(item.getItemId()==R.id.help_menu)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainframe,new AboutFragment());
            ft.commit();
        }


        return true;
    }
}
