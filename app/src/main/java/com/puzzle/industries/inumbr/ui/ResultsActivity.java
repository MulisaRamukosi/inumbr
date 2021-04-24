package com.puzzle.industries.inumbr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.puzzle.industries.inumbr.adapters.CombinationsAdapter;
import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.databinding.ActivityResultsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultsActivity extends FragmentActivity {

    private ActivityResultsBinding resultsBinding;

    public static List<int[]> sResults;
    private final int BET_NUMS_RANGE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultsBinding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(resultsBinding.getRoot());

        resultsBinding.tvCombinations.setText(String.format(Locale.UK, "Total Combinations (%d)", sResults.size()));

        List<List<Ball>> listOfBalls = new ArrayList<>();

        for (int[] combinations : sResults){
            List<Ball> balls = new ArrayList<>();
            for (int i : combinations) balls.add(new Ball(i, true));
            listOfBalls.add(balls);
        }

        CombinationsAdapter combinationsAdapter = new CombinationsAdapter(listOfBalls);
        resultsBinding.rvResults.setAdapter(combinationsAdapter);

        resultsBinding.btnPlaceBet.setOnClickListener(this::placeBets);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sResults = null;
    }

    private void placeBets(View v){
        for (int i = 0; i < sResults.size(); i  += BET_NUMS_RANGE){
            int toIndex = i + BET_NUMS_RANGE;

            List<int[]> subResults = sResults.subList(i, Math.min(toIndex, sResults.size()));

            if (!subResults.isEmpty()){
                PlaceBetsActivity.sResults = subResults;
                startActivity(new Intent(v.getContext(), PlaceBetsActivity.class));
            }

        }
    }
}
