package com.addukkan.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.CountryRowBinding;
import com.addukkan.databinding.DoctorRowBinding;
import com.addukkan.models.CountryModel;
import com.addukkan.uis.activity_countries.CountryActivity;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CountryModel> list;
    private LayoutInflater inflater;
    private Context context;
    private int i = -1;
   // private int old_pos = 0;


    public CountryAdapter(Context context,List<CountryModel> list) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            CountryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.country_row, parent, false);
            return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        CountryModel countryModel = list.get(position);
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(countryModel);

        holder.itemView.setOnClickListener(view -> {
//            CountryModel model1 = list.get(old_pos);
//            model1.setSelected(false);
//            list.set(old_pos,model1);
//            notifyItemChanged(old_pos);
//
//            i = holder.getAdapterPosition();
//            CountryModel model2 = list.get(i);
//            model2.setSelected(true);
//            list.set(i,model2);
//            notifyItemChanged(i);
//            old_pos =i;
            i=position;
            notifyDataSetChanged();

            //fragment_categories.setItemData(model2);

        });
if(i==position){
    ((MyHolder) holder).binding.rb.setChecked(true);
    if(context instanceof CountryActivity){
        CountryActivity countryActivity=(CountryActivity)context;
        countryActivity.setcountry(list.get(position));
    }
}
else {
    ((MyHolder) holder).binding.rb.setChecked(false);
}

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CountryRowBinding binding;

        public MyHolder(CountryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

}
