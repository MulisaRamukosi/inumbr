package com.puzzle.industries.inumbr.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puzzle.industries.inumbr.databinding.ItemCombinationsBinding;

import java.util.List;

public class CombinationsAdapter extends RecyclerView.Adapter<CombinationsAdapter.ViewHolder> {

    private final List<int[]> combinationsList;
    private final double ballWidth;

    public CombinationsAdapter(List<int[]> combinationsList, double ballWidth) {
        this.combinationsList = combinationsList;
        this.ballWidth = ballWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCombinationsBinding itemBinding = ItemCombinationsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int[] combinations = combinationsList.get(position);
        TableViewController controller = new TableViewController(holder.binding.tlResultItem, ballWidth);
        controller.addAllNumbers(combinations);
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
