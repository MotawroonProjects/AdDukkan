package com.addukkan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.MainCategorySubCategoryProductRowBinding;
import com.addukkan.databinding.SideMenuCategoryRowBinding;
import com.addukkan.models.MainCategoryDataModel;

import java.util.List;

import io.paperdb.Paper;

public class SideMenuCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String lang;
    private List<MainCategoryDataModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private int i=-1;
    private int type=0;

    //private Fragment_Main fragment_main;
    public SideMenuCategoryAdapter(List<MainCategoryDataModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
        inflater = LayoutInflater.from(context);
        //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SideMenuCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.side_menu_category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        SideMenuSubCategoryAdapter subCategoryAdapter = new SideMenuSubCategoryAdapter(list.get(position).getSub_departments(), context,list.get(position));
        myHolder.binding.recViewSubCategory.setLayoutManager(new GridLayoutManager(context, 1));
        myHolder.binding.recViewSubCategory.setAdapter(subCategoryAdapter);

        myHolder.itemView.setOnClickListener(view -> {
           i=holder.getLayoutPosition();
           type=1;
           notifyDataSetChanged();
        });
        if(i==position){
            if(((MyHolder) holder).binding.elexpend.isExpanded()){
                type=0;
                myHolder.binding.elexpend.setExpanded(false);
                myHolder.binding.arrow.setRotation(-90);
            }else {
                type=1;
                myHolder.binding.elexpend.setExpanded(true);
                myHolder.binding.arrow.setRotation(90);
            }
            myHolder.binding.setType(type+"");
        }
        else {
            type=0;
            myHolder.binding.elexpend.setExpanded(false);
            myHolder.binding.arrow.setRotation(-90);
            myHolder.binding.setType(type+"");

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public SideMenuCategoryRowBinding binding;

        public MyHolder(@NonNull SideMenuCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
