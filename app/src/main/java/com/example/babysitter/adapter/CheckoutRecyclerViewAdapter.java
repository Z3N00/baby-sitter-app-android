package com.example.babysitter.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitter.databinding.ItemCheckoutBinding;
import com.example.babysitter.model.Service;

import java.util.List;

public class CheckoutRecyclerViewAdapter extends RecyclerView.Adapter<CheckoutRecyclerViewAdapter.ViewHolder> {

    private final List<Service> mValues;
    private final UpdateTotal updateTotal;

    public CheckoutRecyclerViewAdapter(List<Service> items, UpdateTotal updateTotal) {
        mValues = items;
        this.updateTotal = updateTotal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCheckoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Service service = mValues.get(position);
        holder.binding.serviceName.setText(service.name);
        holder.binding.price.setText(String.format("$ %04.2f", service.price));
        if (service.selected) {
            holder.binding.select.setText("Remove");
        } else {
            holder.binding.select.setText("Add");
        }
        holder.binding.select.setOnClickListener(v -> {
            service.selected = !service.selected;
            if (service.selected) {
                holder.binding.select.setText("Remove");
            } else {
                holder.binding.select.setText("Add");
            }
            updateTotal.doUpdate(service);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface UpdateTotal {
        void doUpdate(Service s);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCheckoutBinding binding;

        public ViewHolder(ItemCheckoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}