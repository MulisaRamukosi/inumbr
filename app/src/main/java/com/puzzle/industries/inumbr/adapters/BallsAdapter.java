package com.puzzle.industries.inumbr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.puzzle.industries.inumbr.R;
import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.databinding.ItemBallBinding;
import com.puzzle.industries.inumbr.interfaces.BallClickListener;

import java.util.List;

public class BallsAdapter extends RecyclerView.Adapter<BallsAdapter.ViewHolder>{

    private final List<Ball> numbers;
    private final BallClickListener ballClickListener;

    public BallsAdapter(@NonNull List<Ball> numbers, BallClickListener ballClickListener) {
        this.numbers = numbers;
        this.ballClickListener = ballClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBallBinding ballBinding = ItemBallBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(ballBinding, ballClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.BINDING.getRoot().getContext();

        Ball ball = numbers.get(position);

        holder.BINDING.tvNum.setText(String.valueOf(ball.getNum()));
        holder.BINDING.tvNum.setTextColor(ContextCompat.getColor(ctx, ball.isSelected() ? R.color.white : R.color.color_accent));
        holder.BINDING.tvNum.setBackground(ContextCompat.getDrawable(ctx, ball.isSelected() ? R.drawable.bg_circle_selected : R.drawable.bg_circle));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ItemBallBinding BINDING;
        private final BallClickListener ballClickListener;

        public ViewHolder(@NonNull ItemBallBinding binding, BallClickListener ballClickListener) {
            super(binding.getRoot());
            BINDING = binding;
            BINDING.getRoot().setOnClickListener(this);
            this.ballClickListener = ballClickListener;
        }


        @Override
        public void onClick(View v) {
            if(ballClickListener != null){
                ballClickListener.ballOnClick(getAdapterPosition());
            }
        }
    }
}
