package com.puzzle.industries.inumbr.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.puzzle.industries.inumbr.adapters.CombinationsAdapter;
import com.puzzle.industries.inumbr.dataModels.Ball;
import com.puzzle.industries.inumbr.dataModels.LuckyNumberGame;
import com.puzzle.industries.inumbr.databinding.ActivityResultsBinding;
import com.puzzle.industries.inumbr.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultsActivity extends FragmentActivity {

    private ActivityResultsBinding resultsBinding;
    private LuckyNumberGame mLuckyNumberGame;

    public static List<int[]> sResults;
    private final int BET_NUMS_RANGE = 200;
    private boolean placeingBets = false;
    int startIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultsBinding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(resultsBinding.getRoot());

        mLuckyNumberGame = getIntent().getExtras().getParcelable(Constants.KEY_LN_GAME);

        resultsBinding.tvCombinations.setText(String.format(Locale.UK, "Total Combinations (%d)", sResults.size()));

        List<List<Ball>> listOfBalls = new ArrayList<>();

        for (int[] combinations : sResults){
            List<Ball> balls = new ArrayList<>();
            for (int i : combinations) balls.add(new Ball(i, true));
            listOfBalls.add(balls);
        }

        CombinationsAdapter combinationsAdapter = new CombinationsAdapter(listOfBalls);
        resultsBinding.rvResults.setAdapter(combinationsAdapter);

        resultsBinding.btnPlaceBet.setOnClickListener(v -> placeBets());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (placeingBets) placeBets();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sResults = null;
    }

    private void placeBets(){
        placeingBets = startIndex < sResults.size();

        if (placeingBets){
            int toIndex = startIndex + BET_NUMS_RANGE;

            List<int[]> subResults = sResults.subList(startIndex, Math.min(toIndex, sResults.size()));

            startIndex += BET_NUMS_RANGE;

            if (!subResults.isEmpty()){
                PlaceBetActivity.sResults = subResults;
                Intent i = new Intent(ResultsActivity.this, PlaceBetActivity.class);
                i.putExtra(Constants.KEY_LN_GAME, mLuckyNumberGame);
                startActivity(i);
            }
        }

    }
}
