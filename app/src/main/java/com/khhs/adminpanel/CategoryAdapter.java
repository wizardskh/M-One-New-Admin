package com.khhs.adminpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    ArrayList<CategoryModel> categoryModels =new ArrayList<>();
    Context context;
    FragmentManager fm;

    public CategoryAdapter(ArrayList<CategoryModel> categoryModels, Context context, FragmentManager fm) {
        this.categoryModels = categoryModels;
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem,parent,false);
        CategoryHolder myholder = new CategoryHolder(myView);
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, final int position) {

        holder.sr.setText(position+1+"");
        holder.name.setText(categoryModels.get(position).categoryName);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.options);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.popmenu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete_menu)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmation!")
                                    .setMessage("Are you Sure To Delete?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CategoryFragment.deleteCategory(position);
                                            Toasty.success(context,"Category Deleted Successfully!",Toasty.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();
                        }
                        if(item.getItemId() == R.id.edit_menu)
                        {
                            Category categorypopup = new Category();
                            categorypopup.editmodel = categoryModels.get(position);
                            categorypopup.id = CategoryFragment.categoryIds.get(position);
                            categorypopup.show(fm,"Edit Category");
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{

        TextView sr,name;
        ImageView options;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            sr = itemView.findViewById(R.id.sr);
            name = itemView.findViewById(R.id.edtname);
            options = itemView.findViewById(R.id.options);

        }
    }
}
