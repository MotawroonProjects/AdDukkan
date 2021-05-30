package com.addukkan.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.DoctorRowBinding;
import com.addukkan.models.UserModel;
import com.addukkan.uis.activity_ask_doctor.AskDoctorActivity;
import com.addukkan.uis.activity_doctor_detials.DoctorDetialsActivity;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<UserModel.Data> list;
    private AskDoctorActivity activity;

    public DoctorsAdapter(Context context,List<UserModel.Data> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity= (AskDoctorActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.doctor_row, parent, false);
        return new DoctorHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        DoctorHolder doctorHolder = (DoctorHolder) holder;
        doctorHolder.binding.setModel(list.get(position));
        holder.itemView.setOnClickListener(v -> {
            UserModel.Data model = list.get(doctorHolder.getAdapterPosition());
            activity.setDoctorItemData(model);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DoctorHolder extends RecyclerView.ViewHolder {
        private DoctorRowBinding binding;

        public DoctorHolder(DoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
