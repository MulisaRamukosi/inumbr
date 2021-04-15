package com.puzzle.industries.inumbr.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.databinding.ItemCombinationsBinding;

import java.util.List;

public class CombinationsAdapter extends RecyclerView.Adapter<CombinationsAdapter.ViewHolder> {

    private final List<List<Ball>> combinationsList;

    public CombinationsAdapter(List<List<Ball>> combinationsList) {
        this.combinationsList = combinationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCombinationsBinding itemBinding = ItemCombinationsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Ball> balls = combinationsList.get(position);
        BallsAdapter adapter = new BallsAdapter(balls, null);
        holder.binding.rvResultItem.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return combinationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemCombinationsBinding binding;

        public ViewHolder(@NonNull ItemCombinationsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
