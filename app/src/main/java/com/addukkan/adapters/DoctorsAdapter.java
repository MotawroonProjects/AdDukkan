package com.addukkan.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.DoctorRowBinding;

public class DoctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;

    public DoctorsAdapter( Context context) {
        inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            DoctorRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.doctor_row, parent, false);
            return new DoctorHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class DoctorHolder extends RecyclerView.ViewHolder {
        private DoctorRowBinding binding;

        public DoctorHolder(DoctorRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
