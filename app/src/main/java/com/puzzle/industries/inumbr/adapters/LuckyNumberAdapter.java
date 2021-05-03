package com.puzzle.industries.inumbr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.puzzle.industries.inumbr.dataModels.LuckyNumberGame;
import com.puzzle.industries.inumbr.databinding.ItemLuckyNumberGameBinding;
import com.puzzle.industries.inumbr.repositores.CredentialsRepository;
import com.puzzle.industries.inumbr.ui.GameViewActivity;
import com.puzzle.industries.inumbr.utils.Constants;

import java.util.List;

public class LuckyNumberAdapter extends RecyclerView.Adapter<LuckyNumberAdapter.ViewHolder> {

    private final CredentialsRepository CRED_REPO = CredentialsRepository.getInstance();
    private final List<LuckyNumberGame> luckyNumberGames;

    public LuckyNumberAdapter(List<LuckyNumberGame> luckyNumberGames) {
        this.luckyNumberGames = luckyNumberGames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemLuckyNumberGameBinding binding = ItemLuckyNumberGameBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LuckyNumberGame luckyNumberGame = luckyNumberGames.get(position);

        holder.binding.tvGameName.setText(luckyNumberGame.getName());
        holder.binding.ivLogo.setImageResource(luckyNumberGame.getImage());


        View.OnClickListener itemClickListener = v -> {
            final Context c = v.getContext();

            if (CRED_REPO.isCredentialsSet()){
                final Intent i = new Intent(c, GameViewActivity.class);
                i.putExtra(Constants.KEY_LN_GAME, luckyNumberGame);
                c.startActivity(i);
            }
            else{
                Toast.makeText(c, "Input login betway credentials first", Toast.LENGTH_LONG).show();
            }
        };

        holder.binding.getRoot().setOnClickListener(itemClickListener);
        holder.binding.btnBetNow.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return luckyNumberGames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ItemLuckyNumberGameBinding binding;

        public ViewHolder(@NonNull ItemLuckyNumberGameBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
