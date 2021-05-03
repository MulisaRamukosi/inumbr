package com.puzzle.industries.inumbr.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.puzzle.industries.inumbr.dataModels.LuckyNumberGame;
import com.puzzle.industries.inumbr.databinding.ActivityPlaceBetsBinding;
import com.puzzle.industries.inumbr.utils.Constants;

import java.io.IOException;
import java.util.List;

public class PlaceBetActivity extends BetPlacerActivity {

    public static List<int[]> sResults;

    private ActivityPlaceBetsBinding mBinding;
    private LuckyNumberGame mLuckyNumberGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPlaceBetsBinding.inflate(getLayoutInflater());
        mLuckyNumberGame = getIntent().getExtras().getParcelable(Constants.KEY_LN_GAME);

        setContentView(mBinding.getRoot());
        startBetting(mBinding.wvSite);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void placeBets() {
        try{
            String betNumsSet = getStringifiedList(sResults);

            String betPlaceScript = loadScript(mLuckyNumberGame.getScript());

            if (mLuckyNumberGame.getName().equals("GOSLOTO RUSSIA 7/49")){
                betPlaceScript = String.format(betPlaceScript, betNumsSet, 3);
            }
            else betPlaceScript = String.format(betPlaceScript, betNumsSet, sResults.get(0).length - 1);

            evaluateScript(betPlaceScript);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
