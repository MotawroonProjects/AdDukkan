package com.addukkan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.AttrRowBinding;
import com.addukkan.models.SingleProductModel;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;

import java.util.ArrayList;
import java.util.List;


public class ProductAttrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<SingleProductModel.Sub> list;
    private int i=0;
private int level;
private int x=-1;
    public ProductAttrAdapter(Context context, List<SingleProductModel.Sub> list, int size) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        level=size;
    }

    public ProductAttrAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list=new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AttrRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.attr_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
   myHolder.itemView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           i=holder.getLayoutPosition();
           x=0;
           notifyDataSetChanged();
       }
   });
   if(i==position){
       myHolder.binding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
      myHolder.binding.tvtitle.setTextColor(context.getResources().getColor(R.color.white));
       if(list.get(position).getSub()!=null&&list.get(position).getSub().size()>0){

       if(context instanceof ProductDetialsActivity){
           ProductDetialsActivity productDetialsActivity=(ProductDetialsActivity)context;
           productDetialsActivity.setAttr(list.get(position),level,list.get(position).getAttribute_trans_fk().getTitle());

       }


       }
       else {

           if(context instanceof ProductDetialsActivity){

               ProductDetialsActivity productDetialsActivity=(ProductDetialsActivity)context;
           productDetialsActivity.setAttr(level);
       }}
   }
   else {
       myHolder.binding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
       myHolder.binding.tvtitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
       // myHolder.binding.recView.getAdapter().c
      // myHolder.binding.recView.setAdapter(null);

       //  myHolder.binding.recView.getAdapter().notifyItemRangeRemoved(0,myHolder.binding.recView.getAdapter().getItemCount());
   }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private AttrRowBinding binding;

        public MyHolder(AttrRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
